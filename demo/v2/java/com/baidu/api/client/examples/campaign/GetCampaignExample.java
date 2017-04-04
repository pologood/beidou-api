package com.baidu.api.client.examples.campaign;

import java.rmi.RemoteException;
import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.CampaignService;
import com.baidu.api.sem.nms.v2.GetCampaignRequest;
import com.baidu.api.sem.nms.v2.GetCampaignResponse;

/**
 * 
 * ClassName: CampaignServiceExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class GetCampaignExample {

	private CampaignService service;

	public GetCampaignExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(CampaignService.class);
	}

	public GetCampaignResponse getCampaign() {
		// Prepare your parameters.
		GetCampaignRequest parameters = new GetCampaignRequest();
		// Invoke the method.
		GetCampaignResponse ret = service.getCampaign(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getCampaignTypes()));
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
		GetCampaignExample example = new GetCampaignExample();
		example.getCampaign();
	}

}
