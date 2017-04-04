package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupTradeType
 * Function: 投放网站行业设置
 *
 * @author Baidu API Team
 * @date 2012-3-31
 */
public class GroupTradeType implements Serializable {
		
	private static final long serialVersionUID = 1350409871924606011L;

	private long groupId;	// 推广组id
	
	private int trade;	// 投放的网站分类id

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
}
