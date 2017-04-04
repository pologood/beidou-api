package com.baidu.beidou.api.internal.audit.rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.UnitAuditService;
import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.UnitAuditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitReauditInfo;
import com.baidu.beidou.api.internal.audit.vo.request.AuditResultUnit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;

public class UnitAuditServiceImplTest {

    @Ignore
    @Test
    public void testGetUnitAuditList() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);

        int userId = 3;
        QueryUnitAudit request = new QueryUnitAudit();
        request.setQuery("");
        request.setQueryType(2);
        request.setPage(0);
        request.setPageSize(50);
        request.setUserId(userId);
        request.setPlanState(-1);
        request.setGroupState(-1);
        request.setUnitType(-1);
        request.setAccuracyType(-1);
        request.setBeautyType(-1);
        request.setVulgarType(-1);
        request.setCheatType(-1);
        List<Long> unitIds = new ArrayList<Long>();
        unitIds.add(7862997L);
        unitIds.add(7862996L);
        unitIds.add(38581658L);

        // unitIds.add(38581658l);
        request.setUnitIds(unitIds);

        AuditResult<UnitAuditInfo> result = exporter.getUnitAuditList(userId, request);
        System.out.println(result.getData().size());
        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
        }
    }

    @Ignore
    @Test
    public void testGetUnitReauditList() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.23.244.60:8080/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);

        int userId = 1865058;
        QueryUnitReaudit request = new QueryUnitReaudit();
        request.setQuery("382垂直论坛C");
        request.setQueryType(2);
        request.setPage(0);
        request.setPageSize(50);
        request.setUserId(userId);
        request.setPlanState(-1);
        request.setGroupState(-1);
        request.setUnitType(-1);
        request.setAccuracyType(-1);
        request.setBeautyType(-1);
        request.setVulgarType(-1);
        request.setCheatType(-1);

        AuditResult<UnitReauditInfo> result = exporter.getUnitReauditList(userId, request);

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result);
        }
    }

    @Ignore
    @Test
    public void testPass() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);

        List<AuditResultUnit> auditList = new ArrayList<AuditResultUnit>();
        Integer auditorId = 694275;

        AuditResultUnit auditUnit = new AuditResultUnit();
        auditUnit.setUnitId(84625936L);
        auditUnit.setUserId(8);
        auditUnit.setTradeId(990101);
        auditUnit.setTradeModified(1);
        auditUnit.setAccuracyType(1);
        auditUnit.setBeautyType(1);
        auditUnit.setVulgarType(2);
        auditUnit.setCheatType(1);

        auditList.add(auditUnit);

        auditUnit = new AuditResultUnit();
        auditUnit.setUnitId(84625944L);
        auditUnit.setUserId(8);
        auditUnit.setTradeId(990101);
        auditUnit.setTradeModified(1);
        auditUnit.setAccuracyType(1);
        auditUnit.setBeautyType(1);
        auditUnit.setVulgarType(2);
        auditUnit.setCheatType(1);

        auditList.add(auditUnit);

        AuditResult<AuditOpt> result = exporter.pass(auditList, auditorId);

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            for (AuditOpt opt : result.getData()) {
                if (opt != null) {
                    System.out.println(opt.getUnitId() + "\t" + opt.getAuditTime() + "\t" + opt.getMaterUrl());
                }
            }
        }
    }

    @Test
    @Ignore
    public void testRefuse() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);

        List<AuditResultUnit> auditList = new ArrayList<AuditResultUnit>();
        Integer auditorId = 694275;

        AuditResultUnit auditUnit = new AuditResultUnit();
        auditUnit.setUnitId(206596848L);
        auditUnit.setUserId(480787);
        auditUnit.setTradeId(990101);
        auditUnit.setTradeModified(1);
        auditUnit.setAccuracyType(1);
        auditUnit.setBeautyType(1);
        auditUnit.setVulgarType(2);
        auditUnit.setCheatType(1);

        auditList.add(auditUnit);

        auditUnit = new AuditResultUnit();
        auditUnit.setUnitId(206596856L);
        auditUnit.setUserId(480787);
        auditUnit.setTradeId(990101);
        auditUnit.setTradeModified(1);
        auditUnit.setAccuracyType(1);
        auditUnit.setBeautyType(1);
        auditUnit.setVulgarType(2);
        auditUnit.setCheatType(1);

        auditList.add(auditUnit);

        List<Integer> refuseReasonList = new ArrayList<Integer>();
        refuseReasonList.add(1);
        AuditResult<AuditOpt> result = exporter.refuse(auditList, refuseReasonList, auditorId);

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            for (AuditOpt opt : result.getData()) {
                if (opt != null) {
                    System.out.println(opt.getUnitId() + "\t" + opt.getAuditTime());
                }
            }
        }
    }

    @Ignore
    @Test
    public void testGetProductViewList() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);

        Integer userId = 8;
        QueryProduct query = new QueryProduct();
        query.setUserId(userId);
        query.setUnitId(105847128L);
        query.setGroupId(8272785);
        query.setPlanId(2380297);
        query.setHeight(200);
        query.setWidth(200);
        query.setTemplateId(3);

        AuditResult<ProductView> result = exporter.getProductViewList(userId, query);

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            for (ProductView product : result.getData()) {
                if (product != null) {
                    System.out.println(product.getUnitId() + "\t" + product.getHtmlSnippet());
                }
            }
        }
    }

    @Ignore
    @Test
    public void testRefuseProduct() throws Exception {
        // McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new
        // ExceptionHandler());
        // McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new
        // ExceptionHandler());
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/api/UnitAuditService", "UTF-8", new ExceptionHandler());

        UnitAuditService exporter = ProxyFactory.createProxy(UnitAuditService.class, proxy);
        Integer userId = 8;
        List<Long> productIds = new ArrayList<Long>();
        productIds.add(138L);
        productIds.add(140L);

        AuditResult<Object> result = exporter.refuseProduct(userId, productIds);

        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println("success");
        }
    }
}
