package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GetExcludeSiteRequest;
import com.baidu.api.sem.nms.v2.GetExcludeSiteResponse;
import com.baidu.api.sem.nms.v2.GroupConfigService;

/**
 * ClassName: GetExcludeSiteExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class GetExcludeSiteExample {
	private GroupConfigService service;

	public GetExcludeSiteExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public GetExcludeSiteResponse getExcludeSite(long[] groupIds) {
		// Prepare your parameters.
		GetExcludeSiteRequest parameters = new GetExcludeSiteRequest();
		for (long groupId : groupIds) {
			parameters.getGroupIds().add(groupId);
		}
		// Invoke the method.
		GetExcludeSiteResponse ret = service.getExcludeSite(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getExcludeSites()));
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
		GetExcludeSiteExample example = new GetExcludeSiteExample();
		long[] groupIds = new long[] { 2166146, 2166147, 2166149 };
		example.getExcludeSite(groupIds);
	}
}
