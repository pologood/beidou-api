package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupExcludeIpType
 * Function: 投放的过滤IP
 *
 * @author Baidu API Team
 * @date 2012-3-31
 */
public class GroupExcludeIpType implements Serializable {
	
	private static final long serialVersionUID = 8144298355383188637L;

	private long groupId;	// 推广组id
	
	private String excludeIp;	// 投放的排除ip

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getExcludeIp() {
		return excludeIp;
	}

	public void setExcludeIp(String excludeIp) {
		this.excludeIp = excludeIp;
	}
}
