package com.baidu.beidou.api.internal.audit.rpc.service;

import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;

/**
 * Function: 与风控交互接口
 * 
 * @ClassName: RiskMgtAuditService
 * @author genglei01
 * @date Mar 18, 2015 11:53:15 AM
 */
public interface RiskMgtAuditService {
    /**
     * Function: 向风控部门推送审核操作记录
     * 
     * @author genglei01
     * @param auditLog    审核日志，推送给风控审核使用
     * @return 记录结果
     */
    boolean pushAuditLog(AuditLogItem auditLog);
}
