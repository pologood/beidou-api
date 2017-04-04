package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: ItVo  <br>
 * Function: 兴趣数据 <br>
 * 
 * 属性包括：兴趣ID,GroupId,CampaignId,兴趣名称,是否为排除兴趣,是否为兴趣组合
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 1, 2012
 */
public class ItVo implements AbstractVo{
	
	private Integer itId;

	private Integer groupId;
	
	private Integer planId;
	
	private String itName;
	
	private Integer isExclude;  //是否为排除兴趣
	
	private Integer type;  //是否为兴趣组合
	
	private Integer price;  //兴趣出价
	
	public String[] toStringArray(){
		String[] str = new String[7];
		str[0] = String.valueOf(this.getItId());
		str[1] = String.valueOf(this.getGroupId());
		str[2] = String.valueOf(this.getPlanId());
		str[3] = String.valueOf(this.getItName());
		str[4] = String.valueOf(this.getIsExclude());
		str[5] = String.valueOf(this.getType());
		str[6] = String.valueOf(this.getPrice() == null ? "" : this.getPrice());
		return str;
	}

	public Integer getItId() {
		return itId;
	}

	public void setItId(Integer itId) {
		this.itId = itId;
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

	public String getItName() {
		return itName;
	}

	public void setItName(String itName) {
		this.itName = itName;
	}

	public Integer getIsExclude() {
		return isExclude;
	}

	public void setIsExclude(Integer isExclude) {
		this.isExclude = isExclude;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	

}
