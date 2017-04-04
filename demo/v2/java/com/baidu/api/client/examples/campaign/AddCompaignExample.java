package com.baidu.api.client.examples.campaign;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.DateTimeUtil;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AddCampaignRequest;
import com.baidu.api.sem.nms.v2.AddCampaignResponse;
import com.baidu.api.sem.nms.v2.CampaignService;
import com.baidu.api.sem.nms.v2.CampaignType;

/**
 * 
 * ClassName: AddCompaignExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class AddCompaignExample {

	private CampaignService service;

	public AddCompaignExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(CampaignService.class);
	}

	public AddCampaignResponse addCampaign() {
		// Prepare your parameters.
		AddCampaignRequest parameters = new AddCampaignRequest();
		CampaignType campaign = new CampaignType();
		campaign.setCampaignName("TestCampaign");
		campaign.setStartDate(DateTimeUtil.getDate(2012, 2, 10));
		campaign.setEndDate(DateTimeUtil.getDate(2012, 3, 10));
		campaign.setBudget(100);
		campaign.setStatus(0);
		parameters.getCampaignTypes().add(campaign);
		// Invoke the method.
		AddCampaignResponse ret = service.addCampaign(parameters);
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
		AddCompaignExample example = new AddCompaignExample();
		example.addCampaign();
	}

}
