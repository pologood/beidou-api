package com.baidu.beidou.api.external.site.vo.request;

import com.baidu.beidou.api.external.site.vo.SiteFileRequestType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetSiteFileIdRequest implements ApiRequest {

	private SiteFileRequestType siteFileRequestType;

	public SiteFileRequestType getSiteFileRequestType() {
		return siteFileRequestType;
	}

	public void setSiteFileRequestType(SiteFileRequestType siteFileRequestType) {
		this.siteFileRequestType = siteFileRequestType;
	}
	
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

}
