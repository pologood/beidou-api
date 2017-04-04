package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class DeleteAdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 6203861743120384562L;
	
	private long[] adIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(adIds)) {
			result = adIds.length;
		}
		
		return result;
	}

	public long[] getAdIds() {
		return adIds;
	}

	public void setAdIds(long[] adIds) {
		this.adIds = adIds;
	}
}
