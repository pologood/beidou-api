package com.baidu.api.client.examples.group;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AddGroupRequest;
import com.baidu.api.sem.nms.v2.AddGroupResponse;
import com.baidu.api.sem.nms.v2.GroupService;
import com.baidu.api.sem.nms.v2.GroupType;

/**
 * 
 * ClassName: AddGroupExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class AddGroupExample {

	private GroupService service;

	public AddGroupExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupService.class);
	}

	public AddGroupResponse addGroup() {
		// Prepare your parameters.
		AddGroupRequest parameters = new AddGroupRequest();
		GroupType group = new GroupType();
		group.setCampaignId(757456l);
		group.setGroupName("TestGroup");
		group.setPrice(10);
		group.setStatus(0);
		group.setType(1);
		parameters.getGroupTypes().add(group);
		// Invoke the method.
		AddGroupResponse ret = service.addGroup(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getGroupTypes()));
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
		AddGroupExample example = new AddGroupExample();
		example.addGroup();
	}

}
