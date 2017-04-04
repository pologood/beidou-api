package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GetTradeSitePriceRequest;
import com.baidu.api.sem.nms.v2.GetTradeSitePriceResponse;
import com.baidu.api.sem.nms.v2.GroupConfigService;

/**
 * ClassName: GetTradeSitePriceExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class GetTradeSitePriceExample {
	private GroupConfigService service;

	public GetTradeSitePriceExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public GetTradeSitePriceResponse getTradeSitePrice(long[] groupIds) {
		// Prepare your parameters.
		GetTradeSitePriceRequest parameters = new GetTradeSitePriceRequest();
		for (long groupId : groupIds) {
			parameters.getGroupIds().add(groupId);
		}
		// Invoke the method.
		GetTradeSitePriceResponse ret = service.getTradeSitePrice(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getTradeSitePrices()));
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
		GetTradeSitePriceExample example = new GetTradeSitePriceExample();
		long[] groupIds = new long[] { 2166146, 2166147, 2166149 };
		example.getTradeSitePrice(groupIds);
	}
}
