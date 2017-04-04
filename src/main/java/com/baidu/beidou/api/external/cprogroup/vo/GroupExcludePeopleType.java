package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: GroupExcludePeopleType  <br>
 * Function: 推广组排除人群类
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class GroupExcludePeopleType implements Serializable {
	
	private static final long serialVersionUID = 12587628406987L;

	private long groupId;
	
	private long excludePeopleId;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getExcludePeopleId() {
		return excludePeopleId;
	}

	public void setExcludePeopleId(long excludePeopleId) {
		this.excludePeopleId = excludePeopleId;
	}
	

}
