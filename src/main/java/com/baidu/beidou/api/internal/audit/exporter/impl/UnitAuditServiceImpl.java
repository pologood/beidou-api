package com.baidu.beidou.api.internal.audit.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.UnitAuditService;
import com.baidu.beidou.api.internal.audit.service.AuditOptService;
import com.baidu.beidou.api.internal.audit.service.ProductInfoService;
import com.baidu.beidou.api.internal.audit.service.UnitAuditInfoService;
import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.UnitAuditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitReauditInfo;
import com.baidu.beidou.api.internal.audit.vo.request.AuditResultUnit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitTagAndTrade;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.exception.UserStateDisableException;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.cache.TradeCache;

public class UnitAuditServiceImpl implements UnitAuditService {
	
	private static final Log LOG = LogFactory.getLog(UnitAuditServiceImpl.class);
	
	private AuditOptService auditOptService;
	
	private UnitAuditInfoService unitAuditInfoService;
	
	private ProductInfoService productInfoService;
	
	private UserMgr userMgr;
	
	private TradeCache tradeCache;

	public AuditResult<UnitAuditInfo> getUnitAuditList(Integer userId, QueryUnitAudit query) {
		AuditResult<UnitAuditInfo> result = new AuditResult<UnitAuditInfo>();
		if (userId == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if (query == null || CollectionUtils.isEmpty(query.getUnitIds())) {
			result.setTotalNum(0);
			result.setTotalPage(0);
			return result;
		}
		
		int totalNum = (int) unitAuditInfoService.countAuditUnit(userId, query);
		int totalPage = (totalNum - 1) / query.getPageSize() + 1;
		
		List<UnitAuditInfo> list = unitAuditInfoService.updateUnitStateAndGetUnitAuditList(userId, query);
		result.addResults(list);
		
		result.setTotalNum(totalNum);
		result.setTotalPage(totalPage);
		
		return result;
	}
	
	public AuditResult<Object> getUnitAuditNum(Integer userId, QueryUnitAudit query) {
		AuditResult<Object> result = new AuditResult<Object>();
		if (userId == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if (query == null || CollectionUtils.isEmpty(query.getUnitIds())) {
			result.setTotalNum(0);
			result.setTotalPage(0);
			return result;
		}
		
		int totalNum = (int) unitAuditInfoService.countAuditUnit(userId, query);
		result.setTotalNum(totalNum);
		return result;
	}
	
	public AuditResult<UnitReauditInfo> getUnitReauditListByUnitIds(String unitIds) {
		AuditResult<UnitReauditInfo> result = new AuditResult<UnitReauditInfo>();
		
		if (StringUtils.isEmpty(unitIds)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		List<UnitReauditInfo> list = unitAuditInfoService.findReauditUnitByUnitIds(unitIds);
		if (CollectionUtils.isNotEmpty(list)) {
			result.addResults(list);
		}
		return result;
	}
	
	public AuditResult<UnitReauditInfo> getUnitReauditList(Integer userId, QueryUnitReaudit query) {
		AuditResult<UnitReauditInfo> result = new AuditResult<UnitReauditInfo>();
		if (userId == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		int totalNum = (int) unitAuditInfoService.countReauditUnit(userId, query);
		int totalPage = (totalNum - 1) / query.getPageSize() + 1;
		
		List<UnitReauditInfo> list = unitAuditInfoService.getUnitReauditList(userId, query);
		result.addResults(list);
		
		result.setTotalNum(totalNum);
		result.setTotalPage(totalPage);
		
		return result;
	}
	
	public AuditResult<AuditOpt> pass(List<AuditResultUnit> auditList, Integer auditorId) {
		AuditResult<AuditOpt> result = new AuditResult<AuditOpt>();
		List<AuditOpt> list = new ArrayList<AuditOpt>();
		
		if (CollectionUtils.isEmpty(auditList)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		Visitor visitor = this.getVisitor(auditorId);
		if (visitor == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		// 添加计时器，记录各步骤执行时间
		StopWatch sw = new StopWatch();
		sw.start();
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		List<Unit> unitList = new ArrayList<Unit>();
		for (AuditResultUnit auditUnit : auditList) {
			if (auditUnit.getTradeId() == null || auditUnit.getTradeId() < 0) {
				ThreadContext.putUserId(auditUnit.getUserId());
				Unit unit = auditOptService.reauditPass(auditUnit.getUserId(), 
						auditUnit.getUnitId(), optContents);
				
				// 如果操作成功，则unit非null，则保留记录审核历史
				if (unit != null) {
					unitList.add(unit);
				}
			} else {
				ThreadContext.putUserId(auditUnit.getUserId());
				Unit unit = auditOptService.auditPass(auditUnit.getUserId(), 
						auditUnit.getUnitId(), auditUnit.getTradeId(), auditUnit.getTradeModified(),
						auditUnit.getAccuracyType(), auditUnit.getBeautyType(),
						auditUnit.getVulgarType(), auditUnit.getCheatType(), auditUnit.getDangerType(), optContents);
				
				// 如果操作成功，则unit非null，则保留记录审核历史
				if (unit != null) {
					unitList.add(unit);
				}
			}
		}
		
		sw.stop();
		LOG.info("Step1(audit/reaudit pass) using " + sw.getTime() + " ms, for auditorId=" + auditorId + ", num=" + auditList.size());
		sw.reset();
		sw.start();
		
		// 记录并返回审核操作记录
		list = auditOptService.generateAuditOpt(unitList, visitor, false);
		result.addResults(list);
		
		sw.stop();
		LOG.info("Step2(record_audit_opt) using " + sw.getTime() + " ms, for auditorId=" + auditorId + ", num=" + auditList.size());
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); 
		
		return result;
	}
	
	private Visitor getVisitor(Integer auditorId) {
		Visitor visitor = null;
		try {
			visitor = userMgr.getUserByUserId(auditorId);
			ThreadContext.putVisitor(visitor);
		} catch (UserStateDisableException e) {
			LOG.error("get visitor failed for auditorId=" + auditorId, e);
			return null;
		}
		return visitor;
	}
	
	public AuditResult<AuditOpt> refuse(List<AuditResultUnit> auditList, 
			List<Integer> refuseReasonIds, Integer auditorId){
		AuditResult<AuditOpt> result = new AuditResult<AuditOpt>();
		List<AuditOpt> list = new ArrayList<AuditOpt>();
		
		if (CollectionUtils.isEmpty(auditList)
				|| CollectionUtils.isEmpty(refuseReasonIds)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		Visitor visitor = this.getVisitor(auditorId);
		if (visitor == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		// 添加计时器，记录各步骤执行时间
		StopWatch sw = new StopWatch();
		sw.start();
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		List<Unit> unitList = new ArrayList<Unit>();
		for (AuditResultUnit auditUnit : auditList) {
			if (auditUnit.getTradeId() == null || auditUnit.getTradeId() < 0) {
				ThreadContext.putUserId(auditUnit.getUserId());
				Unit unit = auditOptService.reauditRefuse(auditUnit.getUserId(), 
						auditUnit.getUnitId(), refuseReasonIds, optContents);
				
				// 如果操作成功，则unit非null，则保留记录审核历史
				if (unit != null) {
					unitList.add(unit);
				}
			} else {
				ThreadContext.putUserId(auditUnit.getUserId());
				Unit unit = auditOptService.auditRefuse(auditUnit.getUserId(), 
						auditUnit.getUnitId(), auditUnit.getTradeId(), auditUnit.getTradeModified(),
						auditUnit.getAccuracyType(), auditUnit.getBeautyType(),
						auditUnit.getVulgarType(), auditUnit.getCheatType(), auditUnit.getDangerType(), 
						refuseReasonIds, optContents);
				
				// 如果操作成功，则unit非null，则保留记录审核历史
				if (unit != null) {
					unitList.add(unit);
				}
			}
		}
		
		sw.stop();
		LOG.info("Step1(audit/reaudit pass) using " + sw.getTime() + " ms, for auditorId=" + auditorId + ", num=" + auditList.size());
		sw.reset();
		sw.start();
		
		// 记录并返回审核操作记录
		list = auditOptService.generateAuditOpt(unitList, visitor, true);
		result.addResults(list);
		
		sw.stop();
		LOG.info("Step2(record_audit_opt) using " + sw.getTime() + " ms, for auditorId=" + auditorId + ", num=" + auditList.size());
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); 
				
		return result;
	}
	
	public AuditResult<Object> modifyTag(List<Integer> userIds, List<Long> unitIds, 
			Integer tagType, Integer tagValue, Integer auditorId) {
		AuditResult<Object> result = new AuditResult<Object>();
		
		Visitor visitor = this.getVisitor(auditorId);
		if (visitor == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if (CollectionUtils.isEmpty(userIds) 
				|| CollectionUtils.isEmpty(unitIds)
				|| tagType == null || tagValue == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		result = this.validateTag(result, tagType, tagValue);
		if (result.getStatus() != ResponseStatus.SUCCESS) {
			return result;
		}
		
		for (int index = 0; index < unitIds.size(); index++) {
			ThreadContext.putUserId(userIds.get(index));
			auditOptService.modifyTag(userIds.get(index), 
					unitIds.get(index), tagType, tagValue);
		}
		
		return result;
	}
	
	private AuditResult<Object> validateTag(AuditResult<Object> result, Integer tagType, Integer tagValue) {
		if (tagType != QueryConstant.QueryModifyTag.confidenceLevel
				&& tagType != QueryConstant.QueryModifyTag.beautyLevel
				&& tagType != QueryConstant.QueryModifyTag.cheatLevel
				&& tagType != QueryConstant.QueryModifyTag.vulgarLevel
				&& tagType != QueryConstant.QueryModifyTag.dangerLevel) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		
		switch (tagType) {
			case QueryConstant.QueryModifyTag.confidenceLevel:
				if (tagValue < QueryConstant.QueryModifyTag.levelMinValue
						|| tagValue > QueryConstant.QueryModifyTag.confidenceLevelMaxValue) {
					result.setStatus(ResponseStatus.PARAM_ERROR);
				}
				break;
			case QueryConstant.QueryModifyTag.beautyLevel:
				if (tagValue < QueryConstant.QueryModifyTag.levelMinValue
						|| tagValue > QueryConstant.QueryModifyTag.beautyLevelMaxValue) {
					result.setStatus(ResponseStatus.PARAM_ERROR);
				}
				break;
			case QueryConstant.QueryModifyTag.cheatLevel:
				if (tagValue < QueryConstant.QueryModifyTag.levelMinValue
						|| tagValue > QueryConstant.QueryModifyTag.cheatLevelMaxValue) {
					result.setStatus(ResponseStatus.PARAM_ERROR);
					return result;
				}
				break;
			case QueryConstant.QueryModifyTag.vulgarLevel:
				if (tagValue < QueryConstant.QueryModifyTag.levelMinValue
						|| tagValue > QueryConstant.QueryModifyTag.vulgarLevelMaxValue) {
					result.setStatus(ResponseStatus.PARAM_ERROR);
				}
				break;
			case QueryConstant.QueryModifyTag.dangerLevel:
                if (tagValue < QueryConstant.QueryModifyTag.levelMinValue
                        || tagValue > QueryConstant.QueryModifyTag.dangerLevelMaxValue) {
                    result.setStatus(ResponseStatus.PARAM_ERROR);
                }
                break;
			default:
				break;
		}
		
		return result;
	}
	
	private AuditResult<Object> validateTagAndTrade(AuditResult<Object> result, QueryUnitTagAndTrade query) {
		int confidenceLevel = query.getTagTypeValueMap().get(QueryConstant.QueryModifyTag.confidenceLevel);
		if (confidenceLevel < -1
				|| confidenceLevel > QueryConstant.QueryModifyTag.confidenceLevelMaxValue) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		int beautyLevel = query.getTagTypeValueMap().get(QueryConstant.QueryModifyTag.beautyLevel);
		if (beautyLevel < -1
				|| beautyLevel > QueryConstant.QueryModifyTag.beautyLevelMaxValue) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		int cheatLevel = query.getTagTypeValueMap().get(QueryConstant.QueryModifyTag.cheatLevel);
		if (cheatLevel < -1
				|| cheatLevel > QueryConstant.QueryModifyTag.cheatLevelMaxValue) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		int vulgarLevel = query.getTagTypeValueMap().get(QueryConstant.QueryModifyTag.vulgarLevel);
		if (vulgarLevel < -1
				|| vulgarLevel > QueryConstant.QueryModifyTag.vulgarLevelMaxValue) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		int dangerLevel = query.getTagTypeValueMap().get(QueryConstant.QueryModifyTag.dangerLevel);
		if (dangerLevel < -1
				|| dangerLevel > QueryConstant.QueryModifyTag.dangerLevelMaxValue) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		if (query.getTradeId() == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		if (query.getModTime() == null) {
		    result.setStatus(ResponseStatus.PARAM_ERROR);
		}
		return result;
	}
	
	public AuditResult<Object> modifyTrade(List<Integer> userIds, List<Long> unitIds, 
			Integer tradeId, Integer auditorId) {
		AuditResult<Object> result = new AuditResult<Object>();
		
		if (CollectionUtils.isEmpty(unitIds) 
				|| CollectionUtils.isEmpty(userIds) 
				|| tradeId == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		Visitor visitor = this.getVisitor(auditorId);
		if (visitor == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < unitIds.size(); index++) {
			ThreadContext.putUserId(userIds.get(index));
			auditOptService.modifyTrade(userIds.get(index), 
					unitIds.get(index), tradeId, optContents);
		}
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}

    /**
     * modifyTagAndTrade: 重审页面，修改创意标签Tag和行业分类  此方法是针对UBI外包团队，审核系统不使用此方法
     * 
     * @version 1.0.0
     * @author zhangzhenhua02
     * @date Nov 20, 2014
     * @param queryList 请求列表
     * @param auditorId 审核员id
     * @return 审核结果
     */
    public AuditResult<Object> modifyTagAndTrade(List<QueryUnitTagAndTrade> queryList, Integer auditorId) {
		AuditResult<Object> result = new AuditResult<Object>();
		if (CollectionUtils.isEmpty(queryList)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
        }
        // 因为auditorId不全来自用户中心，而来自UBI团队，使用不对auditorId做验证
        /*
         * Visitor visitor = this.getVisitor(auditorId); if (visitor == null) {
         * result.setStatus(ResponseStatus.PARAM_ERROR); return result; }
         */
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (QueryUnitTagAndTrade query : queryList) {
			ThreadContext.putUserId(query.getUserId());
			result = this.validateTagAndTrade(result, query);
			if (result.getStatus() != ResponseStatus.SUCCESS) {
				return result;
			}
			auditOptService.modifyTagAndTrade(query.getUserId(), query.getUnitId(), query.getTradeId(),
					query.getTagTypeValueMap(), query.getModTime(), optContents);
		}
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}
	
	public AuditResult<Object> modifyRefuseReason(Integer userId, Long unitId, 
			List<Integer> refuseReasonIds, Integer auditorId) {
		AuditResult<Object> result = new AuditResult<Object>();
		
		Visitor visitor = this.getVisitor(auditorId);
		if (visitor == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if (userId == null || unitId == null || CollectionUtils.isEmpty(refuseReasonIds)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		auditOptService.modifyRefuseReason(userId, unitId, refuseReasonIds);
		
		return result;
	}
	
	public AuditResult<Long> findAuditingUnitIds(Integer userId, List<Long> unitIds) {
		AuditResult<Long> result = new AuditResult<Long>();
		
		if (userId == null || CollectionUtils.isEmpty(unitIds)
				|| unitIds.size() > QueryConstant.GET_AUDITING_UNIT_MAX) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		List<Long> auditingUnitIds = unitAuditInfoService.findAuditingUnitIds(userId, unitIds);
		result.setStatus(ResponseStatus.SUCCESS);
		result.addResults(auditingUnitIds);
		
		return result;
	}
	
	public AuditResult<ProductView> getProductViewList(Integer userId, QueryProduct query) {
		AuditResult<ProductView> result = new AuditResult<ProductView>();
		
		List<ProductView> list = productInfoService.getProductViewList(query);
		result.setStatus(ResponseStatus.SUCCESS);
		result.addResults(list);
		
		return result;
	}
	
	public AuditResult<Object> refuseProduct(Integer userId, List<Long> productIds) {
		AuditResult<Object> result = new AuditResult<Object>();
		
		if (userId == null || CollectionUtils.isEmpty(productIds)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		boolean flag = productInfoService.refuseProduct(productIds, userId);
		if (flag == false) {
			result.setStatus(ResponseStatus.ERROR);
		}
		
		return result;
	}

    /**
     * Function: 获取创意物料信息，包含图片或者flash的二进制文件
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @return UnitMaterialInfo
     */
    public AuditResult<UnitMaterialInfo> getUnitMaterailInfo(Integer userId, Long unitId) {
        AuditResult<UnitMaterialInfo> result = new AuditResult<UnitMaterialInfo>();
        UnitMaterialInfo info = unitAuditInfoService.getUnitMaterailInfo(userId, unitId);
        result.addResult(info);
        
        return result;
    }

	public AuditOptService getAuditOptService() {
		return auditOptService;
	}

	public void setAuditOptService(AuditOptService auditOptService) {
		this.auditOptService = auditOptService;
	}

	public UnitAuditInfoService getUnitAuditInfoService() {
		return unitAuditInfoService;
	}

	public void setUnitAuditInfoService(UnitAuditInfoService unitAuditInfoService) {
		this.unitAuditInfoService = unitAuditInfoService;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public TradeCache getTradeCache() {
		return tradeCache;
	}

	public void setTradeCache(TradeCache tradeCache) {
		this.tradeCache = tradeCache;
	}

	public ProductInfoService getProductInfoService() {
		return productInfoService;
	}

	public void setProductInfoService(ProductInfoService productInfoService) {
		this.productInfoService = productInfoService;
	}
}
