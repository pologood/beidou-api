package com.baidu.beidou.api.external.code.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

import java.io.Serializable;

/**
 * ClassName: GetAllRegionRequest
 * Function: 请求空对象
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public class GetAllRegionRequest implements Serializable, ApiRequest {
    private String version;
	
	private static final long serialVersionUID = -1255566705459208781L;
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
