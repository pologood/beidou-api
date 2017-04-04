package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetKeywordRequest  <br>
 * Function: 获取关键词字面请求，根据关键词的keywordid获取其字面
 *
 * @author zhangxu
 * @date May 31, 2012
 */
public class GetKeywordRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -7713885162247626644L;
	
	private Long[] keywordIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(keywordIds)) {
			result = keywordIds.length;
		}
		
		return result;
	}

	public Long[] getKeywordIds() {
		return keywordIds;
	}

	public void setKeywordIds(Long[] keywordIds) {
		this.keywordIds = keywordIds;
	}

}
