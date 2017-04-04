package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: SiteVo  <br>
 * Function: 自选网站数据 <br>
 * 
 * 属性包括：网站URL,GroupId,CampaignId,网站出价信息
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class SiteVo implements AbstractVo{
	
	private Integer groupid;
	
	private Integer planid;
	
	private String siteurl;
	
	private Float price;
	
	public String[] toStringArray(){
		String[] str = new String[4];
		str[0] = String.valueOf(this.getSiteurl());
		str[1] = String.valueOf(this.getGroupid());
		str[2] = String.valueOf(this.getPlanid());
		str[3] = String.valueOf(this.getPrice());
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

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	

}
