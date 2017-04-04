package com.baidu.beidou.api.internal.unit.vo;

/**
 * Function: 上传的media返回的ubmc相关id信息
 * 
 * @ClassName: MediaResult
 * @author genglei01
 * @date Apr 28, 2015 9:04:28 PM
 */
public class MediaResult {
    
    private Long mcId;  // ubmc物料ID
    private Integer versionId;  // ubmc物料对应的版本ID
    private Long mediaId;   // ubmc物料对应多媒体的ID
    
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
    public Long getMediaId() {
        return mediaId;
    }
    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }
    
}
