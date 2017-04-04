package com.baidu.beidou.api.external.cproplan.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetCampaignIdRequest implements ApiRequest {
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
}
