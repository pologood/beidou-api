/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.user.rpc;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.user.exporter.UserTagService;
import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.vo.UserTagInfo;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by hewei18 on 2016-01-22.
 */

public class UserTagServiceTest {

    private static final int TEST_USER_SIZE = 30;
    private static String API_URL = "http://127.0.0.1:8080/api/UserTagService";
    private UserTagService exporter;
    private List<UserTagInfo> tagInfoList;
    private Map<Integer, Integer> initTagMap;
    private Map<Integer, Integer> updateTagMap;
    private List<Integer> userIdList;

    @Before
    public void setUp() {
        McpackRpcProxy proxy = new McpackRpcProxy(API_URL,
                "UTF-8", new ExceptionHandler());

        exporter = ProxyFactory.createProxy(UserTagService.class, proxy);

        initTagMap = Maps.newHashMap();
        initTagMap.put(0, 1);
        initTagMap.put(1, 1);
        initTagMap.put(2, 4);

        initTagMap.put(21, 7);
        initTagMap.put(35, 3);

        initTagMap.put(61, 100); // invalid value
        initTagMap.put(62, 3);

        initTagMap.put(-1, 2); // invalid value
        initTagMap.put(10000, 3);  // invalid value
        initTagMap.put(60, 8);  // invalid value
        initTagMap.put(9, 8);  // invalid value
        initTagMap.put(20, -1);  // invalid value

        tagInfoList = Lists.newArrayList();

        for (int i = 1; i <= TEST_USER_SIZE; ++i) {
            tagInfoList.add(new UserTagInfo(i, initTagMap));
        }
        userIdList = Lists.newArrayListWithCapacity(TEST_USER_SIZE + 1);
        for (int i = 1; i <= TEST_USER_SIZE; ++i) {
            userIdList.add(i);
        }
    }

    public void clearAllTag() throws Exception {
        Map<Integer, Integer> clearMap = Maps.newHashMap();
        for (int i = -1; i < 100; ++i) {
            clearMap.put(i, 0);
        }
        List<UserTagInfo> clearTagList = Lists.newArrayList();
        for (int i = 1; i <= TEST_USER_SIZE; ++i) {
            clearTagList.add(new UserTagInfo(i, clearMap));
        }
        UserServResult<List<Integer>> result = exporter.batchUpdateUserTagInfo(clearTagList);
        Assert.assertEquals(UserServResult.OK, result.getErrCode());
        Assert.assertEquals(result.getData().size(), 0);
    }

    @Test
    @Ignore
    public void testAll() throws Exception {
        this.clearAllTag();
        this.testBatchInsertUserTagInfo();
        this.testBatchGetUserTagInfo();
        this.testBatchUpdateUserTagInfo();
        this.testBatchGetUpdatedTag();
    }

    //    @Test
    public void testBatchInsertUserTagInfo() throws Exception {
        UserServResult<List<Integer>> result = exporter.batchUpdateUserTagInfo(tagInfoList);
        Assert.assertEquals(UserServResult.OK, result.getErrCode());
        Assert.assertEquals(result.getData().size(), 0);
    }

    //    @Test
    public void testBatchGetUserTagInfo() throws Exception {
        UserServResult<List<UserTagInfo>> result = exporter.batchGetUserTagInfo(userIdList);

        Map<Integer, Integer> tagMap = this.tagInfoList.get(0).getUserTags();
        tagMap.remove(-1);
        tagMap.remove(10000);
        tagMap.remove(61);
        tagMap.remove(60);
        tagMap.remove(9);
        tagMap.remove(20);

        Assert.assertEquals(UserServResult.OK, result.getErrCode());
        Assert.assertArrayEquals(tagInfoList.toArray(), result.getData().toArray());
    }

    //    @Test
    public void testBatchUpdateUserTagInfo() throws Exception {
        updateTagMap = Maps.newHashMap();
        // add new tags
        updateTagMap.put(20, 3);
        updateTagMap.put(40, 4);
        updateTagMap.put(50, 5);
        updateTagMap.put(60, 6);

        // erase some tags
        updateTagMap.put(21, 0);
        updateTagMap.put(35, 0);
        updateTagMap.put(60, 0);

        // invalid tags
        updateTagMap.put(63, 0);
        updateTagMap.put(0, -1);

        List<UserTagInfo> updateTagList = Lists.newArrayList();

        for (int i = 1; i <= TEST_USER_SIZE; ++i) {
            updateTagList.add(new UserTagInfo(i, updateTagMap));
        }
        UserServResult<List<Integer>> result = exporter.batchUpdateUserTagInfo(updateTagList);
        Assert.assertEquals(UserServResult.OK, result.getErrCode());
        Assert.assertEquals(result.getData().size(), 0);
    }

    //    @Test
    public void testBatchGetUpdatedTag() {
        // remove invalid
        updateTagMap.remove(63);
        updateTagMap.remove(0);

        // merge init and udpate tag map
        initTagMap.putAll(updateTagMap);

        // remove erased
        initTagMap.remove(21);
        initTagMap.remove(35);
        initTagMap.remove(60);

        UserServResult<List<UserTagInfo>> result = exporter.batchGetUserTagInfo(userIdList);

        Assert.assertEquals(UserServResult.OK, result.getErrCode());

        // here tag in tagInfoList has been updated
        Assert.assertArrayEquals(tagInfoList.toArray(), result.getData().toArray());
    }

}