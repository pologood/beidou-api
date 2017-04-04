package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: GetGroupByGroupIdRequest
 * Function:  获取某些指定的推广组信息，需要groupIds
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GetGroupByGroupIdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -5341746453109877504L;
	
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
