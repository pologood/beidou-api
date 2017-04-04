package com.baidu.api.client.examples.group;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupService;
import com.baidu.api.sem.nms.v2.GroupType;
import com.baidu.api.sem.nms.v2.UpdateGroupRequest;
import com.baidu.api.sem.nms.v2.UpdateGroupResponse;

/**
 * 
 * ClassName: UpdateGroupExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class UpdateGroupExample {

	private GroupService service;

	public UpdateGroupExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupService.class);
	}

	public UpdateGroupResponse updateGroup() {
		// Prepare your parameters.
		UpdateGroupRequest parameters = new UpdateGroupRequest();
		GroupType group = new GroupType();
		group.setCampaignId(757456l);
		group.setGroupId(2166164l);
		group.setGroupName("TestGroup");
		group.setPrice(200);
		group.setStatus(1);
		group.setType(1);
		parameters.getGroupTypes().add(group);
		// Invoke the method.
		UpdateGroupResponse ret = service.updateGroup(parameters);
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
		UpdateGroupExample example = new UpdateGroupExample();
		example.updateGroup();
	}

}
