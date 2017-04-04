package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.SetSiteUrlRequest;
import com.baidu.api.sem.nms.v2.SetSiteUrlResponse;
import com.baidu.api.sem.nms.v2.SiteUrlItemType;
import com.baidu.api.sem.nms.v2.SiteUrlType;

public class SetSiteUrlExample {
	private GroupConfigService service;

	public SetSiteUrlExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetSiteUrlResponse setSiteUrl() {
		// Prepare your parameters.
		SetSiteUrlRequest parameters = new SetSiteUrlRequest();
		SiteUrlType siteUrlConfig = new SiteUrlType();
		// Set your group id.
		siteUrlConfig.setGroupId(2166147);
		// Set targeturl for your chosen site.
		SiteUrlItemType siteUrl = new SiteUrlItemType();
		siteUrl.setSiteUrl("autohome.com.cn");
		siteUrl.setTargetUrl("http://baidu.com/autohome.com.cn");
		siteUrlConfig.getSiteUrlList().add(siteUrl);
		parameters.setSiteUrl(siteUrlConfig);

		// Invoke the method.
		SetSiteUrlResponse ret = service.setSiteUrl(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getResponse()));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	/**
	 * @param args
	 * @throws Throwable 
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws Throwable {
		SetSiteUrlExample example = new SetSiteUrlExample();
		example.setSiteUrl();
	}
}
