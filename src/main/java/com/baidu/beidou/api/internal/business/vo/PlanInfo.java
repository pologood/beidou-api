package com.baidu.beidou.api.internal.business.vo;

public class PlanInfo {
	private String name;
	private int isDeleted; //删除状态为1，默认为0
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
