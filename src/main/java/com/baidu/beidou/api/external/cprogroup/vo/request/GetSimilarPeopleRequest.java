/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 获取相似人群设置API请求Model
 * 
 * @author Wang Yu
 * 
 */
public class GetSimilarPeopleRequest implements Serializable, ApiRequest {

    private static final long serialVersionUID = 7624302739894171988L;

    private int[] groupIds;

    @Override
    public int getDataSize() {
        int result = 0;

        if (!ArrayUtils.isEmpty(groupIds)) {
            result = groupIds.length;
        }

        return result;
    }

    public int[] getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(int[] groupIds) {
        this.groupIds = groupIds;
    }
}
