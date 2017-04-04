package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: GetKeywordByWordIdRequest
 * Function: 获取关键词字面请求，根据关键词的keywordid获取其字面
 *
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-28
 */
public class GetKeywordByWordIdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -2263781126544138551L;
	
	private long[] wordIds;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(wordIds)) {
			result = wordIds.length;
		}
		
		return result;
	}

	public long[] getWordIds() {
		return wordIds;
	}

	public void setWordIds(long[] wordIds) {
		this.wordIds = wordIds;
	}
}
