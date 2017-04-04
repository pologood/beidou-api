package com.baidu.beidou.api.external.tool.vo.response;

import java.io.Serializable;

import com.baidu.beidou.api.external.tool.vo.LastHistoryResponseType;

public class GetLastHistoryResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2910096040259476569L;
	private LastHistoryResponseType data;

	public LastHistoryResponseType getData() {
		return data;
	}

	public void setData(LastHistoryResponseType data) {
		this.data = data;
	}
	
	public String toString(){
		return data.toString();
	}
}
