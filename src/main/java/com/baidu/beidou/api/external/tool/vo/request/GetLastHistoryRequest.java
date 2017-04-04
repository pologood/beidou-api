package com.baidu.beidou.api.external.tool.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetLastHistoryRequest implements ApiRequest{

	@Override
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

}
