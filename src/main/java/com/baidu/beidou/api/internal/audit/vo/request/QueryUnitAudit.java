package com.baidu.beidou.api.internal.audit.vo.request;

import java.util.List;

public class QueryUnitAudit extends QueryBase {
	
	private Integer userId;
	private Integer planState;	// 推广计划状态：-1：全部，0：有效，1：暂停，2：删除，3：未开始，4：已结束（默认为-1）
	private Integer groupState;	// 推广组状态：-1：全部，0：有效，1：暂停，2：删除（默认为-1）
	private Integer unitType;	// 物料类型：-1：全部，1：文字，2：图片，3：图文（默认为-1）
	private Integer accuracyType;	// 准确度：-1：全部，0：未评定，1：低，2：中，3：高（默认为-1）
	private Integer beautyType;	// 美观度：-1：全部，0：未评定，1：低，2：中，3：高（默认为-1）
	private Integer vulgarType;	// 低俗：-1：全部，0：未评定，1：白，2：灰，3：黑（默认为-1）
	private Integer cheatType;	// 欺诈：-1：全部，0：未评定，1：否，2：是（默认为-1）
	private List<Long> unitIds;	// 待审核创意ID
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getPlanState() {
		return planState;
	}
	public void setPlanState(Integer planState) {
		this.planState = planState;
	}
	public Integer getGroupState() {
		return groupState;
	}
	public void setGroupState(Integer groupState) {
		this.groupState = groupState;
	}
	public Integer getUnitType() {
		return unitType;
	}
	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
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
	public List<Long> getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(List<Long> unitIds) {
		this.unitIds = unitIds;
	}

}
