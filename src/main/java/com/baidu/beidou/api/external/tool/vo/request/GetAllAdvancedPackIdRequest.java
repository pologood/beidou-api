package com.baidu.beidou.api.external.tool.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetAllAdvancedPackIdRequest  <br>
 * Function: 获取所有高级组合id请求头
 *
 * @author zhangxu
 * @date Aug 25, 2012
 */
public class GetAllAdvancedPackIdRequest implements ApiRequest {

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
