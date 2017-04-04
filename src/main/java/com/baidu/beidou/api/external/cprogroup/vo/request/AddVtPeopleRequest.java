package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupVtItemType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: AddVtPeopleRequest
 * Function: 添加人群请求
 *
 * @author Baidu API Team
 * @date 2012-3-29
 */
public class AddVtPeopleRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 616060983040911335L;
	
	private GroupVtItemType[] vtPeoples;	// 推广组人群设置数组

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(vtPeoples)) {
			result = vtPeoples.length;
		}
		
		return result;
	}
	
	public GroupVtItemType[] getVtPeoples() {
		return vtPeoples;
	}

	public void setVtPeoples(GroupVtItemType[] vtPeoples) {
		this.vtPeoples = vtPeoples;
	}
}
