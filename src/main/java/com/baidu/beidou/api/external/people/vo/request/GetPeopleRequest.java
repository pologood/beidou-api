package com.baidu.beidou.api.external.people.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetPeopleRequest  <br>
 * Function: 获取指定账户的所有的人群信息请求类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public class GetPeopleRequest implements ApiRequest{

	private long[] peopleIds;

	public long[] getPeopleIds() {
		return peopleIds;
	}

	public void setPeopleIds(long[] peopleIds) {
		this.peopleIds = peopleIds;
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
