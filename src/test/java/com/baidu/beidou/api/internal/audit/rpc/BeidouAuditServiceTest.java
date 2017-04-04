package com.baidu.beidou.api.internal.audit.rpc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.BeidouAuditService;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.MaterialUrlRequest;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.TemplateElementUrlVo;
import com.baidu.beidou.api.internal.audit.vo.UnitResponse;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cprounit.vo.MaterialElementUrl;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.google.common.collect.Lists;

@Ignore
public class BeidouAuditServiceTest {

    @Test
    public void testAuditResult() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/BeidouAuditService", 
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);

        int userId = 19;
        long unitId = 3082279L;
        long mcId = 298718735L;
        int versionId = 1;
        int newTradeId = 480787;
        boolean tradeModified = false;
        int unitTag = 1;
        boolean unitTagModified = false;
        int auditType = 1;
        int auditResult = 4;
        boolean auditResultModified = true;
        List<Integer> refuseReasonIds = new ArrayList<Integer>();
        refuseReasonIds.add(145);
        refuseReasonIds.add(234);
        int auditorId = 673992;
        int dataStreamType = 0;

        AuditResult<Object> result =
                exporter.auditResult(userId, unitId, mcId, versionId, newTradeId, tradeModified, unitTag,
                        unitTagModified, auditType, auditResult, auditResultModified, refuseReasonIds, auditorId,
                        dataStreamType);

        assertThat(result.getStatus(), is(ResponseStatus.SUCCESS));
        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
        }
    }

//    @Ignore
    @Test
    public void testGetReauditUnitList() {
//        McpackRpcProxy proxy =
//                new McpackRpcProxy("http://10.26.7.129:8080/api/BeidouAuditService", 
//                        "UTF-8", new ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.65.211.24:8080/api/BeidouAuditService", 
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);

        int userId = 10081948;
        int planState = -1;
        int groupState = -1;
        int unitType = 10;
        int accuracyType = -1;
        int beautyType = -1;
        int vulgarType = -1;
        int cheatType = -1;
        int unitState = -1;
        int refuseReasonId = 0;

        QueryUnitReaudit queryUnit = new QueryUnitReaudit();

        queryUnit.setAccuracyType(accuracyType);
        queryUnit.setBeautyType(beautyType);
        queryUnit.setCheatType(cheatType);
        queryUnit.setGroupState(groupState);
        queryUnit.setPlanState(planState);
        queryUnit.setRefuseReasonId(refuseReasonId);
        queryUnit.setUnitType(unitType);
        queryUnit.setVulgarType(vulgarType);
        queryUnit.setUnitState(unitState);
        queryUnit.setPageSize(50);
        queryUnit.setPage(0);
        queryUnit.setQueryType(0);
        queryUnit.setQuery("");

        AuditResult<UnitResponse> result = exporter.getReauditUnitList(userId, queryUnit);

        assertThat(result.getStatus(), is(ResponseStatus.SUCCESS));

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
            List<UnitResponse> unitResponses = result.getData();
            assertThat(unitResponses, notNullValue());

            if (unitResponses != null) {
                for (UnitResponse response : unitResponses) {
                    System.out.println(response);
                }
            }
        }

    }
    
    @Test
    public void testGetReauditUnitListByUnitIds() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.26.7.129:8080/api/BeidouAuditService", 
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);

        AuditResult<UnitResponse> result = exporter.getReauditUnitListByUnitIds("222012362");

        assertThat(result.getStatus(), is(ResponseStatus.SUCCESS));

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
            List<UnitResponse> unitResponses = result.getData();
            assertThat(unitResponses, notNullValue());

            if (unitResponses != null) {
                for (UnitResponse response : unitResponses) {
                    System.out.println(response);
                }
            }
        }

    }
    
//    url=http://10.65.211.24:8080/api/&file=BeidouAuditService&method=getProductViewList
//        &params=(0=7393996&1=(userId=7393996&unitId=228193475&groupId=20577674&planId=4137171&templateId=134&width=728&height=90&type=10))
//        &result=(status=0&data=&totalPage=1&totalNum=0)

    @Test
    public void testGetProductViewList() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.26.7.129:8080/api/BeidouAuditService", 
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);
        int userId = 7393996;
        QueryProduct query = new QueryProduct();
        query.setUserId(userId);
        query.setUnitId(228193475L);
        query.setGroupId(20577674);
        query.setPlanId(4137171);
        query.setTemplateId(134);
        query.setWidth(728);
        query.setHeight(90);
        query.setType(9);
        

        AuditResult<ProductView> result = exporter.getProductViewList(userId, query);

        assertThat(result.getStatus(), is(ResponseStatus.SUCCESS));

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
            List<ProductView> unitResponses = result.getData();
            assertThat(unitResponses, notNullValue());

            if (unitResponses != null) {
                for (ProductView response : unitResponses) {
                    System.out.println(response);
                }
            }
        }
    }
    
    @Test
    public void testGetElementUrlList() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://tc-beidou-api00.tc.baidu.com:8080/api/BeidouAuditService",
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);

        int userId = 19;
        int groupId = 14679246;
        long unitId = 157101696L;

        AuditResult<TemplateElementUrlVo> result = exporter.getElementUrlList(userId, groupId, unitId);

        assertThat(result.getStatus(), is(ResponseStatus.SUCCESS));
        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
        }
    }

    @Test
    public void testGetFlashAndElementUrl() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://tc-beidou-api00.tc.baidu.com:8080/api/BeidouAuditService",
                        "UTF-8", new ExceptionHandler());

        BeidouAuditService exporter = ProxyFactory.createProxy(BeidouAuditService.class, proxy);


        AuditResult<MaterialElementUrl>
                result = exporter.getFlashAndElementUrl(Lists.newArrayList(new MaterialUrlRequest(2142830456L, 1)));
        Arrays.toString(result.getData().toArray());
    }
}
