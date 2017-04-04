package com.baidu.beidou.api.external.accountfile.vo;


/**
 * 
 * ClassName: VtVo  <br>
 * Function: 回头客到访定向人群数据 <br>
 * 
 * 属性包括：人群ID,RelationType,GroupId,CampaignId,人群名称,人群有效期
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 1, 2012
 */
public class VtVo implements AbstractVo{
	
	private Long pid;
	
	private Integer groupId;
	
	private Integer planId;
	
	private String peopleName;
	
	private int aliveDays;
	
	private Integer relateType;
	
	public String[] toStringArray(){
		String[] str = new String[6];
		str[0] = String.valueOf(this.getPid());
		str[1] = String.valueOf(this.getRelateType()); //取反，为来和VtType保持一致
		str[2] = String.valueOf(this.getGroupId());
		str[3] = String.valueOf(this.getPlanId());
		str[4] = String.valueOf(this.getPeopleName());
		str[5] = String.valueOf(this.getAliveDays());
		return str;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
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

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}

	public Integer getRelateType() {
		return relateType;
	}

	public void setRelateType(Integer relateType) {
		this.relateType = relateType;
	}

	

}
