/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import com.baidu.beidou.api.external.cprogroup.vo.GroupSimilarPeopleType;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * 修改相似人群设置API请求Model
 * 
 * @author Wang Yu
 * 
 */
public class UpdateSimilarPeopleRequest implements ApiRequest {
    private List<GroupSimilarPeopleType> similarPeoples;

    public List<GroupSimilarPeopleType> getSimilarPeoples() {
        return similarPeoples;
    }

    public void setSimilarPeoples(List<GroupSimilarPeopleType> similarPeoples) {
        this.similarPeoples = similarPeoples;
    }

    @Override
    public int getDataSize() {
        if (CollectionUtil.isEmpty(similarPeoples)) {
            return 0;
        }
        return similarPeoples.size();
    }
}