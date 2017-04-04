package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.ExcludeIpType;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.SetExcludeIpRequest;
import com.baidu.api.sem.nms.v2.SetExcludeIpResponse;

/**
 * ClassName: SetExcludeIpExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class SetExcludeIpExample {
	private GroupConfigService service;

	public SetExcludeIpExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetExcludeIpResponse setExcludeIp() {
		// Prepare your parameters.
		SetExcludeIpRequest parameters = new SetExcludeIpRequest();
		ExcludeIpType excludeIpConfig = new ExcludeIpType();
		// Set your group id.
		excludeIpConfig.setGroupId(2166147);
		// Set your exclude ip
		excludeIpConfig.getExcludeIp().add("111.111.111.111");
		excludeIpConfig.getExcludeIp().add("111.111.111.*");
		parameters.setExcludeIp(excludeIpConfig);

		// Invoke the method.
		SetExcludeIpResponse ret = service.setExcludeIp(parameters);
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
		SetExcludeIpExample example = new SetExcludeIpExample();
		example.setExcludeIp();
	}
}
