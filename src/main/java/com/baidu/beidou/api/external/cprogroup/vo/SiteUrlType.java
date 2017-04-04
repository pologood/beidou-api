package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: SiteUrlType
 * Function: 分网站点击URL列表
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SiteUrlType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// 推广组ID
	private long groupId;
	
	// 分网站点击链接
	private List<SiteUrlItemType> siteUrlList;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<SiteUrlItemType> getSiteUrlList() {
		return siteUrlList;
	}

	public void setSiteUrlList(List<SiteUrlItemType> siteUrlList) {
		this.siteUrlList = siteUrlList;
	}
	
}
