package com.baidu.beidou.api.external.tool.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetAdInfoRequest  <br>
 * Function: 获取创意其他信息请求头
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class GetAdInfoRequest implements ApiRequest{

	private long[] adIds;

	public long[] getAdIds() {
		return adIds;
	}

	public void setAdIds(long[] adIds) {
		this.adIds = adIds;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(adIds)) {
			result = adIds.length;
		}
		
		return result;
	}
}
