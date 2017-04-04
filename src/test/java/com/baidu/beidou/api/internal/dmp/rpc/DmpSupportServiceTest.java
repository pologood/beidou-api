package com.baidu.beidou.api.internal.dmp.rpc;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.dmp.exporter.DmpSupportService;
import com.baidu.beidou.api.internal.dmp.vo.DmpPlanResult;
import com.baidu.beidou.api.internal.dmp.vo.VtCodeResult;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;

/**
 * dmp 服务单测
 * 
 * @author work
 * 
 */
public class DmpSupportServiceTest {

    @Test
    @Ignore
    public void testGetPlanList() throws Exception {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/DmpSupportService", "UTF-8", new ExceptionHandler());

        DmpSupportService exporter = ProxyFactory.createProxy(DmpSupportService.class, proxy);

        Integer userId = 8;

        DmpPlanResult result = exporter.getPlanList(userId);

        System.out.println("status:" + result.getStatus());
        System.out.println("errMsg:" + result.getErrorMsg());
        System.out.println("size:" + result.getData().size());

    }

    @Test
    @Ignore
    public void testGetUserDomain() throws Exception {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.94.168.119:8080/api/DmpSupportService", "UTF-8", new ExceptionHandler());

        DmpSupportService exporter = ProxyFactory.createProxy(DmpSupportService.class, proxy);
        List<String> domainList = exporter.getUserDomain(499);
        for (String domain : domainList) {
            System.out.println(domain);
        }
    }

    @Test
    @Ignore
    public void testGetVtCodeList() throws Exception {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://10.94.168.119:8080/api/DmpSupportService", "UTF-8", new ExceptionHandler());

        DmpSupportService exporter = ProxyFactory.createProxy(DmpSupportService.class, proxy);
        List<VtCodeResult> vtCodeList = exporter.getVtCodeList(499, 1);
        for (VtCodeResult vtCode : vtCodeList) {
            System.out.println(vtCode.getName() + "," + vtCode.getIsAllSite());
        }
    }
}
