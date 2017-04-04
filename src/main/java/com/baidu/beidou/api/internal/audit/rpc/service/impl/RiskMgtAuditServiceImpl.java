package com.baidu.beidou.api.internal.audit.rpc.service.impl;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.rpc.RiskMgtAuditDriverProxy;
import com.baidu.beidou.api.internal.audit.rpc.service.RiskMgtAuditService;
import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;
import com.baidu.beidou.api.internal.audit.util.RiskMgtAuditUtil;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.util.service.impl.BaseDrawinRpcServiceImpl;

/**
 * Function: 风控审核记录接口
 * 
 * @ClassName: RiskMgtAuditServiceImpl
 * @author genglei01
 * @date Mar 25, 2015 10:54:57 AM
 */
public class RiskMgtAuditServiceImpl extends BaseDrawinRpcServiceImpl implements RiskMgtAuditService {

    private RiskMgtAuditDriverProxy riskMgtAuditDriverProxy;

    /**
     * 构造类
     * 
     * @param syscode   syscode
     * @param prodid    prodid
     */
    public RiskMgtAuditServiceImpl(String syscode, String prodid) {
        super(syscode, prodid);
    }

    /**
     * Function: 向风控部门推送审核操作记录
     * 
     * @author genglei01
     * @param auditLog    审核日志，推送给风控审核使用
     * @return 记录结果
     */
    public boolean pushAuditLog(AuditLogItem auditLog) {

        String operate = "RiskMgtAuditServiceImpl.pushAuditLog";
        RiskMgtAuditUtil.startTiming();
        try {
            AuditResult<Object> result = riskMgtAuditDriverProxy.pushAuditLog(auditLog, getHeaders());
            if (result == null || result.getStatus() != ResponseStatus.SUCCESS) {
                RiskMgtAuditUtil.endTiming(operate, false, result.getStatus(), auditLog.getUserId(), 
                        auditLog.getUnitId(), auditLog.getAuditTime(), auditLog.getAuditResult(), 
                        auditLog.getMcId(), auditLog.getOldVersionId(), auditLog.getNewVersionId());
                return false;
            }
        } catch (Exception e) {
            RiskMgtAuditUtil.endTiming(operate, false, -1, auditLog.getUserId(), 
                    auditLog.getUnitId(), auditLog.getAuditTime(), auditLog.getAuditResult(), 
                    auditLog.getMcId(), auditLog.getOldVersionId(), auditLog.getNewVersionId());
            return false;
        }
        
        RiskMgtAuditUtil.endTiming(operate, true, ResponseStatus.SUCCESS, auditLog.getUserId(), 
                auditLog.getUnitId(), auditLog.getAuditTime(), auditLog.getAuditResult(), 
                auditLog.getMcId(), auditLog.getOldVersionId(), auditLog.getNewVersionId());
        return true;
    }

    public RiskMgtAuditDriverProxy getRiskMgtAuditDriverProxy() {
        return riskMgtAuditDriverProxy;
    }

    public void setRiskMgtAuditDriverProxy(RiskMgtAuditDriverProxy riskMgtAuditDriverProxy) {
        this.riskMgtAuditDriverProxy = riskMgtAuditDriverProxy;
    }
}
