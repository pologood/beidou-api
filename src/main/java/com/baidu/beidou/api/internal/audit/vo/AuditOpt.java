package com.baidu.beidou.api.internal.audit.vo;

public class AuditOpt {

	// 对外输出
	private Long unitId;
	private Integer userId;
	private String userName;
	private Integer wuliaoType; // tinyint(3) 否 物料类型（文字为1/图片为2/flash为3/图文为5/智能为8）
	private String wuliaoTypeStr;	// tinyint(3) 否 物料类型（文字为1/图片为2/flash为3/图文为5/智能为8）
	private String title;	// varchar(40) 否 标题
	private String desc1;	// varchar(80) 是 NULL 文字广告的描述1(图片和flash都不含)
	private String desc2;	// varchar(80) 是 NULL 文字广告的描述2(图片和flash都不含)
	private String size;	// width X height
	private String showUrl;	// varchar(35) 是 NULL 显示的URL地址，不包括“http(s)://”
	private String targetUrl;	// varchar(1024) 否 点击url
	private String subTime;	// 提交时间
	private String auditTime;	// 审核时间
	private String auditorName;	//审核员
	private Integer auditResult;	// 0: 审核通过，1：审核拒绝
	private String refuseReason;	// 拒绝理由
	private String refuseReasonStr;	// 拒绝理由
	private String remark;	// 备注
	private Long auditPeriod;	// 审核时长
	private Integer newTradeid;	// 新广告分类ID
	private String materUrl;	// 图片临时URL
	private String fileSrcMd5; // 多媒体md5
	
	private Integer groupId;
	private Integer planId;
	private String keyword; // varchar(1000) 是 NULL
			// 推广单元主题词，以‘|’分隔各个关键词，结尾一个’|’，如果用户输入中有’|’，则在beidou的web端替换为‘
			// ’(空格)，并在页面上给出提示。Beidou中只负责将用户输入的可见的“|”，至于汉字中隐藏的“|”，需要cpro进行处理。
	private Integer iftitle; // tinyint(3) 否 是否允许只出 标题，（只对文字广告有效），0否，1：是
	private Integer player; // tinyint(3) 是 NULL
			// 播放器版本，当用户端的播放器版本大于广告的player值，才能展现该flash广告，非flash广告时，该字段为空
	private Integer height; // smallint(5) 是 NULL
	private Integer width; // smallint(5) 是 NULL 图片或多媒体广告展现的宽度，文字广告这个字段为空。
	private String wirelessShowUrl; // 无线的显示url add for bmob
	private String wirelessTargetUrl; // 无线的点击curl add for bmob
	private int promotionType;
	private Integer ubmcSyncFlag;	// UBMC物料已同步
	private Long mcId = 0L; // UBMC物料ID
	private Integer mcVersionId = 0; // UBMC物料ID指定的版本ID
	private Integer auditorId;
	private Integer canDelete = Integer.valueOf(0); //默认为0
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getWuliaoType() {
		return wuliaoType;
	}
	public void setWuliaoType(Integer wuliaoType) {
		this.wuliaoType = wuliaoType;
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
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public Integer getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getIftitle() {
		return iftitle;
	}
	public void setIftitle(Integer iftitle) {
		this.iftitle = iftitle;
	}
	public Integer getPlayer() {
		return player;
	}
	public void setPlayer(Integer player) {
		this.player = player;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getWirelessShowUrl() {
		return wirelessShowUrl;
	}
	public void setWirelessShowUrl(String wirelessShowUrl) {
		this.wirelessShowUrl = wirelessShowUrl;
	}
	public String getWirelessTargetUrl() {
		return wirelessTargetUrl;
	}
	public void setWirelessTargetUrl(String wirelessTargetUrl) {
		this.wirelessTargetUrl = wirelessTargetUrl;
	}
	public int getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(int promotionType) {
		this.promotionType = promotionType;
	}
	public Integer getUbmcSyncFlag() {
		return ubmcSyncFlag;
	}
	public void setUbmcSyncFlag(Integer ubmcSyncFlag) {
		this.ubmcSyncFlag = ubmcSyncFlag;
	}
	public Long getMcId() {
		return mcId;
	}
	public void setMcId(Long mcId) {
		this.mcId = mcId;
	}
	public Integer getMcVersionId() {
		return mcVersionId;
	}
	public void setMcVersionId(Integer mcVersionId) {
		this.mcVersionId = mcVersionId;
	}
	public Integer getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}
	public Integer getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(Integer canDelete) {
		this.canDelete = canDelete;
	}
	public String getMaterUrl() {
		return materUrl;
	}
	public void setMaterUrl(String materUrl) {
		this.materUrl = materUrl;
	}
	public Integer getNewTradeid() {
		return newTradeid;
	}
	public void setNewTradeid(Integer newTradeid) {
		this.newTradeid = newTradeid;
	}
    public String getFileSrcMd5() {
        return fileSrcMd5;
    }
    public void setFileSrcMd5(String fileSrcMd5) {
        this.fileSrcMd5 = fileSrcMd5;
    }
}
