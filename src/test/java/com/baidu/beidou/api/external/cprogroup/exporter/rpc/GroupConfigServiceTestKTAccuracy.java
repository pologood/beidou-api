package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKTAccuracyType;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKTAccuracyRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateKTAccuracyRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 优质流量RPC UT
 *
 * @author huangjinkun.
 * @date 16/2/20
 * @time 上午11:40
 */
public class GroupConfigServiceTestKTAccuracy extends ApiBaseRPCTest<GroupConfigService> {

    @Test
//    @Ignore
    public void testGetKTAccuracy() throws Exception {
        GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant
                .GROUPCONFIG_SERVICE_URL);
        GetKTAccuracyRequest request = new GetKTAccuracyRequest();
        request.setGroupIds(new int[]{12000610});
        long start = System.currentTimeMillis();
        BaseResponse<GroupKTAccuracyType> result = exporter.getKTAccuracy(dataUser2, request, apiOption2);
        System.out.println(result.getErrors().get(0).getCode());

        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        System.out.println(result);
    }

    @Test
    @Ignore
    public void testUpdateKTAccuracy() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        UpdateKTAccuracyRequest request = new UpdateKTAccuracyRequest();
        request.setKtAccuracies(Lists.newArrayList(new GroupKTAccuracyType(12000610, 1, 120),
            new GroupKTAccuracyType(12000664, 0, 100)));
        long start = System.currentTimeMillis();
        BaseResponse<PlaceHolderResult> result = exporter.updateKTAccuracy(dataUser2, request, apiOption2);
        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        System.out.println(result);
    }
}
