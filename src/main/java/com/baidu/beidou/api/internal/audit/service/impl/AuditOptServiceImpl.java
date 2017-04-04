package com.baidu.beidou.api.internal.audit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.rpc.service.RiskMgtAuditService;
import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;
import com.baidu.beidou.api.internal.audit.service.AuditOptService;
import com.baidu.beidou.api.internal.audit.util.AuditOptBeanUtil;
import com.baidu.beidou.api.internal.audit.util.UnitTag;
import com.baidu.beidou.api.internal.audit.util.UnitTagUtil;
import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.cprounit.bo.DelMaterial;
import com.baidu.beidou.cprounit.bo.OnlineUnit;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitAuditing;
import com.baidu.beidou.cprounit.bo.UnitMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.constant.UnitTagConstant;
import com.baidu.beidou.cprounit.dao.DelMaterialDao;
import com.baidu.beidou.cprounit.dao.OnlineUnitDao;
import com.baidu.beidou.cprounit.dao.UnitDao;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.RefuseReasonUtils;
import com.baidu.beidou.cprounit.service.UbmcService;
import com.baidu.beidou.cprounit.service.UnitTagMgr;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestLite;
import com.baidu.beidou.cprounit.ubmcdriver.material.response.ResponseBaseMaterial;
import com.baidu.beidou.cprounit.vo.UnitTagInfo;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.cache.TradeCache;
import com.google.common.collect.Lists;

public class AuditOptServiceImpl implements AuditOptService {
	
	private static final Log LOG = LogFactory.getLog(AuditOptServiceImpl.class);
	
	private CproUnitMgr unitMgr = null;
	private UnitDao unitDao = null;
	private OnlineUnitDao onlineUnitDao = null;
	private UbmcService ubmcService = null;
	private TradeCache tradeCache = null;
	private DelMaterialDao delMaterDao = null;
    private RiskMgtAuditService riskMgtAuditService;
    
    private UnitTagMgr unitTagMgr = null;
	
