/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.external.util.error;

/**
 * 错误信息类
 * 
 * @author Wang Yu
 * 
 */
public class ErrorParam {
    private String paramName;
    private Integer position;

    /**
     * @param paramName
     * @param position
     */
    public ErrorParam(String paramName, Integer position) {
        this.paramName = paramName;
        this.position = position;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
