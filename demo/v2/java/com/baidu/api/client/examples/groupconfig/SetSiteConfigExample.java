package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.SetSiteConfigRequest;
import com.baidu.api.sem.nms.v2.SetSiteConfigResponse;
import com.baidu.api.sem.nms.v2.SiteConfigType;

/**
 * ClassName: SetSiteConfigExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class SetSiteConfigExample {
	private GroupConfigService service;

	public SetSiteConfigExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetSiteConfigResponse setSiteConfig() {
		// Prepare your parameters.
		SetSiteConfigRequest parameters = new SetSiteConfigRequest();
		SiteConfigType siteConfig = new SiteConfigType();
		// Set your group id.
		siteConfig.setGroupId(228);
		// Set if not all site.
		siteConfig.setAllSite(false);
		// Set site for your group.
		siteConfig.getSiteList().add("7k7k.com");
		siteConfig.getSiteList().add("autohome.com.cn");
		// Set category for your group.
		// 9: 教学及考试（一级行业），1001: 医学行业（二级行业）
		siteConfig.getCategoryList().add(9);
		siteConfig.getCategoryList().add(1001);
		parameters.setSiteConfig(siteConfig);

		// Invoke the method.
		SetSiteConfigResponse ret = service.setSiteConfig(parameters);
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
		SetSiteConfigExample example = new SetSiteConfigExample();
		example.setSiteConfig();
	}
}
