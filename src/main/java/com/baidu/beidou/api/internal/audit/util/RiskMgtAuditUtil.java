package com.baidu.beidou.api.internal.audit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Function: 风控日志记录
 * 
 * @ClassName: RiskMgtAuditUtil
 * @author genglei01
 * @date Mar 18, 2015 11:34:20 AM
 */
public class RiskMgtAuditUtil {
    private final static Log log = LogFactory.getLog(RiskMgtAuditUtil.class);
    
    private final static ThreadLocal<Long> logStart = new ThreadLocal<Long>();
    
    public static void startTiming(){
        long start = System.currentTimeMillis();
        logStart.set(start);
    }
    
    /**
     * Function: 记录日志
     * 
     * @author genglei01
     * @param operate   操作方法
     * @param isSuccess    操作是否成功
     * @param statusCode    返回结果码
     * @param userId    用户ID
     * @param unitId    创意ID
     * @param auditTime 审核时间，格式：yyyy-MM-dd HH:mm:ss
     * @param auditResult   操作类型，0-审核通过，1-审核拒绝
     * @param mcId  UBMC物料ID
     * @param oldVersionId  UBMC物料ID指定的旧版本ID
     * @param newVersionId  UBMC物料ID指定的新版本ID
     */
    public static void endTiming(String operate, boolean isSuccess, Integer statusCode, Integer userId, Long unitId, 
            String auditTime, Integer auditResult, Long mcId, Integer oldVersionId, Integer newVersionId){
        long end = System.currentTimeMillis();
        long time = end - logStart.get();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Timing-");
        sb.append(operate);
        if (isSuccess) {
            sb.append("(").append(isSuccess).append(")");
        }
        sb.append(", statusCode=");
        sb.append(statusCode);
        sb.append(", userId=");
        sb.append(userId);
        sb.append(", unitId=");
        sb.append(unitId);
        sb.append(", auditTime=");
        sb.append(auditTime);
        sb.append(", auditResult=");
        sb.append(auditResult);
        sb.append(", mcId=");
        sb.append(mcId);
        sb.append(", oldVersionId=");
        sb.append(oldVersionId);
        sb.append(", newVersionId=");
        sb.append(newVersionId);
        sb.append(", ms=");
        sb.append(time);
        
        log.info(sb.toString());
    }
}
