package com.baidu.beidou.api.external.tool.vo.response;

import java.io.Serializable;

import com.baidu.beidou.api.external.tool.vo.AttachInfoUserResponseType;
import com.baidu.beidou.api.external.tool.vo.LastHistoryResponseType;

public class AttachInfoUserResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7789618180419873486L;
	
	private AttachInfoUserResponseType data;

	public AttachInfoUserResponseType getData() {
		return data;
	}

	public void setData(AttachInfoUserResponseType data) {
		this.data = data;
	}

	public String toString(){
		return data.toString();
	}
}
