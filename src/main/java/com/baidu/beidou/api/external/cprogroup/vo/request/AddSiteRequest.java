package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupSiteType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: AddSiteRequest
 * Function: 添加投放网站请求
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class AddSiteRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 4550172000733823016L;
	
	private GroupSiteType[] sites;	// 投放网站设置数组

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(sites)) {
			result = sites.length;
		}
		
		return result;
	}
	
	public GroupSiteType[] getSites() {
		return sites;
	}

	public void setSites(GroupSiteType[] sites) {
		this.sites = sites;
	}
}
