package com.baidu.beidou.api.internal.audit.rpc;

import java.util.Map;

import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;

/**
 * Function: 与风控交互接口
 * 
 * @ClassName: RiskMgtAuditDriverProxy
 * @author genglei01
 * @date Mar 18, 2015 11:48:23 AM
 */
public interface RiskMgtAuditDriverProxy {
    /**
     * Function: 向风控部门推送审核操作记录
     * 
     * @author genglei01
     * @param auditLog    审核日志，推送给风控审核使用
     * @param params Rpc调用时需要传递的参数列表，通常是消息头
     * @return 记录结果
     */
    AuditResult<Object> pushAuditLog(AuditLogItem auditLog, Map<String, String> params);
}