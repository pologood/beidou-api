package com.baidu.beidou.api.external.tool.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetFCKeywordByUnitIdsRequest  <br>
 * Function: 根据搜索推广单元id获取关键词请求头
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class GetFCKeywordByUnitIdsRequest implements ApiRequest{

	private long[] unitIds;

	public long[] getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(long[] unitIds) {
		this.unitIds = unitIds;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(unitIds)) {
			result = unitIds.length;
		}
		
		return result;
	}

}
