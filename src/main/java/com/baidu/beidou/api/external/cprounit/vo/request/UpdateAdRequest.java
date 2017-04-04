package com.baidu.beidou.api.external.cprounit.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class UpdateAdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 5120104444262610275L;
	
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
