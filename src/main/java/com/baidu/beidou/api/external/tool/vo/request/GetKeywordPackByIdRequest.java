package com.baidu.beidou.api.external.tool.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetKeywordPackByIdRequest  <br>
 * Function: 根据id获取关键词组合配置请求头
 *
 * @author zhangxu
 * @date Aug 25, 2012
 */
public class GetKeywordPackByIdRequest implements ApiRequest {

	private int[] ids;
	
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(ids)) {
			result = ids.length;
		}
		
		return result;
	}
	
}
