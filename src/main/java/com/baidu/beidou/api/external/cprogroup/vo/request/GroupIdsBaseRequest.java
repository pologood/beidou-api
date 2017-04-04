package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: GroupIdsBaseRequest
 * Function: 基本设置，包含groupIds，没实际意义
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GroupIdsBaseRequest implements Serializable, ApiRequest {

	private static final long serialVersionUID = -7378092376456463673L;

	private long[] groupIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(groupIds)) {
			result = groupIds.length;
		}
		
		return result;
	}

	public long[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(long[] groupIds) {
		this.groupIds = groupIds;
	}
}
