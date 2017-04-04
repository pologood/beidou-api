package com.baidu.beidou.api.internal.audit.vo.request;

import java.util.Date;
import java.util.Map;

/**
 * 标注更新请求类
 * 
 * @author zhangzhenhua02
 * @version 1.0.0
 */
public class QueryUnitTagAndTrade {

    private Integer userId;
    private Long unitId;
    private Integer tradeId;
    private Date modTime;
    private Map<Integer, Integer> tagTypeValueMap; // tag的tagType和tagValue
    
    /**
     * @return userId
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * @param userId userid
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    /**
     * @return unitId
     */
    public Long getUnitId() {
        return unitId;
    }
    /**
     * @param unitId unitId
     */
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
    /**
     * @return tradeId
     */
    public Integer getTradeId() {
        return tradeId;
    }
    /**
     * @param tradeId tradeId
     */
    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }
    /**
     * @return modTime
     */
    public Date getModTime() {
        return modTime;
    }
    /**
     * @param modTime modTime
     */
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }
    /**
     * @return tagTypeValueMap
     */
    public Map<Integer, Integer> getTagTypeValueMap() {
        return tagTypeValueMap;
    }
    /**
     * @param tagTypeValueMap tagTypeValueMap
     */
    public void setTagTypeValueMap(Map<Integer, Integer> tagTypeValueMap) {
        this.tagTypeValueMap = tagTypeValueMap;
    }
}
