package com.baidu.beidou.api.internal.business.vo;

public class KtEnabledResult {

	private int status; //0正常，1错误, 2参数错误，enabled为-1
	
	private int isEnabled = -1; //0未启用KT，1启用KT，-1未设置

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

}
