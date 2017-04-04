package com.baidu.beidou.api.internal.audit.vo;

public class UnitAuditInfo {
	private Long unitId;

	private int promotionType;
	
	private UnitMaterView unit;
	private UnitMaterView preUnit;
	
	private String preRefuseReason;	// 前一个物料拒绝理由，初审可见

	private String groupName;
	private Integer groupId;
	private String planName;
	private Integer planId;
	
	private Integer userId;
	
	private String subTime;	// 格式：yyyy-MM-dd HH:mm:ss
	private Integer tradeId;	// 行业分类ID
	private Integer accuracyType;
	private Integer beautyType;
	private Integer vulgarType;
	private Integer cheatType;
	private Integer dangerType;
	private String auditorName;
	
    private String auditHistory;    // 物料在审核系统中存储的历史审核标记位
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public UnitMaterView getUnit() {
		return unit;
	}
	public void setUnit(UnitMaterView unit) {
		this.unit = unit;
	}
	public UnitMaterView getPreUnit() {
		return preUnit;
	}
	public void setPreUnit(UnitMaterView preUnit) {
		this.preUnit = preUnit;
	}
	public String getPreRefuseReason() {
		return preRefuseReason;
	}
	public void setPreRefuseReason(String preRefuseReason) {
		this.preRefuseReason = preRefuseReason;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	public Integer getAccuracyType() {
		return accuracyType;
	}
	public void setAccuracyType(Integer accuracyType) {
		this.accuracyType = accuracyType;
	}
	public Integer getBeautyType() {
		return beautyType;
	}
	public void setBeautyType(Integer beautyType) {
		this.beautyType = beautyType;
	}
	public Integer getVulgarType() {
		return vulgarType;
	}
	public void setVulgarType(Integer vulgarType) {
		this.vulgarType = vulgarType;
	}
	public Integer getCheatType() {
		return cheatType;
	}
	public void setCheatType(Integer cheatType) {
		this.cheatType = cheatType;
	}
	public int getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(int promotionType) {
		this.promotionType = promotionType;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
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

    /**
     * @return 高危度
     */
    public Integer getDangerType() {
        return dangerType;
    }

    /**
     * @param dangerType 高危度
     */
    public void setDangerType(Integer dangerType) {
        this.dangerType = dangerType;
    }
    
    public String getAuditHistory() {
        return auditHistory;
    }

    public void setAuditHistory(String auditHistory) {
        this.auditHistory = auditHistory;
    }
}
