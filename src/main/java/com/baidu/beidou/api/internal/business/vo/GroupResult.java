package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class GroupResult {
	private int status; // 0正常，1系统错误，2参数错误，groupid2Name为null
	private Map<Integer, GroupInfo> groupid2Name;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Integer, GroupInfo> getGroupid2Name() {
		return groupid2Name;
	}
	public void setGroupid2Name(Map<Integer, GroupInfo> groupid2Name) {
		this.groupid2Name = groupid2Name;
	}

}
