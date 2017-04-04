package com.baidu.beidou.api.external.interest.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetAllCustomInterestRequest  <br>
 * Function: 获取用户所有自定义兴趣组合请求头
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class GetAllCustomInterestRequest implements ApiRequest{

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
	
}
