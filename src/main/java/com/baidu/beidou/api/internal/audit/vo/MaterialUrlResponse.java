package com.baidu.beidou.api.internal.audit.vo;

/**
 * Function: 请求url返回结果
 * 
 * @ClassName: MaterialUrlResponse
 * @author genglei01
 * @date Mar 17, 2015 5:31:14 PM
 */
public class MaterialUrlResponse {

    /**
     * ubmc物料ID
     */
    private Long mcId;
    
    /**
     * ubmc物料版本ID
     */
    private Integer versionId;
    
    /**
     * url串，对图片/flash/图文混排有效
     */
    private String url;
    
    public MaterialUrlResponse(){
    }
    
    public MaterialUrlResponse(Long mcId, Integer versionId, String url) {
        this.mcId = mcId;
        this.versionId = versionId;
        this.url = url;
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
