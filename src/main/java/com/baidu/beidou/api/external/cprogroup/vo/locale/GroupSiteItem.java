package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupSiteItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class GroupSiteItem {

	private int index;	// 参数中所在索引
	
	private String site;	// 投放网站

	public GroupSiteItem(int index, String site) {
		this.index = index;
		this.site = site;
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
}
