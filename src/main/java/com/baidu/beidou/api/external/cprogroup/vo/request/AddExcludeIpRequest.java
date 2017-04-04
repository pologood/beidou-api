package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeIpType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: AddExcludeIpRequest
 * Function: 添加过滤IP请求
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class AddExcludeIpRequest implements Serializable, ApiRequest {

	private static final long serialVersionUID = 1173705735395824180L;
	
	private GroupExcludeIpType[] excludeIps;	// 过滤IP设置数组

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(excludeIps)) {
			result = excludeIps.length;
		}
		
		return result;
	}
	
	public GroupExcludeIpType[] getExcludeIps() {
		return excludeIps;
	}

	public void setExcludeIps(GroupExcludeIpType[] excludeIps) {
		this.excludeIps = excludeIps;
	}
}
