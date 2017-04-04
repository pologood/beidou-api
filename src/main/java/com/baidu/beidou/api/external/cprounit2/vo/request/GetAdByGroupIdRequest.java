package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetAdByGroupIdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -6723735485604941829L;
	
	private long groupId;
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
}
