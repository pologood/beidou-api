package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: AddGroupRequest
 * Function: 新增推广组请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class AddGroupRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 355606291407425519L;
	
	private GroupType[] groupTypes;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(groupTypes)) {
			result = groupTypes.length;
		}
		
		return result;
	}
	
	public GroupType[] getGroupTypes() {
		return groupTypes;
	}

	public void setGroupTypes(GroupType[] groupTypes) {
		this.groupTypes = groupTypes;
	}
}
