package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class CopyAdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -6019961749513793879L;

	private long[] groupIds;
	
	private long[] adIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if ((!ArrayUtils.isEmpty(groupIds)) 
				&& (!ArrayUtils.isEmpty(adIds))) {
			result = groupIds.length * adIds.length;
		}
		
		return result;
	}

	public long[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(long[] groupIds) {
		this.groupIds = groupIds;
	}

	public long[] getAdIds() {
		return adIds;
	}

	public void setAdIds(long[] adIds) {
		this.adIds = adIds;
	}
}
