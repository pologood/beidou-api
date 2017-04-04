package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupVtItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-3-30
 */
public class GroupVtItem {
	
	private int index;	// 参数中所在索引
	
	private int relationType;	// 人群类型：0-关联人群，1-排除人群

	private long peopleId;	// 人群id
	

	public GroupVtItem(int index, int relationType, long peopleId) {
		this.index = index;
		this.relationType = relationType;
		this.peopleId = peopleId;
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

	public long getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(long peopleId) {
		this.peopleId = peopleId;
	}
}
