package com.baidu.beidou.api.internal.audit.rpc;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.rpc.service.RiskMgtAuditService;
import com.baidu.beidou.api.internal.audit.rpc.vo.AuditLogItem;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

/**
 * Function: 风控回写接口测试
 * 
 * @ClassName: RiskMgtAuditServiceTest
 * @author genglei01
 * @date Mar 19, 2015 6:46:16 PM
 */
public class RiskMgtAuditServiceTest extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 480786;

    @Override
    public int getShard() {
        return userId;
    }

    @Resource
    private RiskMgtAuditService riskMgtAuditService;
    
    @Test
    public void testPushAuditLog() throws Exception {
        AuditLogItem auditLog = new AuditLogItem();
        auditLog.setUnitId(11323212L);
        auditLog.setUserId(userId);
        auditLog.setAppId(AuditConstant.APP_ID_BEIDOU_SMART_UNIT);
        auditLog.setAuditorId(111);
        auditLog.setAuditPeriod(10L);
        auditLog.setAuditResult(0);
        auditLog.setModTime("2015-03-21 11:01:11");
        auditLog.setAuditTime("2015-03-21 11:11:11");
        auditLog.setTitle("title");
        auditLog.setDesc1("desc1");
        auditLog.setDesc2("desc2");
        auditLog.setMaterUrl(null);
        auditLog.setMcId(121321L);
        auditLog.setOldVersionId(1);
        auditLog.setNewVersionId(2);
        
        auditLog.setNewTradeid(520101);
        auditLog.setRefuseReasonIds(null);
        auditLog.setRefuseReasonStr(null);
        auditLog.setRemark("用户提交");
        auditLog.setShowUrl("baidu.com");
        auditLog.setTargetUrl("http://baidu.com");
        auditLog.setWuliaoTypeStr("文本");
        
        boolean flag = riskMgtAuditService.pushAuditLog(auditLog);
        Long unitId = 11323212L;
        String auditTime = "2015-03-21 11:11:11";
        Integer auditResult = 0;
        Integer[] refuseReasonIds = new Integer[]{0};
        String remark = "用户提交";
        Long mcId = 11111111L;
        Integer oldVersionId = 1;
        Integer newVersionId = 2;
//        AuditLogItem auditLog = new AuditLogItem(userId, unitId, auditTime, auditResult,
//                refuseReasonIds, remark, mcId, oldVersionId, newVersionId);
//        
//        boolean flag = riskMgtAuditService.pushAuditLog(auditLog);

//        System.out.println("status:" + flag);

    }

    public RiskMgtAuditService getRiskMgtAuditService() {
        return riskMgtAuditService;
    }

    public void setRiskMgtAuditService(RiskMgtAuditService riskMgtAuditService) {
        this.riskMgtAuditService = riskMgtAuditService;
    }

}
