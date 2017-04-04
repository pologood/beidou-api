package com.baidu.beidou.api.internal.audit.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;
import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.exception.RefuseReasonFormatException;
import com.baidu.beidou.cprounit.service.RefuseReasonUtils;
import com.baidu.beidou.util.DateUtils;
import com.google.common.primitives.Ints;

public final class AuditOptBeanUtil {

	private static final Log LOG = LogFactory.getLog(AuditOptBeanUtil.class);

	public static AuditOpt createAuditOptByUnit(Unit unit, int auditorId, 
			String auditorName, boolean isRefused) {
		if (unit == null) {
			return null;
		}
		AuditOpt auditOpt = new AuditOpt();

		auditOpt.setUnitId(unit.getId());
		auditOpt.setGroupId(unit.getGroup().getGroupId());
		auditOpt.setPlanId(unit.getPlan().getPlanId());
		auditOpt.setUserId(unit.getUser().getUserid());
		auditOpt.setUserName(unit.getUser().getUsername());

		auditOpt.setKeyword(unit.getMaterial().getKeyword());
		auditOpt.setShowUrl(unit.getMaterial().getShowUrl());
		auditOpt.setTargetUrl(unit.getMaterial().getTargetUrl());
		auditOpt.setTitle(unit.getMaterial().getTitle());
		auditOpt.setWuliaoType(unit.getMaterial().getWuliaoType());
		auditOpt.setIftitle(unit.getMaterial().getIftitle());

		if (unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL
				|| unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			auditOpt.setDesc1(unit.getMaterial().getDescription1());
			auditOpt.setDesc2(unit.getMaterial().getDescription2());
		}
		if (unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
				|| unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH
				|| unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
				|| unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_SMART_IDEA){
			auditOpt.setHeight(unit.getMaterial().getHeight());
			auditOpt.setWidth(unit.getMaterial().getWidth());
			auditOpt.setSize(unit.getMaterial().getWidth() + "X" + unit.getMaterial().getHeight());
			auditOpt.setFileSrcMd5(unit.getMaterial().getFileSrcMd5());
		}
		auditOpt.setPlayer(unit.getMaterial().getPlayer());
		if (isRefused) {
			auditOpt.setRefuseReason(unit.getAuditing().getRefuseReason());
			auditOpt.setAuditResult(AuditConstant.AUDIT_RESULT_REFUSE);
			
			String reason = null;
			try {
				reason = RefuseReasonUtils
						.generateRefuseReason(unit.getAuditing().getRefuseReason(), false);
			} catch (RefuseReasonFormatException e) {
				LOG.error(e.getMessage(),e);
				return null;
			}
			auditOpt.setRefuseReasonStr(reason);
		} else {
			auditOpt.setAuditResult(AuditConstant.AUDIT_RESULT_PASS);
			auditOpt.setRefuseReasonStr("-");
		}
		
		auditOpt.setAuditorId(auditorId);
		auditOpt.setUbmcSyncFlag(CproUnitConstant.UBMC_SYNC_FLAG_YES);
		auditOpt.setMcId(0L);
		auditOpt.setMcVersionId(0);
		
		auditOpt.setWirelessShowUrl(unit.getMaterial().getWirelessShowUrl());
		auditOpt.setWirelessTargetUrl(unit.getMaterial().getWirelessTargetUrl());
		
		Integer wuliaoType = unit.getMaterial().getWuliaoType();
		switch (wuliaoType) {
		case CproUnitConstant.MATERIAL_TYPE_LITERAL:
			auditOpt.setWuliaoTypeStr("文字");
			break;
		case CproUnitConstant.MATERIAL_TYPE_PICTURE:
		case CproUnitConstant.MATERIAL_TYPE_FLASH:
			auditOpt.setWuliaoTypeStr("图片");
			break;
		case CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON:
			auditOpt.setWuliaoTypeStr("图文");
			break;
		case CproUnitConstant.MATERIAL_TYPE_SMART_IDEA:
			auditOpt.setWuliaoTypeStr("智能");
			break;
		default:
			auditOpt.setWuliaoTypeStr("-");
			break;
		}
		
		Date subTime = unit.getChaTime();
		Date auditTime = new Date();
		auditOpt.setSubTime(DateUtils.getDateStr(subTime));
		auditOpt.setAuditTime(DateUtils.getDateStr(auditTime));
		auditOpt.setAuditorName(auditorName);

		if (unit.getUser().getUserid().equals(unit.getChanger())) {
			auditOpt.setRemark("用户提交");
		} else {
			auditOpt.setRemark("代理提交");
		}
		
		auditOpt.setAuditPeriod(getBetweenDate(subTime, auditTime));
		
		auditOpt.setNewTradeid(unit.getMaterial().getNewAdtradeid());

		return auditOpt;
	}

