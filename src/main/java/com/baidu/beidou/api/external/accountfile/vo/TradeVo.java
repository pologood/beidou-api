package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: TradeVo  <br>
 * Function: 自选行业数据 <br>
 * 
 * 属性包括：行业分类ID,行业名称,GroupId,CampaignId,行业出价信息
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class TradeVo implements AbstractVo{

	private Integer groupid;
	
	private Integer planid;
	
	private Integer tradeid;
	
	private String tradename;
	
	private Float price;
	
	public String[] toStringArray(){
		String[] str = new String[5];
		str[0] = String.valueOf(this.getTradeid());
		str[1] = String.valueOf(this.getTradename());
		str[2] = String.valueOf(this.getGroupid());
		str[3] = String.valueOf(this.getPlanid());
		str[4] = String.valueOf(this.getPrice());
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

	public Integer getTradeid() {
		return tradeid;
	}

	public void setTradeid(Integer tradeid) {
		this.tradeid = tradeid;
	}

	public String getTradename() {
		return tradename;
	}

	public void setTradename(String tradename) {
		this.tradename = tradename;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	
	
}
