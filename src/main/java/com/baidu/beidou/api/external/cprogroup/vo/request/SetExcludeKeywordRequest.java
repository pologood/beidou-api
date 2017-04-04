package com.baidu.beidou.api.external.cprogroup.vo.request;

import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetExcludeKeywordRequest  <br>
 * Function: 设置排除关键词请求
 *
 * @author zhangxu
 * @date Aug 28, 2012
 */
public class SetExcludeKeywordRequest implements ApiRequest {
	
	private ExcludeKeywordType excludeKeyword;

	public ExcludeKeywordType getExcludeKeyword() {
		return excludeKeyword;
	}

	public void setExcludeKeyword(ExcludeKeywordType excludeKeyword) {
		this.excludeKeyword = excludeKeyword;
	}
	
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
