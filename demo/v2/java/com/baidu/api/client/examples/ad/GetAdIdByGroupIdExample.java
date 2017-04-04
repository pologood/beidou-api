package com.baidu.api.client.examples.ad;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AdService;
import com.baidu.api.sem.nms.v2.GetAdIdByGroupIdRequest;
import com.baidu.api.sem.nms.v2.GetAdIdByGroupIdResponse;

/**
 * ClassName: GetAdIdByGroupIdExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-14
 */
public class GetAdIdByGroupIdExample {
	private AdService service;

	public GetAdIdByGroupIdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AdService.class);
	}

	public GetAdIdByGroupIdResponse getAdIdByGroupId(int groupId) {
		// Prepare your parameters.
		GetAdIdByGroupIdRequest parameters = new GetAdIdByGroupIdRequest();
		parameters.setGroupId(groupId);
		
		// Invoke the method.
		GetAdIdByGroupIdResponse ret = service.getAdIdByGroupId(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdIds()));
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
		GetAdIdByGroupIdExample example = new GetAdIdByGroupIdExample();
		int groupId = 2166165;
		example.getAdIdByGroupId(groupId);
	}
}
