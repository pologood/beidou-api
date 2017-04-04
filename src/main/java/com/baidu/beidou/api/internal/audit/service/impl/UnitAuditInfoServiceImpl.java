package com.baidu.beidou.api.internal.audit.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.dao.UnitAuditDao;
import com.baidu.beidou.api.internal.audit.dao.UnitAuditDaoOnMultiAddb;
import com.baidu.beidou.api.internal.audit.service.UnitAuditInfoService;
import com.baidu.beidou.api.internal.audit.util.UnitTagUtil;
import com.baidu.beidou.api.internal.audit.vo.TemplateElementUrlVo;
import com.baidu.beidou.api.internal.audit.vo.UnitAuditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterView;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitReauditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitResponse;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cprounit.bo.SmartIdeaTemplateElementConfVo;
import com.baidu.beidou.cprounit.bo.TemplateElementInfo;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitAuditing;
import com.baidu.beidou.cprounit.bo.UnitMater;
import com.baidu.beidou.cprounit.bo.UnitPreMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.dao.UnitDao;
import com.baidu.beidou.cprounit.exception.RefuseReasonFormatException;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.RefuseReasonUtils;
import com.baidu.beidou.cprounit.service.SmartIdeaTemplateConfMgr;
import com.baidu.beidou.cprounit.service.UbmcService;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.util.ThreadContext;
import com.google.common.collect.Lists;

public class UnitAuditInfoServiceImpl implements UnitAuditInfoService {
	
	private static final Log LOG = LogFactory.getLog(UnitAuditInfoServiceImpl.class);
	
	private CproUnitMgr unitMgr;
	private UnitAuditDao unitAuditDao;
	private UnitDao unitDao;
	private UnitAuditDaoOnMultiAddb unitAuditDaoOnMultiAddb;
	private SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr;
    private UbmcService ubmcService = null;

	public long countAuditUnit(Integer userId, QueryUnitAudit query) {
		return unitAuditDao.countAuditUnit(userId, query);
	}
	
	public List<UnitAuditInfo> updateUnitStateAndGetUnitAuditList(Integer userId, QueryUnitAudit query) {
		List<Unit> unitList = unitAuditDao.findAuditUnit(userId, query);
		List<UnitAuditInfo> result = new ArrayList<UnitAuditInfo>();
		
		Map<Integer, List<TemplateElementInfo>> groupTemplateConfMap = new HashMap<Integer, List<TemplateElementInfo>>();
		
		for (Unit unit : unitList) {
			UnitPreMater preUnitMater = null;
			List<TemplateElementInfo> elementList = null;
			
			if (unit.getMaterial().getIsSmart() == CproUnitConstant.IS_SMART_TRUE) {
				// 智能创意无需设置上一次版本
			    // 智能创意需要获取其模板元素url
			    Integer groupId = unit.getGroup().getGroupId();
			    if (groupTemplateConfMap.containsKey(groupId)) {
			        // 如果曾经获取过并且数据非空，则使用数据
			        List<TemplateElementInfo> list = groupTemplateConfMap.get(groupId);
			        if (!CollectionUtils.isEmpty(list)) {
			            elementList = list;
			        }
			    } else {
			        // 如果从没有获取过，则查询db数据
			        List<TemplateElementInfo> list = null;
			        SmartIdeaTemplateElementConfVo groupConfVo = smartIdeaTemplateConfMgr.findByGroupId(groupId, userId);
			        if (groupConfVo == null || CollectionUtils.isEmpty(groupConfVo.getTemplateElementInfoList())) {
			            groupTemplateConfMap.put(groupId, Collections.EMPTY_LIST);
			        } else {
			            list = groupConfVo.getTemplateElementInfoList();
			            groupTemplateConfMap.put(groupId, list);
			        }
			        elementList = list;
			    }
			    
			} else {
			    // 非智能创意，需要获取最新一次创意版本
			    preUnitMater = unitAuditDao.findUnitPreMater(userId, unit.getId());
			}
			
			UnitAuditInfo info = this.getPropertyFromUnitAndPremater(unit, preUnitMater, elementList);
			if (info != null) {
				unitDao.markAuditingMaterial(unit.getId(), unit.getUser().getUserid());
				result.add(info);
			}
		}
		return result;
	}
	
