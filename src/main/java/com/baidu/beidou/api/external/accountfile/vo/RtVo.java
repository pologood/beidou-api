package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: RtVo  <br>
 * Function: 回头客点击定向搜索推广关联数据 <br>
 * 
 * 属性包括：GroupId,RelationType,fcCampaignId,fcCampaignName,fcUnitId,fcUnitName
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 1, 2012
 */
public class RtVo implements AbstractVo{

	private Integer groupId;
	
	private Integer planId;
	
	private Long fcPlanId;
	
	private Long fcUnitId;
	
	private String fcPlanName;
	
	private String fcUnitName;
	
	private Integer relationType;
	
	public String[] toStringArray(){
		String[] str = new String[7];
		str[0] = String.valueOf(this.getGroupId());
		str[1] = String.valueOf(this.getPlanId());
		str[2] = String.valueOf(this.getRelationType());
		str[3] = String.valueOf(this.getFcPlanId());
		str[4] = String.valueOf(this.getFcPlanName());
		str[5] = String.valueOf(this.getFcUnitId());
		str[6] = String.valueOf(this.getFcUnitName());
		return str;
	}
	

	public Integer getPlanId() {
		return planId;
	}



	public void setPlanId(Integer planId) {
		this.planId = planId;
	}



	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}
	

}
