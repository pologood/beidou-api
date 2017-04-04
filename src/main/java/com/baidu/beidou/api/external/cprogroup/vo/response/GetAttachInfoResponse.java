package com.baidu.beidou.api.external.cprogroup.vo.response;

import java.io.Serializable;

import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;

public class GetAttachInfoResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GroupAttachInfoType[] data;

	public GroupAttachInfoType[] getData() {
		return data;
	}

	public void setData(GroupAttachInfoType[] data) {
		this.data = data;
	}
}
