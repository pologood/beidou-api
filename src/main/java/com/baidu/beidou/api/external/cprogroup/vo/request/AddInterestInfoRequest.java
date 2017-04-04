package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupInterestInfoType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: AddInterestInfoRequest  <br>
 * Function: 添加推广组受众兴趣请求头
 *
 * @author zhangxu
 * @date Jun 5, 2012
 */
public class AddInterestInfoRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 35560622140425310L;

	private GroupInterestInfoType[] interests;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(interests)) {
			result = interests.length;
		}
		
		return result;
	}
	
	public GroupInterestInfoType[] getInterests() {
		return interests;
	}

	public void setInterests(GroupInterestInfoType[] interests) {
		this.interests = interests;
	}
	
}
