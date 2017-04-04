package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * 
 * ClassName: GroupInterestInfoType  <br>
 * Function: 推广组兴趣信息
 *
 * @author zhangxu
 * @date Jun 5, 2012
 */
public class GroupInterestInfoType {

	private long groupId;
	
	private int[] interestIds;
	
	private int[] exceptInterestIds;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int[] getInterestIds() {
		return interestIds;
	}

	public void setInterestIds(int[] interestIds) {
		this.interestIds = interestIds;
	}

	public int[] getExceptInterestIds() {
		return exceptInterestIds;
	}

	public void setExceptInterestIds(int[] exceptInterestIds) {
		this.exceptInterestIds = exceptInterestIds;
	}
	
	

}