	public Unit auditPass(Integer userId, Long unitId, Integer tradeId, Integer tradeModified,
			Integer accuracyType, Integer beautyType, Integer vulgarType, 
			Integer cheatType, Integer dangerType, List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		
		Visitor visitor = ThreadContext.getSessionVisitor();
		if (unit.getState() != CproUnitConstant.UNIT_STATE_AUDITING) {
			LOG.warn("审核通过处于非审核中的创意" + unit.toString());
			return null;
		}
		
		unit.setState(CproUnitConstant.UNIT_STATE_NORMAL);
		unit.setAuditTime(new Date());
		unit.setHelpstatus(unit.getHelpstatus() & 7);
		
		// 设置分类
		if (tradeModified == QueryConstant.TRADE_MODIFIED) {
			setHelpStatus(unit, CproUnitConstant.UNIT_MAN_CATEGORY);
			// 设置新分类
			unit.getMaterial().setNewAdtradeid(tradeId);
			
			// 设置旧分类
			Integer oldTradeId = tradeCache.getOld2LabelTradeId(tradeId);
			if (oldTradeId != null && oldTradeId > 0) {
				unit.getMaterial().setAdtradeid(oldTradeId);
			} else {
				LOG.error("set old trade failed, no oldTradeId for newTradeId:" + tradeId);
			}
		}
		
		// 设置tag
		unit.getMaterial().setConfidenceLevel(accuracyType);
		unit.getMaterial().setBeautyLevel(beautyType);
		unit.getMaterial().setVulgarLevel(vulgarType);
		unit.getMaterial().setCheatLevel(cheatType);
		
		// 新增设置tag @zhangzhenhua02
		int tagMask = UnitTagUtil.getTagMask(accuracyType, beautyType, cheatType, vulgarType, dangerType);
		unit.getMaterial().setTagMask(tagMask);

        // 如果存在影子创意（即老的可投放版本），则需将影子创意删除
        int shadowMcVersionId = unit.getMaterial().getShadowMcVersionId();
        if (shadowMcVersionId > 0) {
            this.addDelMaterial(unit.getMaterial().getUserId(), unit.getMaterial().getMcId(), shadowMcVersionId);
            
            // 影子创意下线，flag为offline
            unitMgr.recordShadowUnitLog(unit, CproUnitConstant.SHADOW_UNIT_OFFLINE);
            
            unit.getMaterial().setShadowMcVersionId(0); // 设置影子创意版本ID为0，即版本正式上线，不存在影子创意
        }
        
        unitMgr.commitOnlineUnit(unit); // 新版本上线

		unitDao.makePersistent(unit);
		
		LogUtils.businessBatchInfo(visitor, "auditPass[success]", unitId.toString(), "userId: " + userId);
		
		// 记录审核历史操作，以便历史操作工具查询
		OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_UNIT_PASS;
		OptContent content = new OptContent();
		content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_AUDITING));
		content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_NORMAL));
		content.setUserid(unit.getUser().getUserid());
		content.setGroupId(unit.getGroup().getGroupId());
		content.setOpLevel(opType.getOpLevel());
		content.setOpObjId(unit.getId());
		content.setOpType(opType.getOpType());
		optContents.add(content);
		
		return unit;
	}
	
	private boolean isDelMaterExist(long mcId, int mcVersionId) {
		return unitDao.isDelMaterExist(mcId, mcVersionId);
	}
	
	public void addDelMaterial(Integer userId, Long mcId, Integer mcVersionId) {
		if (isDelMaterExist(mcId, mcVersionId)) {
			return;
		}
		DelMaterial material = new DelMaterial(new Date(), userId, mcId, mcVersionId);
		delMaterDao.makePersistent(material);
	}
	
	public Unit reauditPass(Integer userId, Long unitId, List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		
		Visitor visitor = ThreadContext.getSessionVisitor();
			
		if (unit.getState() != CproUnitConstant.UNIT_STATE_REFUSE) {
			LOG.warn("重审通过处于非审核拒绝的创意" + unit.toString());
			return null;
		}
		
		unit.setState(Integer.valueOf(CproUnitConstant.UNIT_STATE_PAUSE));
		unitDao.deleteAuditing(unit.getUser().getUserid(), unit.getAuditing());
		unit.setAuditing(null);
		unit.setAuditTime(new Date());
		
        // 如果存在影子创意（即老的可投放版本），则需将影子创意删除，同时旧版本需上线
        int shadowMcVersionId = unit.getMaterial().getShadowMcVersionId();
        if (shadowMcVersionId > 0) {
            this.addDelMaterial(unit.getMaterial().getUserId(), unit.getMaterial().getMcId(), shadowMcVersionId);
            unit.getMaterial().setShadowMcVersionId(0); // 设置影子创意版本ID为0，即不存在影子创意
        }
        unitMgr.deleteOnlineUnit(unitId);
		
		unitDao.makePersistent(unit);
		
		LogUtils.businessBatchInfo(visitor, "reauditPass[success]", unitId.toString(), "userId: " + userId);
		
		// 记录审核历史操作，以便历史操作工具查询
		OpTypeVo opType = OptHistoryConstant.OPTYPE_REAUDIT_UNIT_PASS;
		OptContent content = new OptContent();
		content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
		content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_PAUSE));
		content.setUserid(unit.getUser().getUserid());
		content.setGroupId(unit.getGroup().getGroupId());
		content.setOpLevel(opType.getOpLevel());
		content.setOpObjId(unit.getId());
		content.setOpType(opType.getOpType());
		optContents.add(content);
		
		return unit;
	}
	
	public Unit auditRefuse(Integer userId, Long unitId, Integer tradeId, Integer tradeModified,
			Integer accuracyType, Integer beautyType, Integer vulgarType, Integer cheatType,
			Integer dangerType, List<Integer> refuseReasonIds, List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		
		// 拼装拒绝理由
		String refuseRea = RefuseReasonUtils.getRefuseReason(refuseReasonIds);
		
		Visitor visitor = ThreadContext.getSessionVisitor();
			
		if (unit.getState() != CproUnitConstant.UNIT_STATE_AUDITING) {
			LOG.warn("审核拒绝处于非审核中的创意" + unit.toString());
			return null;
		}
		
		unit.setState(Integer.valueOf(CproUnitConstant.UNIT_STATE_REFUSE));
		UnitAuditing auditRea = new UnitAuditing(unit.getId(), refuseRea);
		if (unit.getUser() == null || unit.getUser().getUserid() == null) {
			LogUtils.debug(LOG, unit.toString() + " doesn't have user");
			return null;
		}
		auditRea = unitDao.addAuditing(unit.getUser().getUserid(), auditRea);
		unit.setAuditing(auditRea);
		unit.setAuditTime(new Date());
		unit.setHelpstatus(unit.getHelpstatus() & 7);

		// 设置分类
		if (tradeModified == QueryConstant.TRADE_MODIFIED) {
			setHelpStatus(unit, CproUnitConstant.UNIT_MAN_CATEGORY);
			// 设置新分类
			unit.getMaterial().setNewAdtradeid(tradeId);
			
			// 设置旧分类
			Integer oldTradeId = tradeCache.getOld2LabelTradeId(tradeId);
			if (oldTradeId != null && oldTradeId > 0) {
				unit.getMaterial().setAdtradeid(oldTradeId);
			} else {
				LOG.error("set old trade failed, no oldTradeId for newTradeId:" + tradeId);
			}
		}
		
		// 设置tag
		unit.getMaterial().setConfidenceLevel(accuracyType);
		unit.getMaterial().setBeautyLevel(beautyType);
		unit.getMaterial().setVulgarLevel(vulgarType);
		unit.getMaterial().setCheatLevel(cheatType);
		
		// 新增设置tag @zhangzhenhua02
		int tagMask = UnitTagUtil.getTagMask(accuracyType, beautyType, cheatType, vulgarType, dangerType);
		unit.getMaterial().setTagMask(tagMask);
		
        // 如果存在影子创意（即老的可投放版本），则需将影子创意删除，同时旧版本需上线
        int shadowMcVersionId = unit.getMaterial().getShadowMcVersionId();
        if (shadowMcVersionId > 0) {
            this.addDelMaterial(unit.getMaterial().getUserId(), unit.getMaterial().getMcId(), shadowMcVersionId);
            
            // 影子创意下线，flag为offline
            unitMgr.recordShadowUnitLog(unit, CproUnitConstant.SHADOW_UNIT_OFFLINE);
            
            unit.getMaterial().setShadowMcVersionId(0); // 设置影子创意版本ID为0，即不存在影子创意
        }
        unitMgr.deleteOnlineUnit(unitId);

		unitDao.makePersistent(unit);
		
		LogUtils.businessBatchInfo(visitor, "auditRefuse[success]", unitId.toString(), "userId: " + userId);
		
		// 记录审核历史操作，以便历史操作工具查询
		OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_UNIT_REFUSE;
		OptContent content = new OptContent();
		content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_AUDITING));
		content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
		content.setUserid(unit.getUser().getUserid());
		content.setGroupId(unit.getGroup().getGroupId());
		content.setOpLevel(opType.getOpLevel());
		content.setOpObjId(unit.getId());
		content.setOpType(opType.getOpType());
		optContents.add(content);
		
		return unit;
	}
	
	public Unit reauditRefuse(Integer userId, Long unitId, 
			List<Integer> refuseReasonIds, List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		int preUnitState = unit.getState();
		
		// 拼装拒绝理由
		String refuseRea = RefuseReasonUtils.getRefuseReason(refuseReasonIds);
		
		Visitor visitor = ThreadContext.getSessionVisitor();
			
		if (unit.getState() != CproUnitConstant.UNIT_STATE_NORMAL 
				&& unit.getState() != CproUnitConstant.UNIT_STATE_PAUSE) {
			LOG.warn("复审拒绝处于非审核中或者暂停的创意" + unit.toString());
			return null;
		}
		
		unit.setState(Integer.valueOf(CproUnitConstant.UNIT_STATE_REFUSE));
		
		// 更新状态和拒绝理由
		UnitAuditing auditRea = unit.getAuditing();
		if (auditRea == null) {
			auditRea = new UnitAuditing(unit.getId(), refuseRea);
			unitDao.addAuditing(unit.getUser().getUserid(), auditRea);
		} else {
			auditRea.setRefuseReason(refuseRea);
		}
		unit.setAuditing(auditRea);
		unit.setAuditTime(new Date());

        // 如果存在影子创意（即老的可投放版本），则需将影子创意删除，同时旧版本需上线
        int shadowMcVersionId = unit.getMaterial().getShadowMcVersionId();
        if (shadowMcVersionId > 0) {
            this.addDelMaterial(unit.getMaterial().getUserId(), unit.getMaterial().getMcId(), shadowMcVersionId);
            unit.getMaterial().setShadowMcVersionId(0); // 设置影子创意版本ID为0，即不存在影子创意
        }
        unitMgr.deleteOnlineUnit(unitId);

        unitDao.makePersistent(unit);

		LogUtils.businessBatchInfo(visitor, "auditRefuse[success]", unitId.toString(), "userId: " + userId);
		
		// 记录审核历史操作，以便历史操作工具查询
		OpTypeVo opType = OptHistoryConstant.OPTYPE_REAUDIT_UNIT_REFUSE;
		OptContent content = new OptContent();
		content.setPreContent(opType.getTransformer().toDbString(preUnitState));
		content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
		content.setUserid(unit.getUser().getUserid());
		content.setGroupId(unit.getGroup().getGroupId());
		content.setOpLevel(opType.getOpLevel());
		content.setOpObjId(unit.getId());
		content.setOpType(opType.getOpType());
		optContents.add(content);
		
		return unit;
	}
	
	public List<AuditOpt> generateAuditOpt(List<Unit> unitList, Visitor visitor, boolean isRefused) {
		
		List<AuditOpt> result = new ArrayList<AuditOpt>();
		
		int auditorId = visitor.getUserid();
		String auditorName = visitor.getUsername();
		
		List<RequestBaseMaterial> requestList = new LinkedList<RequestBaseMaterial>();
		
		for (Unit unit : unitList) {
			AuditOpt opt = AuditOptBeanUtil.createAuditOptByUnit(unit, auditorId, auditorName, isRefused);
			result.add(opt);
			
			Long mcId = unit.getMaterial().getMcId();
			Integer versionId = unit.getMaterial().getMcVersionId();
			RequestLite request = new RequestLite(mcId, versionId);
			requestList.add(request);
		}
		
		// 在ubmc中增加版本，以供审核历史展示
		List<ResponseBaseMaterial> historyUnitResults = ubmcService.addVersion(requestList);
		if (CollectionUtils.isEmpty(historyUnitResults) || historyUnitResults.size() != requestList.size()) {
			LOG.error("backup audit history failed, for add version to ubmc");
			return new ArrayList<AuditOpt>();
		}
		
		// 备份审核历史物料成功，则记录备份成功的mcId和versionId
		for (int index = 0; index < historyUnitResults.size(); index++) {
			ResponseBaseMaterial historyUnitResult = historyUnitResults.get(index);
			AuditOpt opt = result.get(index);
			
			opt.setMcId(historyUnitResult.getMcId());
			opt.setMcVersionId(historyUnitResult.getVersionId());
		}
		
		// 生成临时URL，以供前端展示
		// url没有获取到，不影响记录审核历史
		Map<RequestBaseMaterial, String> urlResults = unitMgr.generateMaterUrl(requestList);
		if (urlResults == null || urlResults.isEmpty()) {
			LOG.error("generate tmp url failed, for audit opt");
		}
		for (int index = 0; index < requestList.size(); index++) {
			RequestLite request = (RequestLite)requestList.get(index);
			AuditOpt opt = result.get(index);
			
			String url = urlResults.get(request);
			opt.setMaterUrl(url);
		}
		return result;
	}

    /**
     * Function: 记录审核日志，推送审核结果给风控团队
     *      1. 记录创意审核物料版本，以便后续进行清理
     *      2. 推送审核结果给风控团队
     * 
     * @author genglei01
     * @param unit 创意信息
     * @param auditResult 审核操作结果，0代表有效，4代表拒绝
     * @param refuseReasonIds   拒绝理由ID列表
     * @param auditorId   审核员ID
     * @return boolean 是否成功
     */
    public boolean pushAuditLog(Unit unit, Integer auditResult, List<Integer> refuseReasonIds, Integer auditorId) {
        Long mcId = unit.getMaterial().getMcId();
        Integer versionId = unit.getMaterial().getMcVersionId();

        // 在ubmc中增加版本，以供审核历史展示
        RequestLite historyUnit = new RequestLite(mcId, versionId);
        List<RequestBaseMaterial> historyUnits = new LinkedList<RequestBaseMaterial>();
        historyUnits.add(historyUnit);
        List<ResponseBaseMaterial> historyUnitResults = ubmcService.addVersion(historyUnits);
        if (CollectionUtils.isEmpty(historyUnitResults) || historyUnitResults.get(0) == null) {
            LOG.error("add version failed to backup audit history");
            return false;
        }
        ResponseBaseMaterial historyUnitResult = historyUnitResults.get(0);
        Integer newVersionId = historyUnitResult.getVersionId();

        AuditLogItem auditLog = AuditOptBeanUtil.createAuditLogItemByUnit(unit, 
                auditResult, refuseReasonIds, auditorId);
        auditLog.setNewVersionId(newVersionId);
        
        return riskMgtAuditService.pushAuditLog(auditLog);
    }

	public void modifyTag(Integer userId, Long unitId, Integer tagType, Integer tagValue) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return;
		}
		switch (tagType) {
			case QueryConstant.QueryModifyTag.confidenceLevel:
				unit.getMaterial().setConfidenceLevel(tagValue);
				break;
			case QueryConstant.QueryModifyTag.beautyLevel:
				unit.getMaterial().setBeautyLevel(tagValue);
				break;
			case QueryConstant.QueryModifyTag.cheatLevel:
				unit.getMaterial().setCheatLevel(tagValue);
				break;
			case QueryConstant.QueryModifyTag.vulgarLevel:
				unit.getMaterial().setVulgarLevel(tagValue);
				break;
			default:
				break;
		}
		// 更新tagMask
		Integer oldTagMask = unit.getMaterial().getTagMask();
		unit.getMaterial().setTagMask(UnitTagUtil.getNewTagMask(oldTagMask, tagType, tagValue));
		unitDao.makePersistent(unit);

        // 如果创意状态为有效，则不仅需要更新cprounitmater[0-7]表中数据，还需要更新online_unit中物料数据
        if (unit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
            unitMgr.commitOnlineUnit(unit);
        }
	}
	
	public void modifyTrade(Integer userId, Long unitId, Integer tradeId, 
			List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
			return;
		}
		int preUnitTradeId = unit.getMaterial().getNewAdtradeid();
		
		setHelpStatus(unit, CproUnitConstant.UNIT_MAN_CATEGORY);
		// 设置新分类
		unit.getMaterial().setNewAdtradeid(tradeId);
		
		// 设置旧分类
		Integer oldTradeId = tradeCache.getOld2LabelTradeId(tradeId);
		if (oldTradeId != null && oldTradeId > 0) {
			unit.getMaterial().setAdtradeid(oldTradeId);
		} else {
			LOG.error("set old trade failed, no oldTradeId for newTradeId:" + tradeId);
		}
		unit.getMaterial().setConfidenceLevel(AuditConstant.CONFIDENCE_LEVEL_HIGH);
		
		// 更新tagMask
		Integer oldTagMask = unit.getMaterial().getTagMask();
		unit.getMaterial().setTagMask(UnitTagUtil.getNewTagMask(oldTagMask, QueryConstant.QueryModifyTag.confidenceLevel, 
																AuditConstant.CONFIDENCE_LEVEL_HIGH));
		
		unitDao.makePersistent(unit);
		
        // 如果创意状态为有效，则不仅需要更新cprounitmater[0-7]表中数据，还需要更新online_unit中物料数据
        if (unit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
            unitMgr.commitOnlineUnit(unit);
        }

		// 记录审核历史操作，以便历史操作工具查询
		OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_CHANGE_ADTRADE;
		OptContent content = new OptContent();
		content.setPreContent(opType.getTransformer().toDbString(preUnitTradeId));
		content.setPostContent(opType.getTransformer().toDbString(tradeId));
		content.setUserid(unit.getUser().getUserid());
		content.setGroupId(unit.getGroup().getGroupId());
		content.setOpLevel(opType.getOpLevel());
		content.setOpObjId(unit.getId());
		content.setOpType(opType.getOpType());
		optContents.add(content);
	}
	
    /**
     * 更新创意标注
     * 
     * @param userId userId
     * @param unitId 创意id
     * @param tradeId 行业id
     * @param tagTypeValueMap tag以及tagValue的map
     * @param modTime 更新时间
     * @param optContents 操作历史
     */
    public void modifyTagAndTrade(Integer userId, Long unitId, Integer tradeId, Map<Integer, Integer> tagTypeValueMap,
            Date modTime, List<OptContent> optContents) {
		Unit unit = unitDao.findById(userId, unitId);
		if (unit == null || unit.getMaterial() == null) {
		    LOG.error("unit does not exit:" + unitId);
			return;
		}
		Integer tagMask = unit.getMaterial().getTagMask();
		UnitTag unitTag = UnitTagUtil.getUnitTag(tagMask);
		Integer confidenceLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.confidenceLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.confidenceLevel) :
					unitTag.getConfidenceLevel();
		Integer beautyLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.beautyLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.beautyLevel) :
					unitTag.getBeautyLevel();
		Integer cheatLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.cheatLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.cheatLevel) :
					unitTag.getCheatLevel();
		Integer vulgarLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.vulgarLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.vulgarLevel) :
					unitTag.getVulgarLevel();
		Integer dangerLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.dangerLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.dangerLevel) :
					unitTag.getDangerLevel();
		// 计算新值
		tagMask = UnitTagUtil.getTagMask(confidenceLevel, beautyLevel, cheatLevel, vulgarLevel, dangerLevel);
		Integer preUnitTradeId = unit.getMaterial().getNewAdtradeid();
		Integer newAdTradeId;
		if (tradeId != -1) {
			newAdTradeId =  tradeId;
			// 更改了行业，准确度就修改
			confidenceLevel = AuditConstant.CONFIDENCE_LEVEL_HIGH;
		}
		else {
			newAdTradeId = unit.getMaterial().getNewAdtradeid();
		}
		Integer adTradeId = tradeCache.getOld2LabelTradeId(newAdTradeId);
		if (adTradeId == null || adTradeId <= 0) {
			adTradeId = unit.getMaterial().getAdtradeid();
			LOG.error("set old trade failed, no oldTradeId for newTradeId:" + tradeId);
		}
		if (!modTime.equals(unit.getChaTime())) {
            LOG.error("unit chaTime does not equal： " + unitId);
            return;
        }
		// 更新新值
		unitDao.updateUnitTagAndTrade(userId, unitId,  newAdTradeId, adTradeId, confidenceLevel, 
								beautyLevel, cheatLevel, vulgarLevel, tagMask, modTime);
		
		// 行业更改过，记录审核历史操作，以便历史操作工具查询
		if (tradeId != -1) {
			OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_CHANGE_ADTRADE;
			OptContent content = new OptContent();
			content.setPreContent(opType.getTransformer().toDbString(preUnitTradeId));
			content.setPostContent(opType.getTransformer().toDbString(tradeId));
			content.setUserid(unit.getUser().getUserid());
			content.setGroupId(unit.getGroup().getGroupId());
			content.setOpLevel(opType.getOpLevel());
			content.setOpObjId(unit.getId());
			content.setOpType(opType.getOpType());
			optContents.add(content);
		}
		
		// online_unit
		OnlineUnit onlineUnit = onlineUnitDao.get(unitId);
		if (onlineUnit == null) {
			LOG.error("onlineUnit does not exist：" + unitId);
			return;
		}
		if (!modTime.equals(onlineUnit.getModTime())) {
			LOG.error("onlineUnit modeTime does not equal： " + unitId);
			return;
		}
		tagMask = onlineUnit.getTagMask();
		unitTag = UnitTagUtil.getUnitTag(tagMask);
		confidenceLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.confidenceLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.confidenceLevel) : 
					unitTag.getConfidenceLevel();
		beautyLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.beautyLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.beautyLevel) : 
					unitTag.getBeautyLevel();
		cheatLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.cheatLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.cheatLevel) : 
					unitTag.getCheatLevel();
		vulgarLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.vulgarLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.vulgarLevel) : 
					unitTag.getVulgarLevel();
		dangerLevel = (tagTypeValueMap.get(QueryConstant.QueryModifyTag.dangerLevel) != -1)
					? tagTypeValueMap.get(QueryConstant.QueryModifyTag.dangerLevel) : 
					unitTag.getDangerLevel();			
		// 计算新值
		tagMask = UnitTagUtil.getTagMask(confidenceLevel, beautyLevel, cheatLevel, vulgarLevel, dangerLevel);
		preUnitTradeId = onlineUnit.getNewAdtradeid();
		if (tradeId != -1) {
			newAdTradeId =  tradeId;
			// 更改了行业，准确度就修改
			confidenceLevel = AuditConstant.CONFIDENCE_LEVEL_HIGH;
		}
		else {
			newAdTradeId = onlineUnit.getNewAdtradeid();
		}
		adTradeId = tradeCache.getOld2LabelTradeId(newAdTradeId);
		if (adTradeId == null || adTradeId <= 0) {
			adTradeId = onlineUnit.getAdtradeid();
			LOG.error("set old trade failed, no oldTradeId for newTradeId:" + tradeId);
		}
		// *****用sql的方式*****
		onlineUnit.setConfidenceLevel(confidenceLevel);
		onlineUnit.setBeautyLevel(beautyLevel);
		onlineUnit.setCheatLevel(cheatLevel);
		onlineUnit.setVulgarLevel(vulgarLevel);
		onlineUnit.setNewAdtradeid(newAdTradeId);
		onlineUnit.setAdtradeid(adTradeId);
		onlineUnit.setTagMask(tagMask);
		onlineUnitDao.update(onlineUnit);	
	}
	
	private void setHelpStatus(Unit unit, int btStatus) {
		int helpStatus = unit.getHelpstatus().intValue();
		helpStatus = helpStatus | btStatus;
		unit.setHelpstatus(helpStatus);
	}
	
	public void modifyRefuseReason(Integer userId, Long unitId, List<Integer> refuseReasonIds) {
		// 拼装拒绝理由
		String refuseRea = RefuseReasonUtils.getRefuseReason(refuseReasonIds);
		if (StringUtils.isEmpty(refuseRea)) {
			return;
		}
		
		Date auditTime = new Date();
		Unit unit = unitDao.findById(userId, unitId);
		if (unit.getState() != CproUnitConstant.UNIT_STATE_REFUSE) {
			return;
		}
		
		// 更新状态和拒绝理由
		UnitAuditing auditRea = unit.getAuditing();
		if (auditRea == null) {
			auditRea = new UnitAuditing(unit.getId(), refuseRea);
			unitDao.addAuditing(unit.getUser().getUserid(), auditRea);
		} else {
			auditRea.setRefuseReason(refuseRea);
		}
		unit.setAuditing(auditRea);
		unit.setAuditTime(auditTime);
		unitDao.makePersistent(unit);
		Visitor visitor = ThreadContext.getSessionVisitor();
		LogUtils.businessBatchInfo(visitor, "reaudit Unit Change Reason" 
				+ "unitId:" + unitId + ", refuseReasonIds:" + refuseRea, "userId: " + userId);
	}
	
    /**
     * Function: 审核操作
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @param mcId    mcId
     * @param versionId    versionId
     * @param newTradeId 三级行业ID
     * @param tradeModified 行业是否被修改，1表示被修改
     * @param unitTag 创意标注，目前支持5个标注
     * @param unitTagModified   创意标注是否修改
     * @param auditType 审核操作类型，1代表一审，2代表复审
     * @param auditResult 审核操作结果，0代表有效，4代表拒绝
     * @param auditResultModified   审核操作结果是否修改
     * @param refuseReasonIds 拒绝理由ID列表
     * @param optContents 历史操作记录
     * @return  创意信息Unit
     */
    public Unit auditResult(Integer userId, Long unitId, Long mcId, Integer versionId, Integer newTradeId, 
            boolean tradeModified, Integer unitTag, boolean unitTagModified, Integer auditType, Integer auditResult, 
            boolean auditResultModified, List<Integer> refuseReasonIds, List<OptContent> optContents) {
        Unit unit = unitDao.findById(userId, unitId);
        if (unit == null || unit.getMaterial() == null) {
            return null;
        }
        
        if (versionId == null || !versionId.equals(unit.getMaterial().getMcVersionId())) {
            LOG.warn("the version of the unit has changed." + unit.toString());
            return null;
        }
        
        UnitMater mater = unit.getMaterial();
        if (auditResultModified) {
            OptContent content = new OptContent();
            int shadowMcVersionId = mater.getShadowMcVersionId();
            
            if (auditResult == AuditConstant.BEIDOU_AUDIT_PASS) {
                // 审核通过
                if (auditType == AuditConstant.BEIDOU_FIRST_AUDIT) {
                    // 一审通过
                    if (unit.getState() != CproUnitConstant.UNIT_STATE_AUDITING) {
                        LOG.warn("audit-pass the unit not in auditing-state for first audit " + unit.toString());
                        return null;
                    }
                    unit.setState(CproUnitConstant.UNIT_STATE_NORMAL);
                    unit.setHelpstatus(unit.getHelpstatus() & 7);
                    
                    // 记录审核历史操作，以便历史操作工具查询
                    OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_UNIT_PASS;
                    content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_AUDITING));
                    content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_NORMAL));
                    content.setUserid(unit.getUser().getUserid());
                    content.setGroupId(unit.getGroup().getGroupId());
                    content.setOpLevel(opType.getOpLevel());
                    content.setOpObjId(unit.getId());
                    content.setOpType(opType.getOpType());
                    
                    // 如果存在影子创意（即老的可投放版本），则需将影子创意删除
                    if (shadowMcVersionId > 0) {
                        // 影子创意下线，flag为offline
                        unitMgr.recordShadowUnitLog(unit, CproUnitConstant.SHADOW_UNIT_OFFLINE);
                    }
                    
                    // 一审通过，删除影子创意标注
                    unitTagMgr.deleteUnitTags(Lists.newArrayList(unitId));
                } else {
                    // 复审通过
                    if (unit.getState() != CproUnitConstant.UNIT_STATE_REFUSE) {
                        LOG.warn("audit-pass the unit not in refused-state for second audit " + unit.toString());
                        return null;
                    }
                    unit.setState(CproUnitConstant.UNIT_STATE_PAUSE);
                    unitDao.deleteAuditing(unit.getUser().getUserid(), unit.getAuditing());
                    unit.setAuditing(null);
                    
                    // 记录审核历史操作，以便历史操作工具查询
                    OpTypeVo opType = OptHistoryConstant.OPTYPE_REAUDIT_UNIT_PASS;
                    content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
                    content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_PAUSE));
                    content.setUserid(unit.getUser().getUserid());
                    content.setGroupId(unit.getGroup().getGroupId());
                    content.setOpLevel(opType.getOpLevel());
                    content.setOpObjId(unit.getId());
                    content.setOpType(opType.getOpType());
                    
                    unitMgr.deleteOnlineUnit(unitId);   // 复审通过，下线老创意
                }
            } else {
                int preUnitState = unit.getState();
                
                // 审核拒绝
                if (auditType == AuditConstant.BEIDOU_FIRST_AUDIT) {
                    // 一审拒绝
                    if (preUnitState != CproUnitConstant.UNIT_STATE_AUDITING) {
                        LOG.warn("audit-refuse the unit not in auditing-state for first audit " + unit.toString());
                        return null;
                    }
                    unit.setHelpstatus(unit.getHelpstatus() & 7);
                    
                    OpTypeVo opType = OptHistoryConstant.OPTYPE_AUDIT_UNIT_REFUSE;
                    content.setPreContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_AUDITING));
                    content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
                    content.setUserid(unit.getUser().getUserid());
                    content.setGroupId(unit.getGroup().getGroupId());
                    content.setOpLevel(opType.getOpLevel());
                    content.setOpObjId(unit.getId());
                    content.setOpType(opType.getOpType());
                    optContents.add(content);
                    
                    if (shadowMcVersionId > 0) {
                        // 影子创意下线，flag为offline
                        unitMgr.recordShadowUnitLog(unit, CproUnitConstant.SHADOW_UNIT_OFFLINE);
                    }
                } else {
                    // 复审拒绝
                    if (preUnitState != CproUnitConstant.UNIT_STATE_NORMAL 
                            && preUnitState != CproUnitConstant.UNIT_STATE_PAUSE
                            && preUnitState != CproUnitConstant.UNIT_STATE_REFUSE) {
                        LOG.warn("audit-refuse the unit not in normal-state or " +
                                "pause-state or refuse-state for second audit " + unit.toString());
                        return null;
                    }
                    
                    // 记录审核历史操作，以便历史操作工具查询
                    OpTypeVo opType = OptHistoryConstant.OPTYPE_REAUDIT_UNIT_REFUSE;
                    content.setPreContent(opType.getTransformer().toDbString(preUnitState));
                    content.setPostContent(opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_REFUSE));
                    content.setUserid(unit.getUser().getUserid());
                    content.setGroupId(unit.getGroup().getGroupId());
                    content.setOpLevel(opType.getOpLevel());
                    content.setOpObjId(unit.getId());
                    content.setOpType(opType.getOpType());
                }
                
                // 拼装拒绝理由
                String refuseRea = RefuseReasonUtils.getRefuseReason(refuseReasonIds);
                unit.setState(CproUnitConstant.UNIT_STATE_REFUSE);
                
                // 更新状态和拒绝理由
                UnitAuditing auditRea = unit.getAuditing();
                if (auditRea == null) {
                    auditRea = new UnitAuditing(unit.getId(), refuseRea);
                    unitDao.addAuditing(unit.getUser().getUserid(), auditRea);
                } else {
                    auditRea.setRefuseReason(refuseRea);
                }
                unit.setAuditing(auditRea);
                
                unitMgr.deleteOnlineUnit(unitId);   // 如有影子创意需下线
                
                // 一审拒绝，删除影子创意标注；二审拒绝，删除当前（一审通过上线）创意标注
                unitTagMgr.deleteUnitTags(Lists.newArrayList(unitId)); 
            }
            optContents.add(content);
            
            // 如果存在影子创意（即老的可投放版本），则需将影子创意删除，同时旧版本需下线
            if (shadowMcVersionId > 0) {
                this.addDelMaterial(mater.getUserId(), mater.getMcId(), shadowMcVersionId);
                mater.setShadowMcVersionId(0); // 设置影子创意版本ID为0，即不存在影子创意
            }
            unit.setAuditTime(new Date());
        }
        
        // 设置创意分类
        if (tradeModified) {
            setHelpStatus(unit, CproUnitConstant.UNIT_MAN_CATEGORY);
            // 设置新分类
            mater.setNewAdtradeid(newTradeId);
            
            // 设置旧分类
            Integer oldTradeId = tradeCache.getOld2LabelTradeId(newTradeId);
            if (oldTradeId != null && oldTradeId > 0) {
                mater.setAdtradeid(oldTradeId);
            } else {
                LOG.error("set old trade failed, no oldTradeId for newTradeId:" + newTradeId);
            }
        }
        
        // 设置创意标注
        if (unitTagModified) {
            mater.setConfidenceLevel(UnitTagUtil.getTagValueByType(unitTag, QueryConstant.QueryModifyTag.confidenceLevel));
            mater.setBeautyLevel(UnitTagUtil.getTagValueByType(unitTag, QueryConstant.QueryModifyTag.beautyLevel));
            mater.setCheatLevel(UnitTagUtil.getTagValueByType(unitTag, QueryConstant.QueryModifyTag.cheatLevel));
            mater.setVulgarLevel(UnitTagUtil.getTagValueByType(unitTag, QueryConstant.QueryModifyTag.vulgarLevel));
            mater.setTagMask(unitTag);
        }
        
        // 如果创意的状态是有效状态，则需要更新已上线创意信息
        if (unit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
            unitMgr.commitOnlineUnit(unit); // 新版本上线
        }

        unitDao.makePersistent(unit);
        
        return unit;
    }
    
    /**
     * Function: 初始化标注信息
     *  在一审通过或者一审拒绝时，因为这是旧有影子创意或者已上线创意要下线，需要重新对新物料进行标注
     * 
     * @author genglei01
     * @param userId
     * @param unitId
     * @param mcId
     * @param versionId 
     */
    private void initTagInfo(Integer userId, Long unitId, Long mcId, Integer versionId) {
        // 如果一审通过，需要将标注信息初始化
        UnitTagInfo tagInfo = new UnitTagInfo();
        tagInfo.setUnitId(unitId);
        tagInfo.setUserId(userId);
        tagInfo.setMcId(mcId);
        tagInfo.setMcVersionId(versionId);
        tagInfo.setUnitTags(UnitTagConstant.EMPTY_UNIT_TAG);
        
        List<UnitTagInfo> unitTagInfoList = Lists.newArrayList();
        unitTagInfoList.add(tagInfo);
        unitTagMgr.updateUnitTags(unitTagInfoList);
    }

	public UnitDao getUnitDao() {
		return unitDao;
	}

	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = unitDao;
	}

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public UbmcService getUbmcService() {
		return ubmcService;
	}

	public void setUbmcService(UbmcService ubmcService) {
		this.ubmcService = ubmcService;
	}

	public TradeCache getTradeCache() {
		return tradeCache;
	}

	public void setTradeCache(TradeCache tradeCache) {
		this.tradeCache = tradeCache;
	}

	public void setDelMaterDao(DelMaterialDao delMaterDao) {
		this.delMaterDao = delMaterDao;
	}

    /**
     * 
     * @return onlineUnitDao
     */
    public OnlineUnitDao getOnlineUnitDao() {
        return onlineUnitDao;
    }

    /**
     * 
     * @param onlineUnitDao onlineUnitDao
     */
    public void setOnlineUnitDao(OnlineUnitDao onlineUnitDao) {
        this.onlineUnitDao = onlineUnitDao;
    }

    public void setRiskMgtAuditService(RiskMgtAuditService riskMgtAuditService) {
        this.riskMgtAuditService = riskMgtAuditService;
    }

    /**
     * Function: setUnitTagMgr
     * 
     * @author genglei01
     * @param unitTagMgr unitTagMgr
     */
    public void setUnitTagMgr(UnitTagMgr unitTagMgr) {
        this.unitTagMgr = unitTagMgr;
    }

}
