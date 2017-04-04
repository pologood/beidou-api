package com.baidu.beidou.api.external.interest.vo;

/**
 * 
 * ClassName: InterestType  <br>
 * Function: 基本兴趣点
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class InterestType {

	private int interestId;
	
	private String interestName;
	
	private int parentId;
	
	public InterestType() {

	}
	
	public InterestType(int interestId, String interestName, int parentId) {
		this.interestId = interestId;
		this.interestName = interestName;
		this.parentId = parentId;
	}

	public int getInterestId() {
		return interestId;
	}

	public void setInterestId(int interestId) {
		this.interestId = interestId;
	}

	public String getInterestName() {
		return interestName;
	}

	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	

}