    public static AuditLogItem createAuditLogItemByUnit(Unit unit, Integer auditResult, 
            List<Integer> refuseReasonIds, Integer auditorId) {
        if (unit == null) {
            return null;
        }
        UnitMater mater = unit.getMaterial();
        AuditLogItem auditLog = new AuditLogItem();

        auditLog.setUnitId(unit.getId());
        auditLog.setUserId(unit.getUser().getUserid());
        
        if (mater.getIsSmart().equals(CproUnitConstant.IS_SMART_FALSE)) {
            auditLog.setAppId(AuditConstant.APP_ID_BEIDOU_NORMAL_UNIT);
        } else {
            auditLog.setAppId(AuditConstant.APP_ID_BEIDOU_SMART_UNIT);
        }

        Integer wuliaoType = mater.getWuliaoType();
        switch (wuliaoType) {
            case CproUnitConstant.MATERIAL_TYPE_LITERAL:
                auditLog.setWuliaoTypeStr("文字");
                break;
            case CproUnitConstant.MATERIAL_TYPE_PICTURE:
            case CproUnitConstant.MATERIAL_TYPE_FLASH:
                auditLog.setWuliaoTypeStr("图片");
                break;
            case CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON:
                auditLog.setWuliaoTypeStr("图文");
                break;
            case CproUnitConstant.MATERIAL_TYPE_SMART_IDEA:
                auditLog.setWuliaoTypeStr("智能");
                break;
            default:
                auditLog.setWuliaoTypeStr("-");
                break;
        }
        
        auditLog.setTitle(mater.getTitle());
        auditLog.setShowUrl(mater.getShowUrl());
        auditLog.setTargetUrl(mater.getTargetUrl());
        if (mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL
                || mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
            auditLog.setDesc1(mater.getDescription1());
            auditLog.setDesc2(mater.getDescription2());
        }
        if (mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
                || mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH
                || mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
                || mater.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_SMART_IDEA){
            auditLog.setSize(mater.getWidth() + "X" + mater.getHeight());
        }
        
        if (auditResult.equals(AuditConstant.BEIDOU_AUDIT_REFUSE)) {
            int[] refuseIds = null;
            if (CollectionUtils.isNotEmpty(refuseReasonIds)) {
                refuseIds = Ints.toArray(refuseReasonIds);
            }
            
            auditLog.setRefuseReasonIds(refuseIds);
            String reason = null;
            try {
                reason = RefuseReasonUtils.generateRefuseReason(unit.getAuditing().getRefuseReason(), false);
            } catch (RefuseReasonFormatException e) {
                LOG.error(e.getMessage(),e);
                return null;
            }
            auditLog.setRefuseReasonStr(reason);
            auditLog.setAuditResult(AuditConstant.AUDIT_RESULT_REFUSE);
        } else {
            auditLog.setRefuseReasonStr("-");
            auditLog.setAuditResult(AuditConstant.AUDIT_RESULT_PASS);
        }
        
        Date subTime = unit.getChaTime();
        Date auditTime = new Date();
        auditLog.setModTime(DateUtils.getDateStr(subTime));
        auditLog.setAuditTime(DateUtils.getDateStr(auditTime));
        auditLog.setAuditorId(auditorId);

        if (unit.getUser().getUserid().equals(unit.getChanger())) {
            auditLog.setRemark("用户提交");
        } else {
            auditLog.setRemark("代理提交");
        }
        auditLog.setAuditPeriod(getBetweenDate(subTime, auditTime));
        auditLog.setNewTradeid(mater.getNewAdtradeid());
        
        auditLog.setMcId(mater.getMcId());
        auditLog.setOldVersionId(mater.getMcVersionId());

        return auditLog;
    }
	
	private static Long getBetweenDate(Date startDate, Date endDate) {
		Long startDateTime = startDate.getTime();
		Long endDateTime = endDate.getTime();
		Long minuteTime = 60 * 1000L;
		Long minutes = (endDateTime - startDateTime) / minuteTime;
		
		return minutes;
	}
}
