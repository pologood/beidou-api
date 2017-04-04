package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.SiteConfigType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetSiteConfigRequest
 * Function: 设置投放网站及网站行业请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetSiteConfigRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -8151437320912863315L;
	
	private SiteConfigType siteConfig;	// 推广组投放网站
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

	public SiteConfigType getSiteConfig() {
		return siteConfig;
	}

	public void setSiteConfig(SiteConfigType siteConfig) {
		this.siteConfig = siteConfig;
	}
}
