package com.baidu.beidou.api.internal.business.vo;

public class GroupInfo {
	private String name;
	private int targettype;
	private int isDeleted; //删除状态为1，默认为0
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTargettype() {
		return targettype;
	}
	public void setTargettype(int targettype) {
		this.targettype = targettype;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
