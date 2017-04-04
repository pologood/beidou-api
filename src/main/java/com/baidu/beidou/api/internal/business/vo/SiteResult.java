package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class SiteResult {
	private int status; //0正常，1系统错误，2参数错误，siteKey2Name为null
	private Map<String, String> siteKey2Url;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, String> getSiteKey2Url() {
		return siteKey2Url;
	}
	public void setSiteKey2Url(Map<String, String> siteKey2Url) {
		this.siteKey2Url = siteKey2Url;
	}

}