	public long countReauditUnit(Integer userId, QueryUnitReaudit query) {
		return unitAuditDao.countReauditUnit(userId, query);
	}
	
	public List<UnitReauditInfo> getUnitReauditList(Integer userId, QueryUnitReaudit query) {
		List<Unit> unitList = unitAuditDao.findReauditUnit(userId, query);
		List<UnitReauditInfo> result = new ArrayList<UnitReauditInfo>();
		
		for (Unit unit : unitList) {
			UnitReauditInfo info = this.getPropertyFromUnit(unit);
			if (info != null) {
				result.add(info);
			}
		}
		return result;
	}

    /**
     * Function: 获取复审创意列表，专门为风控团队
     * 
     * @author genglei01
     * @param userId 用户名
     * @param query 请求
     * @return List<UnitResponse>
     */
    public List<UnitResponse> getReauditUnitList(Integer userId, QueryUnitReaudit query) {
        List<Unit> unitList = unitAuditDao.findReauditUnit(userId, query);
        List<UnitResponse> result = new ArrayList<UnitResponse>();
        for (Unit unit : unitList) {
            UnitResponse info = this.getUnitResponseFromUnit(unit);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }
    

	public List<UnitReauditInfo> findReauditUnitByUnitIds(String unitIds) {
		List<Unit> unitList = unitAuditDaoOnMultiAddb.findReauditUnitByUnitIds(unitIds);
		List<UnitReauditInfo> result = new ArrayList<UnitReauditInfo>();
		
		for (Unit unit : unitList) {
			UnitReauditInfo info = this.getPropertyFromUnit(unit);
			if (info != null) {
				result.add(info);
			}
		}
		return result;
	}

    /**
     * Function: 通过unitIds获取复审创意列表
     * 
     * @author genglei01
     * @param unitIds 创意ID列表，以逗号分隔连接
     * @return List<UnitResponse>
     */
    public List<UnitResponse> getReauditUnitListByUnitIds(String unitIds) {
        List<Unit> unitList = unitAuditDaoOnMultiAddb.findReauditUnitByUnitIds(unitIds);
        List<UnitResponse> result = new ArrayList<UnitResponse>();

        for (Unit unit : unitList) {
            UnitResponse info = this.getUnitResponseFromUnit(unit);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }

	private UnitAuditInfo getPropertyFromUnitAndPremater(final Unit unit, 
	        UnitPreMater preUnitMater, List<TemplateElementInfo> elementList) {
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		UnitAuditInfo info = new UnitAuditInfo();
		info.setUnitId(unit.getId());
		info.setUserId(unit.getUser().getUserid());
		info.setPlanId(unit.getPlan().getPlanId());
		info.setPlanName(unit.getPlan().getPlanName());
		info.setGroupId(unit.getGroup().getGroupId());
		info.setGroupName(unit.getGroup().getGroupName());
		
		// 设置Unit
		info.setPromotionType(unit.getPlan().getPromotionType());
		
		UnitMaterView unitView = new UnitMaterView();
		UnitMater mater = unit.getMaterial();
		info.setUnit(unitView);
		
		unitView.setType(mater.getWuliaoType());
		unitView.setTitle(mater.getTitle());
		
		if (CproPlanConstant.PROMOTIONTYPE_ALL== info.getPromotionType()) {
			unitView.setTargetUrl(mater.getTargetUrl());
			unitView.setShowUrl(mater.getShowUrl());
		}
		unitView.setWirelessTargetUrl(mater.getWirelessTargetUrl());
		unitView.setWirelessShowUrl(mater.getWirelessShowUrl());
		
		Integer wuliaoType = unit.getMaterial().getWuliaoType();
		if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL 
				|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			unitView.setDesc1(mater.getDescription1());
			unitView.setDesc2(mater.getDescription2());
			
			if (unit.getMaterial().getIsSmart() == CproUnitConstant.IS_SMART_TRUE) {
				unitView.setHeight(CproUnitConstant.LITERAL_SMART_PRVIEW_HEIGHT);
				unitView.setWidth(CproUnitConstant.LITERAL_SMART_PRVIEW_WIDTH);
			}
		}
		if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_FLASH
				|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_PICTURE
				|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
            unitView.setHeight(mater.getHeight());
            unitView.setWidth(mater.getWidth());
			
			// 从ubmc获取临时url，反馈给前端
			Long mcId = mater.getMcId();
			Integer versionId = mater.getMcVersionId();
			String url = unitMgr.getTmpUrl(mcId, versionId);
			unitView.setMaterUrl(url);
            unitView.setFileSrcMd5(mater.getFileSrcMd5());
		}
		// @cpweb699，设置智能创意相关信息
		if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_SMART_IDEA) {
            unitView.setHeight(mater.getHeight());
            unitView.setWidth(mater.getWidth());
		}
		
