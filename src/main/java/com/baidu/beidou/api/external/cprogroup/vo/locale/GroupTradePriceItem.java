/**
 * GroupTradePriceItem.java  2012-6-13
 *
 * Copyright 2012 Baidu, Inc. All rights reserved.
 * Baidu PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * @author kanghongwei
 * @date 2012-6-13
 */
public class GroupTradePriceItem {

	private int index; // 参数中所在索引

	private int trade; // 推广组行业id

	// 单位：元
	private float price;

	public GroupTradePriceItem() {
		super();
	}

	public GroupTradePriceItem(int index, int trade, float price) {
		super();
		this.index = index;
		this.trade = trade;
		this.price = price;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
