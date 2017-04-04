package com.baidu.beidou.api.external.interest.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetCustomInterestRequest  <br>
 * Function: 根据id获取用户自定义兴趣组合请求头
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class GetCustomInterestRequest implements ApiRequest{

	private int[] customItIds;

	public int[] getCustomItIds() {
		return customItIds;
	}

	public void setCustomItIds(int[] customItIds) {
		this.customItIds = customItIds;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(customItIds)) {
			result = customItIds.length;
		}
		
		return result;
	}
	
}
