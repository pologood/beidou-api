package com.baidu.api.client.examples.ad;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AdService;
import com.baidu.api.sem.nms.v2.GetAdByGroupIdRequest;
import com.baidu.api.sem.nms.v2.GetAdByGroupIdResponse;

/**
 * ClassName: GetAdByGroupIdExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-14
 */
public class GetAdByGroupIdExample {
	private AdService service;

	public GetAdByGroupIdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AdService.class);
	}

	public GetAdByGroupIdResponse getAdByGroupId(int groupId) {
		// Prepare your parameters.
		GetAdByGroupIdRequest parameters = new GetAdByGroupIdRequest();
		parameters.setGroupId(groupId);
		
		// Invoke the method.
		GetAdByGroupIdResponse ret = service.getAdByGroupId(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdTypes()));
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
		GetAdByGroupIdExample example = new GetAdByGroupIdExample();
		int groupId = 2166165;
		example.getAdByGroupId(groupId);
	}
}
