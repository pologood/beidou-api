package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupRtItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-3-30
 */
public class GroupRtItem {
	
	private int index;	// 参数中所在索引
	
	private int relationType;	// 回头客的关联类型：1-关联到推广计划，0-关联到推广单元

	private long fcPlanId;	// 要关联的搜索推广计划id
	
	private long fcUnitId;	// 要关联的搜索推广创意id

	public GroupRtItem(int index, int relationType, long fcPlanId, long fcUnitId) {
		this.index = index;
		this.relationType = relationType;
		this.fcPlanId = fcPlanId;
		this.fcUnitId = fcUnitId;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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