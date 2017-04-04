package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupRtItemType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: DeleteRtRelationRequest
 * Function: 删除关联关系请求
 *
 * @author Baidu API Team
 * @date 2012-3-29
 */
public class DeleteRtRelationRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 8950859303342446790L;
	
	private GroupRtItemType[] rtRelations;	// 推广组回头客设置数组

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(rtRelations)) {
			result = rtRelations.length;
		}
		
		return result;
	}
	
	public GroupRtItemType[] getRtRelations() {
		return rtRelations;
	}

	public void setRtRelations(GroupRtItemType[] rtRelations) {
		this.rtRelations = rtRelations;
	}
}
