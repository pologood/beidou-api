package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AddSiteRequest;
import com.baidu.api.sem.nms.v2.AddSiteResponse;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.GroupSiteType;
import com.baidu.api.sem.nms.v2.SetSiteConfigResponse;
import com.baidu.api.sem.nms.v2.SiteConfigType;

/**
 * ClassName: AddSiteExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class AddSiteExample {
	private GroupConfigService service;

	public AddSiteExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public AddSiteResponse addSite() {
		// Prepare your parameters.
		AddSiteRequest parameters = new AddSiteRequest();
		GroupSiteType siteConfig1 = new GroupSiteType();
		// Set your group id.
		siteConfig1.setGroupId(228);
		// Set site for your group.
		siteConfig1.setSite("ifeng.com");
		GroupSiteType siteConfig2 = new GroupSiteType();
		// Set your group id.
		siteConfig2.setGroupId(228);
		// Set site for your group.
		siteConfig2.setSite("sina.com.cn");
		parameters.getSites().add(siteConfig1);
		parameters.getSites().add(siteConfig2);

		// Invoke the method.
		AddSiteResponse ret = service.addSite(parameters);
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
		AddSiteExample example = new AddSiteExample();
		example.addSite();
	}
}
