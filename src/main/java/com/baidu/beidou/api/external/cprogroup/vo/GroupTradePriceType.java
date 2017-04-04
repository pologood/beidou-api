package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * 
 * ClassName: GroupTradePriceType  <br>
 * Function: 分行业出价设置
 *
 * @author zhangxu
 * @date Jun 3, 2012
 */
public class GroupTradePriceType {

	private long groupId;
	
	private int trade;
	
	// 单位：元
	private float price;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getTrade() {
		return trade;
	}

	public void setTrade(int trade) {
		this.trade = trade;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
