package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: SiteConfigType
 * Function: 网站及行业信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SiteConfigType implements Serializable {
	private static final long serialVersionUID = 1L;
	private long groupId;
	private boolean allSite;		// 是否选择全网站投放：true表示选择全网站投放，在数据库中1表示全网站
	private String[] siteList;		// 投放网站列表
	private int[] categoryList;		// 投放行业ID列表，allSite选择false时，行业和网站不能同时为空
	
	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the allSite
	 */
	public boolean isAllSite() {
		return allSite;
	}
	/**
	 * @param allSite the allSite to set
	 */
	public void setAllSite(boolean allSite) {
		this.allSite = allSite;
	}
	/**
	 * @return the siteList
	 */
	public String[] getSiteList() {
		return siteList;
	}
	/**
	 * @param siteList the siteList to set
	 */
	public void setSiteList(String[] siteList) {
		this.siteList = siteList;
	}
	/**
	 * @return the categoryList
	 */
	public int[] getCategoryList() {
		return categoryList;
	}
	/**
	 * @param categoryList the categoryList to set
	 */
	public void setCategoryList(int[] categoryList) {
		this.categoryList = categoryList;
	}

}
