package com.baidu.beidou.api.external.code.vo.request;

import java.io.Serializable;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetAllCategoryRequest
 * Function: 请求空对象
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public class GetAllCategoryRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -2738455195459308781L;
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
}
