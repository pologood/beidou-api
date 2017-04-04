package com.baidu.api.client.examples.fc;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.FCService;
import com.baidu.api.sem.nms.v2.GetFCCampaignByFCCampaignIdRequest;
import com.baidu.api.sem.nms.v2.GetFCCampaignByFCCampaignIdResponse;
import com.baidu.api.sem.nms.v2.GetFCCampaignIdRequest;
import com.baidu.api.sem.nms.v2.GetFCCampaignIdResponse;
import com.baidu.api.sem.nms.v2.GetFCCampaignResponse;
import com.baidu.api.sem.nms.v2.GetFCUnitByFCCampaignIdRequest;
import com.baidu.api.sem.nms.v2.GetFCUnitByFCCampaignIdResponse;

/**
 * 
 * ClassName: GetFCUnitByFCCampaignIdExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Apr 20, 2012
 */
public class GetFCUnitByFCCampaignIdExample {

	private FCService service;

	public GetFCUnitByFCCampaignIdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(FCService.class);
	}

	public GetFCUnitByFCCampaignIdResponse getFCUnitByFCCampaignId(long id) {
		// Prepare your parameters.
		GetFCUnitByFCCampaignIdRequest parameters = new GetFCUnitByFCCampaignIdRequest();
		parameters.setCampaignId(id);
		// Invoke the method.
		GetFCUnitByFCCampaignIdResponse ret = service.getFCUnitByFCCampaignId(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getUnits()));
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
		GetFCUnitByFCCampaignIdExample example = new GetFCUnitByFCCampaignIdExample();
		long id = 338722l;
		example.getFCUnitByFCCampaignId(id);
	}

}
