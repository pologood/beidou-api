package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class ReplaceAdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 331967286457200498L;
	
	private long[] adIds; // 待修改创意id
	private long adId; // 要替换的创意内容的id
	
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
	public long getAdId() {
		return adId;
	}
	public void setAdId(long adId) {
		this.adId = adId;
	}
}
