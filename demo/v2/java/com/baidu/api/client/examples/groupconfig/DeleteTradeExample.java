package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.DeleteSiteResponse;
import com.baidu.api.sem.nms.v2.DeleteTradeRequest;
import com.baidu.api.sem.nms.v2.DeleteTradeResponse;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.GroupSiteType;
import com.baidu.api.sem.nms.v2.GroupTradeType;

/**
 * ClassName: DeleteTradeExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class DeleteTradeExample {
	private GroupConfigService service;

	public DeleteTradeExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public DeleteTradeResponse deleteTrade() {
		// Prepare your parameters.
		DeleteTradeRequest parameters = new DeleteTradeRequest();
		GroupTradeType tradeConfig1 = new GroupTradeType();
		// Set your group id.
		tradeConfig1.setGroupId(228);
		// Set Trade for your group.
		tradeConfig1.setTrade(1);
		GroupTradeType tradeConfig2 = new GroupTradeType();
		// Set your group id.
		tradeConfig2.setGroupId(228);
		// Set site for your group.
		tradeConfig2.setTrade(2);
		parameters.getTrades().add(tradeConfig1);
		parameters.getTrades().add(tradeConfig2);

		// Invoke the method.
		DeleteTradeResponse ret = service.deleteTrade(parameters);
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
		DeleteTradeExample example = new DeleteTradeExample();
		example.deleteTrade();
	}
}
