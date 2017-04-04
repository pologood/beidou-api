package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupInterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: DeleteInterestInfoRequest  <br>
 * Function: 删除推广组受众兴趣请求头
 *
 * @author zhangxu
 * @date Jun 5, 2012
 */
public class DeleteInterestInfoRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 35560632146422380L;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(interests)) {
			result = interests.length;
		}
		
		return result;
	}
	
	private GroupInterestInfoType[] interests;

	public GroupInterestInfoType[] getInterests() {
		return interests;
	}

	public void setInterests(GroupInterestInfoType[] interests) {
		this.interests = interests;
	}
	
}
