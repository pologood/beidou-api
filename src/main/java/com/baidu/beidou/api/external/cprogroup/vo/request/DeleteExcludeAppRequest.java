package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * 删除排除移动应用请求
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-6-7 上午11:45:43
 */
public class DeleteExcludeAppRequest implements ApiRequest {

	private List<GroupExcludeAppType> excludeApps;

	public List<GroupExcludeAppType> getExcludeApps() {
		return excludeApps;
	}

	public void setExcludeApps(List<GroupExcludeAppType> excludeApps) {
		this.excludeApps = excludeApps;
	}

	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(excludeApps)) {
			result = excludeApps.size();
		}
		
		return result;
	}

}
