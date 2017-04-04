package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.RegionItemType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetSiteUrlRequest
 * Function: 设置分网站点击URL请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetSiteUrlRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 8021165294968104840L;
	
	private SiteUrlType siteUrl;	// 分网站点击链接
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (siteUrl != null) {
			List<SiteUrlItemType> siteUrlList = siteUrl.getSiteUrlList();
			if (CollectionUtils.isNotEmpty(siteUrlList)) {
				result = siteUrlList.size();
			}
		}
		
		return result;
	}

	public SiteUrlType getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(SiteUrlType siteUrl) {
		this.siteUrl = siteUrl;
	}
}
