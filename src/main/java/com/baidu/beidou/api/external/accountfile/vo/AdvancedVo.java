package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: AdvancedVo  <br>
 * Function: 高级组合投放数据
 *
 * @author zhangxu
 * @date Sep 19, 2012
 */
public class AdvancedVo implements AbstractVo{

	private Integer groupid;
	
	private Integer planid;
	
	private Integer packType;
	
	private Integer packId;
	
	private Integer packPrice;
	
	public String[] toStringArray(){
		String[] str = new String[5];
		str[0] = String.valueOf(this.getGroupid());
		str[1] = String.valueOf(this.getPlanid());
		str[2] = String.valueOf(this.getPackType());
		str[3] = String.valueOf(this.getPackId());
		str[4] = String.valueOf((this.getPackPrice() == null || this.getPackPrice().equals(0))  ? "" : this.getPackPrice());
		return str;
	}
	
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public Integer getPlanid() {
		return planid;
	}

	public void setPlanid(Integer planid) {
		this.planid = planid;
	}

	public Integer getPackType() {
		return packType;
	}

	public void setPackType(Integer packType) {
		this.packType = packType;
	}

	public Integer getPackId() {
		return packId;
	}

	public void setPackId(Integer packId) {
		this.packId = packId;
	}

	public Integer getPackPrice() {
		return packPrice;
	}

	public void setPackPrice(Integer packPrice) {
		this.packPrice = packPrice;
	}

}
