package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: AddExcludePeopleRequest  <br>
 * Function: 添加推广组排除人群请求
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class AddExcludePeopleRequest implements ApiRequest {

	private List<GroupExcludePeopleType> excludePeoples;

	public List<GroupExcludePeopleType> getExcludePeoples() {
		return excludePeoples;
	}

	public void setExcludePeoples(List<GroupExcludePeopleType> excludePeoples) {
		this.excludePeoples = excludePeoples;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(excludePeoples)) {
			result = excludePeoples.size();
		}
		
		return result;
	}
	

}
