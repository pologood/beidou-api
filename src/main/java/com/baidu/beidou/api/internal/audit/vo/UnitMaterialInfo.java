package com.baidu.beidou.api.internal.audit.vo;

/**
 * Function: 创意物料信息，包含图片或者flash的二进制文件
 * 
 * @ClassName: UnitMaterialInfo
 * @author genglei01
 * @date Dec 31, 2014 2:33:43 PM
 */
public class UnitMaterialInfo {
    private Long unitId;
    private Integer userId;
    private Long mcId;
    private Integer versionId;
    private Integer wuliaoType;
    private String fileSrcMd5;
    private String targetUrl;
    private byte[] data; // 多媒体二进制文件
    
    public Long getUnitId() {
        return unitId;
    }
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Long getMcId() {
        return mcId;
    }
    public void setMcId(Long mcId) {
        this.mcId = mcId;
    }
    public Integer getVersionId() {
        return versionId;
    }
    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
    public Integer getWuliaoType() {
        return wuliaoType;
    }
    public void setWuliaoType(Integer wuliaoType) {
        this.wuliaoType = wuliaoType;
    }
    public String getFileSrcMd5() {
        return fileSrcMd5;
    }
    public void setFileSrcMd5(String fileSrcMd5) {
        this.fileSrcMd5 = fileSrcMd5;
    }
    public String getTargetUrl() {
        return targetUrl;
    }
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
