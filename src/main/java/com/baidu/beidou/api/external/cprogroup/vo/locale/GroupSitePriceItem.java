package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupSitePriceItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-6-14
 */
public class GroupSitePriceItem {
	
	private int index;	// 参数中所在索引
	
	private String site;	// 投放网站
	
	private float price;	// 出价
	
	public GroupSitePriceItem(int index, String site, float price) {
		this.index = index;
		this.site = site;
		this.price = price;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
