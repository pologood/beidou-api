package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;
import org.apache.commons.lang.ArrayUtils;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetKeywordRequest  <br>
 * Function: 设置关键词匹配模式请求
 *
 * @author zhangxu
 * @date May 31, 2012
 */
public class SetKeywordRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 4052419512623570697L;
	
	private GroupKeywordItemType[] keywords;	// 推广组关键词数组
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(keywords)) {
			result = keywords.length;
		}
		
		return result;
	}

	public GroupKeywordItemType[] getKeywords() {
		return keywords;
	}

	public void setKeywords(GroupKeywordItemType[] keywords) {
		this.keywords = keywords;
	}

	
}
