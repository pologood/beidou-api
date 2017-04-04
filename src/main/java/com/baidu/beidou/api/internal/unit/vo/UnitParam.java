/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.vo;

import java.io.Serializable;

/**
 * 创意查询参数
 * Created by hewei18 on 2015-11-20.
 */
public class UnitParam implements Serializable {
    /**
     * 创意所属用户id, 必须给出, 否则无法做分片路由
     */
    private Integer userId;
    /**
     * 创意id
     */
    private Long unitId;

    public UnitParam() {
    }

    public UnitParam(Integer userId, Long unitId) {
        this.userId = userId;
        this.unitId = unitId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "UnitParam{"
                + "userId=" + userId
                + ", unitId=" + unitId
                + '}';
    }
}
