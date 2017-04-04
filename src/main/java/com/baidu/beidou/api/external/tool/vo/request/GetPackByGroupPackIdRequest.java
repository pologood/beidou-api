package com.baidu.beidou.api.external.tool.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetPackByGroupPackIdRequest  <br>
 * Function: 根据推广组-受众组合关联关系的gpid获取受众组合id和类型请求头
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class GetPackByGroupPackIdRequest implements ApiRequest{

	private long[] gpids;

	public long[] getGpids() {
		return gpids;
	}

	public void setGpids(long[] gpids) {
		this.gpids = gpids;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(gpids)) {
			result = gpids.length;
		}
		
		return result;
	}
}
