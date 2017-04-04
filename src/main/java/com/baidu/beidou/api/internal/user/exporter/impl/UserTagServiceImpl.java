/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.user.exporter.impl;

import java.util.List;

import com.baidu.beidou.api.internal.user.constant.UserConstant;
import com.baidu.beidou.api.internal.user.error.UserErrorCode;
import com.baidu.beidou.api.internal.user.exporter.UserTagService;
import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.service.UserTagMgr;
import com.baidu.beidou.user.vo.UserTagInfo;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * Created by hewei18 on 2016-01-21.
 */

public class UserTagServiceImpl implements UserTagService {

    private UserTagMgr userTagMgr;

    /**
     * @see UserTagService#getUserTagInfo(int)
     */
    @Override
    public UserServResult<UserTagInfo> getUserTagInfo(int userId) {
        // validate all
        UserServResult<UserTagInfo> result = UserServResult.create();

        UserTagInfo tagInfo = userTagMgr.getUserTagInfo(userId);
        if (tagInfo != null) {
            result.setErrCode(UserServResult.OK);
            result.setData(tagInfo);
        } else {
            result.setErrCode(UserErrorCode.NO_USER.getValue());
            result.setErrMsg(UserErrorCode.NO_USER.getMessage());
        }
        return result;
    }

    /**
     * @see UserTagService#batchGetUserTagInfo(List)
     */
    @Override
    public UserServResult<List<UserTagInfo>> batchGetUserTagInfo(List<Integer> userIds) {
        UserServResult<List<UserTagInfo>> result = UserServResult.create();
        if (CollectionUtil.isEmpty(userIds) || userIds.size() > UserConstant.MAX_USER_TAGS) {
            result.setErrCode(UserErrorCode.PARAM_ERROR.getValue());
            result.setErrMsg(UserErrorCode.PARAM_ERROR.getMessage());
        }
        List<UserTagInfo> tagInfoList = userTagMgr.batchGetUserTagInfos(userIds);
        result.setData(tagInfoList);
        if (tagInfoList == null) {
            result.setErrCode(UserServResult.RPC_FAIL);
        }
        return result;
    }

    /**
     * @see UserTagService#batchUpdateUserTagInfo(List)
     */
    @Override
    public UserServResult<List<Integer>> batchUpdateUserTagInfo(List<UserTagInfo> userTagInfoList) {
        UserServResult<List<Integer>> result = UserServResult.create();
        if (CollectionUtil.isEmpty(userTagInfoList) || userTagInfoList.size() > UserConstant.MAX_USER_TAGS) {
            result.setErrCode(UserErrorCode.PARAM_ERROR.getValue());
            result.setErrMsg(UserErrorCode.PARAM_ERROR.getMessage());
        }
        List<Integer> failedIdList = userTagMgr.batchUpdateUserTagInfos(userTagInfoList);
        result.setData(failedIdList);
        if (CollectionUtil.isNotEmpty(failedIdList)) {
            result.setErrCode(UserServResult.RPC_FAIL);
        }
        return result;
    }

    public UserTagMgr getUserTagMgr() {
        return userTagMgr;
    }

    public void setUserTagMgr(UserTagMgr userTagMgr) {
        this.userTagMgr = userTagMgr;
    }
}
