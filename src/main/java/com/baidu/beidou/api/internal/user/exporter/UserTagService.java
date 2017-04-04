/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.user.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.vo.UserTagInfo;

/**
 * 用户层级标签内部API服务接口
 * Created by hewei18 on 2016-01-18.
 */
public interface UserTagService {

    /**
     * 查询单个用户标签
     *
     * @param userId 用户id
     *
     * @return 用户标签信息
     */
    UserServResult<UserTagInfo> getUserTagInfo(int userId);

    /**
     * 批量查询用户层级标签
     *
     * @param userIds 用户id列表
     *
     * @return 标签列表
     */
    UserServResult<List<UserTagInfo>> batchGetUserTagInfo(List<Integer> userIds);

    /**
     * 批量修改用户层级标签
     *
     * @param userTagInfoList 用户层级标签列表
     *
     * @return 修改失败的id列表
     */
    UserServResult<List<Integer>> batchUpdateUserTagInfo(List<UserTagInfo> userTagInfoList);
}
