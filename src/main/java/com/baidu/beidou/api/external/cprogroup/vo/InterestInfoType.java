package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * 
 * ClassName: InterestInfoType  <br>
 * Function: 推广组受众兴趣信息
 *
 * @author zhangxu
 * @date Jun 5, 2012
 */
public class InterestInfoType {

	private long groupId;
	
	private boolean enable;
	
	private int[] interestIds;
	
	private int[] exceptInterestIds;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
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
