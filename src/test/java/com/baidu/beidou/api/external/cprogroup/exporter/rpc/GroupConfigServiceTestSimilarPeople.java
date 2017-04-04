/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSimilarPeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSimilarPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateSimilarPeopleRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.google.common.collect.Lists;

/**
 * 相似人群RPC UT
 * 
 * @author Wang Yu
 * 
 */
public class GroupConfigServiceTestSimilarPeople extends ApiBaseRPCTest<GroupConfigService> {
    @Test
    @Ignore
    public void testGetSimilarPeople() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        GetSimilarPeopleRequest request = new GetSimilarPeopleRequest();
        request.setGroupIds(new int[] {});
        long start = System.currentTimeMillis();
        BaseResponse<GroupSimilarPeopleType> result = exporter.getSimilarPeople(dataUser2, request, apiOption2);
        System.out.println(result.getErrors().get(0).getCode());

        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        System.out.println(result);
    }

    @Test
    public void testUpdateSimilarPeople() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        UpdateSimilarPeopleRequest request = new UpdateSimilarPeopleRequest();
        request.setSimilarPeoples(Lists.newArrayList(new GroupSimilarPeopleType(12000610, 1),
                new GroupSimilarPeopleType(12000664, 1)));
        long start = System.currentTimeMillis();
        BaseResponse<PlaceHolderResult> result = exporter.updateSimilarPeople(dataUser2, request, null);
        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        System.out.println(result);
    }
}
