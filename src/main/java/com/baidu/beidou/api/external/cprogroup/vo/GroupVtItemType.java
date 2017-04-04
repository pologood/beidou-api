package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupVtItemType
 * Function: 人群设置信息
 *
 * @author Baidu API Team
 * @date 2012-3-29
 */
public class GroupVtItemType implements Serializable {
	
	private static final long serialVersionUID = -1626518986726066315L;

	private long groupId;	// 推广组id
	
	private int relationType;	// 人群类型：0-关联人群，1-排除人群

	private long peopleId;	// 人群id

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getRelationType() {
		return relationType;
	}

	public void setRelationType(int relationType) {
		this.relationType = relationType;
	}

	public long getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(long peopleId) {
		this.peopleId = peopleId;
	}
}
