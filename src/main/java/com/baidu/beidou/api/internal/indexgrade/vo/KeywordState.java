/**
 * Copyright (C) 2015年10月27日 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.indexgrade.vo;

/**
 * 索引分级中关键词的状态信息
 * @author wangxiongjie
 * 
 */
public class KeywordState {

    private long keywordId;
    
    private int state;

    
    
    public KeywordState() {
        super();
    }

    public KeywordState(long keywordId, int state) {
        super();
        this.keywordId = keywordId;
        this.state = state;
    }

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    
}
