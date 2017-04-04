package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class KtEnabledMultiResult {

	private int status; //0正常，1错误, 2参数错误，enabled为-1
	
	private Map<Integer, KtEnabledInfo> userid2KtEnabled;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<Integer, KtEnabledInfo> getUserid2KtEnabled() {
		return userid2KtEnabled;
	}

	public void setUserid2KtEnabled(Map<Integer, KtEnabledInfo> userid2KtEnabled) {
		this.userid2KtEnabled = userid2KtEnabled;
	}

}
