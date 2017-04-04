package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupSiteType
 * Function: 投放网站
 *
 * @author Baidu API Team
 * @date 2012-3-31
 */
public class GroupSiteType implements Serializable {
	
	private static final long serialVersionUID = -524341487876245646L;

	private long groupId;	// 推广组id
	
	private String site;	// 投放的网站url

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
}
