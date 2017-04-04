package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeKeywordType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: AddExcludeKeywordRequest  <br>
 * Function: 添加排除关键词请求
 *
 * @author zhangxu
 * @date Aug 28, 2012
 */
public class AddExcludeKeywordRequest implements ApiRequest {

	private List<GroupExcludeKeywordType> excludeKeywords;

	public List<GroupExcludeKeywordType> getExcludeKeywords() {
		return excludeKeywords;
	}

	public void setExcludeKeywords(List<GroupExcludeKeywordType> excludeKeywords) {
		this.excludeKeywords = excludeKeywords;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(excludeKeywords)) {
			result = excludeKeywords.size();
		}
		
		return result;
	}
	

}
