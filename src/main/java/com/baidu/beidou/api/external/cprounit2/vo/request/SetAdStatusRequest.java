package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprounit2.vo.StatusType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class SetAdStatusRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -5112612289146874138L;
	
	private StatusType[] statusTypes;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(statusTypes)) {
			result = statusTypes.length;
		}
		
		return result;
	}
	
	public StatusType[] getStatusTypes() {
		return statusTypes;
	}

	public void setStatusTypes(StatusType[] statusTypes) {
		this.statusTypes = statusTypes;
	}
}
