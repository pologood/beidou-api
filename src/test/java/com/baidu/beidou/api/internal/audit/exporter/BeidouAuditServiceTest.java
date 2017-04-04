package com.baidu.beidou.api.internal.audit.exporter;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

@Ignore
public class BeidouAuditServiceTest extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 7393996;

    @Override
    public int getShard() {
        return userId;
    }

    @Resource
    private BeidouAuditService beidouAuditService;

    @Test
    public void testGetProductViewList() {
        QueryProduct query = new QueryProduct();
        
        query.setUserId(userId);
        query.setPlanId(4137171);
        query.setGroupId(20577674);
        query.setUnitId(223175123L);
        query.setTemplateId(134);
        query.setType(9);
        query.setHeight(250);
        query.setWidth(250);
        
        AuditResult<ProductView> result = beidouAuditService.getProductViewList(userId, query);
        
        // 打印返回
        System.out.println(result);
    }
}