		unitView.setIsSmart(mater.getIsSmart());
		unitView.setTemplateId(mater.getTemplateId());
		unitView.setIsAudit(true);	// 初审设置该字段为true
		
		// 如果是智能创意，考虑设置元素url
		if (mater.getIsSmart() == CproUnitConstant.IS_SMART_TRUE) {
		    if (!CollectionUtils.isEmpty(elementList)) {
		        List<TemplateElementUrlVo> list = new ArrayList<TemplateElementUrlVo>();
		        for (TemplateElementInfo item : elementList) {
		            list.add(new TemplateElementUrlVo(item.getLiteral(), 
		                    item.getTargetUrl(), item.getWirelessTargetUrl()));
		        }
		        TemplateElementUrlVo[] elementVoArray = list.toArray(new TemplateElementUrlVo[0]);
		        unitView.setElementUrlList(elementVoArray);
		    }
		}
		
		// @version cpweb-535, @author genglei01
		// 获取preUnitMater，已解决lazy延迟加载问题
		if (preUnitMater != null) {
			UnitMaterView preMater = new UnitMaterView();
			try {
				BeanUtils.copyProperties(preMater, preUnitMater);
				if (CproPlanConstant.PROMOTIONTYPE_ALL != info.getPromotionType()) {
					preMater.setTargetUrl(null);
					preMater.setShowUrl(null);
				}
				
				preMater.setType(preUnitMater.getWuliaoType());
				preMater.setTitle(preUnitMater.getTitle());
				
				if (CproPlanConstant.PROMOTIONTYPE_ALL== info.getPromotionType()) {
					preMater.setTargetUrl(preUnitMater.getTargetUrl());
					preMater.setShowUrl(preUnitMater.getShowUrl());
				}
				preMater.setWirelessTargetUrl(preUnitMater.getWirelessTargetUrl());
				preMater.setWirelessShowUrl(preUnitMater.getWirelessShowUrl());
				
				Integer preWuliaoType = preUnitMater.getWuliaoType();
				if (preWuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL 
						|| preWuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
					preMater.setDesc1(preUnitMater.getDescription1());
					preMater.setDesc2(preUnitMater.getDescription2());
				}
				if (preWuliaoType == CproUnitConstant.MATERIAL_TYPE_FLASH
						|| preWuliaoType == CproUnitConstant.MATERIAL_TYPE_PICTURE
						|| preWuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
					preMater.setHeight(preUnitMater.getHeight());
					preMater.setWidth(preUnitMater.getWidth());
					
					// 从ubmc获取临时url，反馈给前端
					Long mcId = preUnitMater.getMcId();
					Integer versionId = preUnitMater.getMcVersionId();
					String url = unitMgr.getTmpUrl(mcId, versionId);
					preMater.setMaterUrl(url);
				}
				
				String refuseReasons = preUnitMater.getRefuseReason();
				if (!StringUtils.isEmpty(refuseReasons)) {
					String refuseReasonStr = RefuseReasonUtils.generateRefuseReason(refuseReasons, false);
					info.setPreRefuseReason(refuseReasonStr);
				}
				info.setPreUnit(preMater);
			} catch (IllegalAccessException e) {
				LogUtils.fatal(LOG, e.getMessage(), e);
			} catch (InvocationTargetException e) {
				LogUtils.fatal(LOG, e.getMessage(), e);
			} catch (RefuseReasonFormatException e) {
				LogUtils.fatal(LOG, e.getMessage(), e);
			}
		}
		
