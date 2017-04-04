package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.ExcludeSiteType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetExcludeSiteRequest
 * Function: 设置过滤网站请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetExcludeSiteRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -1582753335227040031L;
	
	private ExcludeSiteType excludeSite;	// 推广组过滤网站

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (excludeSite != null) {
			String[] excludeSites = excludeSite.getExcludeSite();
			if (!ArrayUtils.isEmpty(excludeSites)) {
				result = excludeSites.length;
			}
		}
		
		return result;
	}
	
	public ExcludeSiteType getExcludeSite() {
		return excludeSite;
	}

	public void setExcludeSite(ExcludeSiteType excludeSite) {
		this.excludeSite = excludeSite;
	}
}
