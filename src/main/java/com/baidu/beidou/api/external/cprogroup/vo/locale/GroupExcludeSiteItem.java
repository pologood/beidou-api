package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupExcludeSiteItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-4-12
 */
public class GroupExcludeSiteItem {

	private int index;	// 参数中所在索引
	
	private String excludeSite;	// 投放的排除网站

	public GroupExcludeSiteItem(int index, String excludeSite) {
		this.index = index;
		this.excludeSite = excludeSite;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getExcludeSite() {
		return excludeSite;
	}

	public void setExcludeSite(String excludeSite) {
		this.excludeSite = excludeSite;
	}
}
