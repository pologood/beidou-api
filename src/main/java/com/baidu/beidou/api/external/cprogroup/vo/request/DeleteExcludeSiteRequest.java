package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeSiteType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: DeleteExcludeSiteRequest
 * Function: 删除过滤网站请求
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class DeleteExcludeSiteRequest implements Serializable, ApiRequest {

	private static final long serialVersionUID = 7934948361678966622L;
	
	private GroupExcludeSiteType[] excludeSites;	// 过滤网站设置数组
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(excludeSites)) {
			result = excludeSites.length;
		}
		
		return result;
	}

	public GroupExcludeSiteType[] getExcludeSites() {
		return excludeSites;
	}

	public void setExcludeSites(GroupExcludeSiteType[] excludeSites) {
		this.excludeSites = excludeSites;
	}

}
