package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: RtRelationType
 * Function: 已关联搜索推广计划/单元信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class RtRelationType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// RT的关联类型。1 为关联到推广计划，0 为关联到推广单元
	private int relationType;
	
	// 凤巢推广计划的Id
	private Long fcPlanId;
	
	// 凤巢推广单元的Id
	private Long fcUnitId;
	
	// 凤巢推广计划的name
	private String fcPlanName;
	
	// 凤巢推广单元的name
	private String fcUnitName;
	
	public int getRelationType() {
		return relationType;
	}
	public void setRelationType(int relationType) {
		this.relationType = relationType;
	}
	public Long getFcPlanId() {
		return fcPlanId;
	}
	public void setFcPlanId(Long fcPlanId) {
		this.fcPlanId = fcPlanId;
	}
	public Long getFcUnitId() {
		return fcUnitId;
	}
	public void setFcUnitId(Long fcUnitId) {
		this.fcUnitId = fcUnitId;
	}
	public String getFcPlanName() {
		return fcPlanName;
	}
	public void setFcPlanName(String fcPlanName) {
		this.fcPlanName = fcPlanName;
	}
	public String getFcUnitName() {
		return fcUnitName;
	}
	public void setFcUnitName(String fcUnitName) {
		this.fcUnitName = fcUnitName;
	}

}
