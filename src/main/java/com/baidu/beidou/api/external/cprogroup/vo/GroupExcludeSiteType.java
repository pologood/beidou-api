package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupExcludeSiteType
 * Function: 投放的排除网站
 *
 * @author Baidu API Team
 * @date 2012-3-31
 */
public class GroupExcludeSiteType implements Serializable {
	
	private static final long serialVersionUID = -4214552999523681666L;

	private long groupId;	// 推广组id
	
	private String excludeSite;	// 投放的排除网站

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getExcludeSite() {
		return excludeSite;
	}

	public void setExcludeSite(String excludeSite) {
		this.excludeSite = excludeSite;
	}
}
