package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * 
 * ClassName: GroupSitePriceType  <br>
 * Function: 分网站出价设置
 *
 * @author zhangxu
 * @date Jun 3, 2012
 */
public class GroupSitePriceType {

	private long groupId;
	
	private String site;
	
	// 单位：元
	private float price;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
