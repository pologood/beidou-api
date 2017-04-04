package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupTradeItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-4-12
 */
public class GroupTradeItem {
	
	private int index;	// 参数中所在索引
	
	private int trade;	// 投放的行业分类id

	public GroupTradeItem(int index, int trade) {
		this.index = index;
		this.trade = trade;
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
}
