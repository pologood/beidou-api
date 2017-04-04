package com.baidu.beidou.api.internal.audit.vo.request;

public class AuditResultUnit {
	
	private Integer userId;
	private Long unitId;
	private Integer tradeId; 
	private Integer tradeModified;
	private Integer accuracyType; 
	private Integer beautyType;
	private Integer vulgarType; 
	private Integer cheatType;
	private Integer dangerType;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	public Integer getTradeModified() {
		return tradeModified;
	}
	public void setTradeModified(Integer tradeModified) {
		this.tradeModified = tradeModified;
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

}
