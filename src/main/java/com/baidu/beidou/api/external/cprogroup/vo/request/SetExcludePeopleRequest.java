package com.baidu.beidou.api.external.cprogroup.vo.request;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetExcludePeopleRequest  <br>
 * Function: 设置推广组排除人群请求
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class SetExcludePeopleRequest implements ApiRequest {

	private ExcludePeopleType excludePeople;
	
	public ExcludePeopleType getExcludePeople() {
		return excludePeople;
	}

	public void setExcludePeople(ExcludePeopleType excludePeople) {
		this.excludePeople = excludePeople;
	}

	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

}
