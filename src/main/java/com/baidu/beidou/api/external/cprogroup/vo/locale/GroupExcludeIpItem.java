package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupExcludeIpItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-4-12
 */
public class GroupExcludeIpItem {
	
	private int index;	// 参数中所在索引
	
	private String excludeIp;	// 投放的排除ip

	public GroupExcludeIpItem(int index, String excludeIp) {
		this.index = index;
		this.excludeIp = excludeIp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getExcludeIp() {
		return excludeIp;
	}

	public void setExcludeIp(String excludeIp) {
		this.excludeIp = excludeIp;
	}
}
