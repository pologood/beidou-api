package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class KeywordResult {

	private int status; // 0正常，1系统错误，2参数错误，keywordid2Name为null
	
	private Map<Long, KeywordInfo> keywordid2Name;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<Long, KeywordInfo> getKeywordid2Name() {
		return keywordid2Name;
	}

	public void setKeywordid2Name(Map<Long, KeywordInfo> keywordid2Name) {
		this.keywordid2Name = keywordid2Name;
	}
	
}
