/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * 相似人群API接口Model
 * 
 * @author Wang Yu
 * 
 */
public class GroupSimilarPeopleType implements Serializable {

    private static final long serialVersionUID = -1401894376485878539L;

    private int groupId;
    private int similarFlag;

    /**
     * 相似人群Model
     */
    public GroupSimilarPeopleType() {
        super();
    }

    /**
     * @param groupId 推广组ID
     * @param similarFlag 相似人群设置
     */
    public GroupSimilarPeopleType(int groupId, int similarFlag) {
        super();
        this.groupId = groupId;
        this.similarFlag = similarFlag;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSimilarFlag() {
        return similarFlag;
    }

    public void setSimilarFlag(int similarFlag) {
        this.similarFlag = similarFlag;
    }
}
