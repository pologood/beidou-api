/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import org.junit.Test;

import com.baidu.beidou.api.external.cprogroup.service.GroupConfigAddAndDeleteService;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSimilarPeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateSimilarPeopleRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.google.common.collect.Lists;

/**
 * Created by hewei18 on 2015-09-28.
 */
public class GroupRegionServiceTest extends ApiBaseRPCTest<GroupConfigAddAndDeleteService> {
    @Test
    public void testUpdateRegion() throws Exception {
        GroupConfigAddAndDeleteService exporter =
                getServiceProxy(GroupConfigAddAndDeleteService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        AddRegionRequest req = new AddRegionRequest();
        // req.setRegions(new GroupRegionType[]);
        UpdateSimilarPeopleRequest request = new UpdateSimilarPeopleRequest();
        request.setSimilarPeoples(Lists.newArrayList(new GroupSimilarPeopleType(12000610, 1),
                new GroupSimilarPeopleType(12000664, 1)));
        long start = System.currentTimeMillis();
        // BaseResponse<PlaceHolderResult> result = exporter.updateSimilarPeople(dataUser2, request, null);
        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        // System.out.println(result);
    }
}