		String subTimeStr = DateUtils.getDateStr(unit.getChaTime());
		info.setSubTime(subTimeStr);
		
		info.setTradeId(mater.getNewAdtradeid());
		info.setAccuracyType(mater.getConfidenceLevel());
		info.setBeautyType(mater.getBeautyLevel());
		info.setVulgarType(mater.getVulgarLevel());
		info.setCheatType(mater.getCheatLevel());
		info.setDangerType(UnitTagUtil.getTagValueByType(mater.getTagMask(), QueryConstant.QueryModifyTag.dangerLevel));
		
		return info;
	}
	
	private UnitReauditInfo getPropertyFromUnit(final Unit unit) {
		if (unit == null || unit.getMaterial() == null) {
			return null;
		}
		try {
			UnitReauditInfo info = new UnitReauditInfo();
			info.setUnitId(unit.getId());
			info.setUserId(unit.getUser().getUserid());
			info.setPlanId(unit.getPlan().getPlanId());
			info.setPlanName(unit.getPlan().getPlanName());
			info.setGroupId(unit.getGroup().getGroupId());
			info.setGroupName(unit.getGroup().getGroupName());
			info.setUserName(unit.getUser().getUsername());
			
			// 设置Unit
			info.setPromotionType(unit.getPlan().getPromotionType());
			
			UnitMaterView unitView = new UnitMaterView();
			UnitMater mater = unit.getMaterial();
			info.setUnit(unitView);
			
			unitView.setType(mater.getWuliaoType());
			unitView.setTitle(mater.getTitle());
			
			if (CproPlanConstant.PROMOTIONTYPE_ALL== info.getPromotionType()) {
				unitView.setTargetUrl(mater.getTargetUrl());
				unitView.setShowUrl(mater.getShowUrl());
			}
			unitView.setWirelessTargetUrl(mater.getWirelessTargetUrl());
			unitView.setWirelessShowUrl(mater.getWirelessShowUrl());
			
			Integer wuliaoType = unit.getMaterial().getWuliaoType();
			if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL 
					|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
				unitView.setDesc1(mater.getDescription1());
				unitView.setDesc2(mater.getDescription2());
			}
			if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_FLASH
					|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_PICTURE
					|| wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
				unitView.setHeight(unit.getMaterial().getHeight());
				unitView.setWidth(unit.getMaterial().getWidth());
				
				// 从ubmc获取临时url，反馈给前端
				Long mcId = mater.getMcId();
				Integer versionId = mater.getMcVersionId();
				String url = unitMgr.getTmpUrl(mcId, versionId);
				unitView.setMaterUrl(url);
			}
			
			// @cpweb699，设置智能创意相关信息
			if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_SMART_IDEA) {
				unitView.setHeight(unit.getMaterial().getHeight());
				unitView.setWidth(unit.getMaterial().getWidth());
			}
			unitView.setIsSmart(mater.getIsSmart());
			unitView.setTemplateId(mater.getTemplateId());
			unitView.setIsAudit(false);	// 复审或者审核历史设置该字段为false
			
			if (unit.getState() == CproUnitConstant.UNIT_STATE_REFUSE) {
				UnitAuditing auditing = unit.getAuditing();
				if (auditing != null) {
					List<Integer> refuseReasonIds = RefuseReasonUtils.generateRefuseReasonIds(auditing.getRefuseReason());
					info.setRefuseReasonIds(refuseReasonIds.toArray(new Integer[0]));
				}
				info.setAuditResult(AuditConstant.AUDIT_RESULT_REFUSE);
			} else if (unit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
				info.setAuditResult(AuditConstant.AUDIT_RESULT_PASS);
			} else if (unit.getState() == CproUnitConstant.UNIT_STATE_PAUSE) {
				info.setAuditResult(AuditConstant.AUDIT_RESULT_PAUSE);
			} else {
				info.setAuditResult(AuditConstant.AUDIT_RESULT_ALL);
			}
			
			String subTimeStr = DateUtils.getDateStr(unit.getChaTime());
			info.setSubTime(subTimeStr);
			
			Date auditTime = unit.getAuditTime();
			if (auditTime != null) {
				String auditTimeStr = DateUtils.getDateStr(auditTime);
				info.setAuditTime(auditTimeStr);
			}
			
			info.setTradeId(mater.getNewAdtradeid());
			info.setAccuracyType(mater.getConfidenceLevel());
			info.setBeautyType(mater.getBeautyLevel());
			info.setVulgarType(mater.getVulgarLevel());
			info.setCheatType(mater.getCheatLevel());
			info.setDangerType(UnitTagUtil.getTagValueByType(mater.getTagMask(), QueryConstant.QueryModifyTag.dangerLevel));
			
			return info;
		} catch (Exception e) {
			LOG.error("get unit reaudit info failed for unitid=" + unit.getId(), e);
			return null;
		}
	}

    private UnitResponse getUnitResponseFromUnit(final Unit unit) {
        if (unit == null || unit.getMaterial() == null) {
            return null;
        }
        try {
            UnitResponse info = new UnitResponse();
            info.setUnitId(unit.getId());
            info.setUserId(unit.getUser().getUserid());
            info.setPlanId(unit.getPlan().getPlanId());
            info.setPlanName(unit.getPlan().getPlanName());
            info.setGroupId(unit.getGroup().getGroupId());
            info.setGroupName(unit.getGroup().getGroupName());
            info.setUserName(unit.getUser().getUsername());
            
            // 设置Unit
            info.setPromotionType(unit.getPlan().getPromotionType());
            
            UnitMater mater = unit.getMaterial();
            
            info.setType(mater.getWuliaoType());
            info.setTitle(mater.getTitle());
            
            if (CproPlanConstant.PROMOTIONTYPE_ALL == info.getPromotionType()) {
                info.setTargetUrl(mater.getTargetUrl());
                info.setShowUrl(mater.getShowUrl());
            }
            info.setWirelessTargetUrl(mater.getWirelessTargetUrl());
            info.setWirelessShowUrl(mater.getWirelessShowUrl());
            
            Integer wuliaoType = unit.getMaterial().getWuliaoType();
            if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL 
                    || wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
                info.setDesc1(mater.getDescription1());
                info.setDesc2(mater.getDescription2());
            }
            if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_FLASH
                    || wuliaoType == CproUnitConstant.MATERIAL_TYPE_PICTURE
                    || wuliaoType == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
                info.setHeight(unit.getMaterial().getHeight());
                info.setWidth(unit.getMaterial().getWidth());
                
                // 从ubmc获取临时url，反馈给前端
                Long mcId = mater.getMcId();
                Integer versionId = mater.getMcVersionId();
                // 设置空的userId，防止报错
                ThreadContext.putUserId(info.getUserId());
                String url = unitMgr.getTmpUrl(mcId, versionId);
                info.setMaterUrl(url);
            }
            
            // 设置智能创意相关信息
            if (wuliaoType == CproUnitConstant.MATERIAL_TYPE_SMART_IDEA) {
                info.setHeight(unit.getMaterial().getHeight());
                info.setWidth(unit.getMaterial().getWidth());
            }
            info.setIsSmart(mater.getIsSmart());
            info.setTemplateId(mater.getTemplateId());
            
            if (unit.getState() == CproUnitConstant.UNIT_STATE_REFUSE) {
                UnitAuditing auditing = unit.getAuditing();
                if (auditing != null) {
                    List<Integer> refuseReasonIds = RefuseReasonUtils
                            .generateRefuseReasonIds(auditing.getRefuseReason());
                    
                    if (CollectionUtils.isEmpty(refuseReasonIds)) {
                        info.setRefuseReasonIds(new Integer[0]);
                    } else {
                        info.setRefuseReasonIds(refuseReasonIds.toArray(new Integer[0]));
                    }
                }
                info.setAuditResult(AuditConstant.AUDIT_RESULT_REFUSE);
            } else if (unit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
                info.setAuditResult(AuditConstant.AUDIT_RESULT_PASS);
            } else if (unit.getState() == CproUnitConstant.UNIT_STATE_PAUSE) {
                info.setAuditResult(AuditConstant.AUDIT_RESULT_PAUSE);
            } else {
                info.setAuditResult(AuditConstant.AUDIT_RESULT_ALL);
            }
            
            String subTimeStr = DateUtils.getDateStr(unit.getChaTime());
            info.setModTime(subTimeStr);
            
            Date auditTime = unit.getAuditTime();
            if (auditTime != null) {
                String auditTimeStr = DateUtils.getDateStr(auditTime);
                info.setAuditTime(auditTimeStr);
            }
            
            info.setTradeId(mater.getNewAdtradeid());
            info.setUnitTag(mater.getTagMask());
            
            info.setMcId(mater.getMcId());
            info.setVersionId(mater.getMcVersionId());
            
            return info;
        } catch (Exception e) {
            LOG.error("get unit reaudit info failed for unitid=" + unit.getId(), e);
            return null;
        }
    }

	public List<Long> findAuditingUnitIds(Integer userId, List<Long> unitIds) {
		List<Long> result = new ArrayList<Long>();
		try {
			result = unitDao.findAuditingUnitIds(userId, unitIds);
		} catch (Exception e) {
			LOG.error("get auditing unitids failed for userId=" + userId
					+ ", unitIds=" + unitIds, e);
			return result;
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
    public UnitMaterialInfo getUnitMaterailInfo(Integer userId, Long unitId) {
        UnitMaterialInfo info = unitAuditDao.findUnitMaterialInfo(userId, unitId);
        if (info == null) {
            return info;
        }
        
        if (info.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
                || info.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH) {
            byte[] result = ubmcService.getMediaData(info.getMcId(), info.getVersionId());
            info.setData(result);
        }
        
        return info;
    }
    
    /**
     * Function: 获取智能创意子链信息
     * 
     * @author genglei01
     * @param userId    userId
     * @param groupId    groupId
     * @return List<TemplateElementUrlVo>
     */
    public List<TemplateElementUrlVo> getElementUrlList(int userId, int groupId) {
        SmartIdeaTemplateElementConfVo groupConfVo = smartIdeaTemplateConfMgr.findByGroupId(groupId, userId);
        if (groupConfVo == null || CollectionUtils.isEmpty(groupConfVo.getTemplateElementInfoList())) {
            return null;
        }
        
        List<TemplateElementUrlVo> result = Lists.newArrayList();
        List<TemplateElementInfo> elementList = groupConfVo.getTemplateElementInfoList();
        for (TemplateElementInfo item : elementList) {
            result.add(new TemplateElementUrlVo(item.getLiteral(), 
                    item.getTargetUrl(), item.getWirelessTargetUrl()));
        }
        
        return result;
    }

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public UnitAuditDao getUnitAuditDao() {
		return unitAuditDao;
	}

	public void setUnitAuditDao(UnitAuditDao unitAuditDao) {
		this.unitAuditDao = unitAuditDao;
	}

	public UnitDao getUnitDao() {
		return unitDao;
	}

	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = unitDao;
	}

	public UnitAuditDaoOnMultiAddb getUnitAuditDaoOnMultiAddb() {
		return unitAuditDaoOnMultiAddb;
	}

	public void setUnitAuditDaoOnMultiAddb(
			UnitAuditDaoOnMultiAddb unitAuditDaoOnMultiAddb) {
		this.unitAuditDaoOnMultiAddb = unitAuditDaoOnMultiAddb;
	}

    public SmartIdeaTemplateConfMgr getSmartIdeaTemplateConfMgr() {
        return smartIdeaTemplateConfMgr;
    }

    public void setSmartIdeaTemplateConfMgr(SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr) {
        this.smartIdeaTemplateConfMgr = smartIdeaTemplateConfMgr;
    }

    public UbmcService getUbmcService() {
        return ubmcService;
    }

    public void setUbmcService(UbmcService ubmcService) {
        this.ubmcService = ubmcService;
    }
}
