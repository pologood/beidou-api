package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: UpdateAdditionalGroupRequest  <br>
 * Function: 更新推广组核心状态位以及出价
 *
 * @author zhangxu
 * @date Nov 5, 2012
 */
public class UpdateAdditionalGroupRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -7185824394320693959L;
	
	private AdditionalGroupType[] groupTypes;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(groupTypes)) {
			result = groupTypes.length;
		}
		
		return result;
	}

	public AdditionalGroupType[] getGroupTypes() {
		return groupTypes;
	}

	public void setGroupTypes(AdditionalGroupType[] groupTypes) {
		this.groupTypes = groupTypes;
	}
}
