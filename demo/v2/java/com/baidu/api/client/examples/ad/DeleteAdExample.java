package com.baidu.api.client.examples.ad;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AdService;
import com.baidu.api.sem.nms.v2.DeleteAdRequest;
import com.baidu.api.sem.nms.v2.DeleteAdResponse;

/**
 * ClassName: DeleteAdExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-14
 */
public class DeleteAdExample {
	private AdService service;

	public DeleteAdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AdService.class);
	}

	public DeleteAdResponse deleteAd(long[] adIds) {
		// Prepare your parameters.
		DeleteAdRequest parameters = new DeleteAdRequest();
		for (long adId : adIds) {
			parameters.getAdIds().add(adId);
		}
		// Invoke the method.
		DeleteAdResponse ret = service.deleteAd(parameters);
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
		DeleteAdExample example = new DeleteAdExample();
		long[] adIds = new long[] { 9283681, 9283679, 9283678 };
		example.deleteAd(adIds);
	}
}
