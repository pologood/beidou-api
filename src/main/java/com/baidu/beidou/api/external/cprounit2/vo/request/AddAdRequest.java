package com.baidu.beidou.api.external.cprounit2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprounit2.vo.AdType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class AddAdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 526343763977191197L;
	
	private AdType[] adTypes;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(adTypes)) {
			result = adTypes.length;
		}
		
		return result;
	}

	public AdType[] getAdTypes() {
		return adTypes;
	}

	public void setAdTypes(AdType[] adTypes) {
		this.adTypes = adTypes;
	}
}
