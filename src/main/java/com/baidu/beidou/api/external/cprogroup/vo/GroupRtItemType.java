package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupRtItemType
 * Function: 关联凤巢信息
 *
 * @author Baidu API Team
 * @date 2012-3-29
 */
public class GroupRtItemType implements Serializable {
	
	private static final long serialVersionUID = -7057588800427833691L;

	private long groupId;	// 推广组id
	
	private int relationType;	// 回头客的关联类型：1-关联到推广计划，0-关联到推广单元

	private long fcPlanId;	// 要关联的搜索推广计划id
	
	private long fcUnitId;	// 要关联的搜索推广创意id

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

	public long getFcPlanId() {
		return fcPlanId;
	}

	public void setFcPlanId(long fcPlanId) {
		this.fcPlanId = fcPlanId;
	}

	public long getFcUnitId() {
		return fcUnitId;
	}

	public void setFcUnitId(long fcUnitId) {
		this.fcUnitId = fcUnitId;
	}
}
