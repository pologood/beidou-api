package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.ExcludeIpType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetExcludeIpRequest
 * Function: 设置过滤IP请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetExcludeIpRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -8362308727961732691L;
	
	private ExcludeIpType excludeIp;	// 推广组过滤ip
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (excludeIp != null) {
			String[] excludeIps = excludeIp.getExcludeIp();
			if (!ArrayUtils.isEmpty(excludeIps)) {
				result = excludeIps.length;
			}
		}
		
		return result;
	}

	public ExcludeIpType getExcludeIp() {
		return excludeIp;
	}

	public void setExcludeIp(ExcludeIpType excludeIp) {
		this.excludeIp = excludeIp;
	}
}
