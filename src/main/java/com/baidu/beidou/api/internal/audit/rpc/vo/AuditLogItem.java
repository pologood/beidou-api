package com.baidu.beidou.api.internal.audit.rpc.vo;

/**
 * Function: 审核日志，推送给风控审核使用
 * 
 * @ClassName: AuditLogItem
 * @author genglei01
 * @date Mar 19, 2015 7:07:26 PM
 */
public class AuditLogItem {
    
    private Long unitId;
    private Integer userId;
    private Integer appId;
    private String wuliaoTypeStr; // tinyint(3) 否 物料类型（文字为1/图片为2/flash为3/图文为5/智能为8）
    private String title; // varchar(40) 否 标题
    private String desc1; // varchar(80) 是 NULL 文字广告的描述1(图片和flash都不含)
    private String desc2; // varchar(80) 是 NULL 文字广告的描述2(图片和flash都不含)
    private String materUrl; // 图片临时URL
    private String size; // width X height
    private String showUrl; // varchar(35) 是 NULL 显示的URL地址，不包括“http(s)://”
    private String targetUrl; // varchar(1024) 否 点击url
    private String modTime; // 提交时间
    private String auditTime; // 审核时间
    private Integer auditorId;
    private Integer auditResult; // 0: 审核通过，1：审核拒绝
    private int[] refuseReasonIds; // 拒绝理由
    private String refuseReasonStr; // 拒绝理由
    private String remark; // 备注
    private Long auditPeriod; // 审核时长
    private Integer newTradeid; // 新广告分类ID
    private Long mcId = 0L; // UBMC物料ID
    private Integer newVersionId = 0; // UBMC物料ID指定的新版本ID
    private Integer oldVersionId = 0; // UBMC物料ID指定的旧版本ID
    
    /**
     * 默认构造器
     * 
     */
    public AuditLogItem() {
    }

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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getWuliaoTypeStr() {
        return wuliaoTypeStr;
    }

    public void setWuliaoTypeStr(String wuliaoTypeStr) {
        this.wuliaoTypeStr = wuliaoTypeStr;
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

    public String getMaterUrl() {
        return materUrl;
    }

    public void setMaterUrl(String materUrl) {
        this.materUrl = materUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public int[] getRefuseReasonIds() {
        return refuseReasonIds;
    }

    public void setRefuseReasonIds(int[] refuseReasonIds) {
        this.refuseReasonIds = refuseReasonIds;
    }

    public String getRefuseReasonStr() {
        return refuseReasonStr;
    }

    public void setRefuseReasonStr(String refuseReasonStr) {
        this.refuseReasonStr = refuseReasonStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAuditPeriod() {
        return auditPeriod;
    }

    public void setAuditPeriod(Long auditPeriod) {
        this.auditPeriod = auditPeriod;
    }

    public Integer getNewTradeid() {
        return newTradeid;
    }

    public void setNewTradeid(Integer newTradeid) {
        this.newTradeid = newTradeid;
    }

    public Long getMcId() {
        return mcId;
    }

    public void setMcId(Long mcId) {
        this.mcId = mcId;
    }

    public Integer getNewVersionId() {
        return newVersionId;
    }

    public void setNewVersionId(Integer newVersionId) {
        this.newVersionId = newVersionId;
    }

    public Integer getOldVersionId() {
        return oldVersionId;
    }

    public void setOldVersionId(Integer oldVersionId) {
        this.oldVersionId = oldVersionId;
    }
}
