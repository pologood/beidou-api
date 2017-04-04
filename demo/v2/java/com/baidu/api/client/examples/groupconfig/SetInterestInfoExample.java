package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.InterestInfoType;
import com.baidu.api.sem.nms.v2.KeywordType;
import com.baidu.api.sem.nms.v2.KtItemType;
import com.baidu.api.sem.nms.v2.SetInterestInfoRequest;
import com.baidu.api.sem.nms.v2.SetInterestInfoResponse;
import com.baidu.api.sem.nms.v2.SetTargetInfoResponse;

/**
 * 
 * ClassName: SetInterestInfoExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class SetInterestInfoExample {

	private GroupConfigService service;

	public SetInterestInfoExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetInterestInfoResponse setInterestInfo() {
		// Prepare your parameters.
		SetInterestInfoRequest parameters = new SetInterestInfoRequest();
		InterestInfoType type = new InterestInfoType();
		type.setEnable(true);
		type.setGroupId(228);
		type.getInterestIds().add(2);
		type.getInterestIds().add(5);
		type.getInterestIds().add(35);
		type.getExceptInterestIds().add(602);
		parameters.setInterestInfo(type);
		// Invoke the method.
		SetInterestInfoResponse ret = service.setInterestInfo(parameters);
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
		SetInterestInfoExample example = new SetInterestInfoExample();
		example.setInterestInfo();
	}

}
