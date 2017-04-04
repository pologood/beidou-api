package com.baidu.beidou.api.internal.audit.vo;

public class UnitResponse {
    private Long unitId;
    private String groupName;   // 推广组名称
    private Integer groupId;    // 推广组ID
    private String planName;    // 推广计划名称
    private Integer planId; // 推广计划ID
    private Integer userId; // 用户ID
    private String userName;    // 用户名

    private int promotionType;  // 推广类型：0-全部，1-仅移动

    private Integer type;   // 创意类型，1：文字，2：图片，3：flash，5：图文混排
    private String title;   // 标题
    private String desc1;   // 描述1，文本或者图文混排创意时有效
    private String desc2;   // 描述2，文本或者图文混排创意时有效
    private String targetUrl;   // 点击链接
    private String showUrl; // 显示链接
    private String wirelessTargetUrl;   // 无线点击链接
    private String wirelessShowUrl; // 无线显示链接
    private String materUrl;    // 多媒体链接，图片、flash或者图文混排创意时有效
    private Integer width;  // 宽，图片、flash或者图文混排创意时有效
    private Integer height; // 高，图片、flash或者图文混排创意时有效
    
    private Integer isSmart = 0;    // 智能创意标识，0：普通创意，默认值；1：智能创意
    private Integer templateId = 0; // 智能创意模板id，默认为0，没有模板

    private String modTime; // 最后一次修改时间，格式：yyyy-MM-dd HH:mm:ss
    private Integer tradeId; // 行业分类ID
    private Integer unitTag;    // 创意标注

    private Integer[] refuseReasonIds;  // 拒绝理由ID
    private String auditTime;  // 审核时间，格式：yyyy-MM-dd HH:mm:ss
    private int auditResult; // 审核状态：0表示通过，1表示拒绝，2表示暂停
    private Long mcId;  // ubmc物料ID
    private Integer versionId;  // ubmc物料版本ID
    
    public Long getUnitId() {
        return unitId;
    }
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public String getPlanName() {
        return planName;
    }
    public void setPlanName(String planName) {
        this.planName = planName;
    }
    public Integer getPlanId() {
        return planId;
    }
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getPromotionType() {
        return promotionType;
    }
    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc1() {
        return desc1;
    }
    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }
    public String getDesc2() {
        return desc2;
    }
    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }
    public String getTargetUrl() {
        return targetUrl;
    }
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    public String getShowUrl() {
        return showUrl;
    }
    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
    public String getWirelessTargetUrl() {
        return wirelessTargetUrl;
    }
    public void setWirelessTargetUrl(String wirelessTargetUrl) {
        this.wirelessTargetUrl = wirelessTargetUrl;
    }
    public String getWirelessShowUrl() {
        return wirelessShowUrl;
    }
    public void setWirelessShowUrl(String wirelessShowUrl) {
        this.wirelessShowUrl = wirelessShowUrl;
    }
    public String getMaterUrl() {
        return materUrl;
    }
    public void setMaterUrl(String materUrl) {
        this.materUrl = materUrl;
    }
    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public Integer getIsSmart() {
        return isSmart;
    }
    public void setIsSmart(Integer isSmart) {
        this.isSmart = isSmart;
    }
    public Integer getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
    public String getModTime() {
        return modTime;
    }
    public void setModTime(String modTime) {
        this.modTime = modTime;
    }
    public Integer getTradeId() {
        return tradeId;
    }
    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }
    public Integer getUnitTag() {
        return unitTag;
    }
    public void setUnitTag(Integer unitTag) {
        this.unitTag = unitTag;
    }
    public Integer[] getRefuseReasonIds() {
        return refuseReasonIds;
    }
    public void setRefuseReasonIds(Integer[] refuseReasonIds) {
        this.refuseReasonIds = refuseReasonIds;
    }
    public String getAuditTime() {
        return auditTime;
    }
    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
    public int getAuditResult() {
        return auditResult;
    }
    public void setAuditResult(int auditResult) {
        this.auditResult = auditResult;
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
