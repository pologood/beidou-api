package com.baidu.beidou.api.internal.audit.vo;

/**
 * Function: 请求url相关ID
 * 
 * @ClassName: MaterialUrlRequest
 * @author genglei01
 * @date Mar 17, 2015 5:31:14 PM
 */
public class MaterialUrlRequest {

    /**
     * ubmc物料ID
     */
    private Long mcId;
    
    /**
     * ubmc物料版本ID
     */
    private Integer versionId;

    public MaterialUrlRequest() {
    }

    public MaterialUrlRequest(Long mcId, Integer versionId) {
        this.mcId = mcId;
        this.versionId = versionId;
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

}
