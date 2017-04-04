package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 获取附加创意信息
 * @author caichao
 *
 */
public class GetAttachInfosRequest  implements ApiRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4067615311916851855L;
	
	private List<Integer> groupIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(groupIds)) {
			result = groupIds.size();
		}
		
		return result;
	}

	public List<Integer> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}
	
}
