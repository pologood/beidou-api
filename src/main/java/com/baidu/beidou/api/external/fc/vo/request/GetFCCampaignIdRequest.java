package com.baidu.beidou.api.external.fc.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetFCCampaignIdRequest  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
public class GetFCCampaignIdRequest implements ApiRequest{

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
