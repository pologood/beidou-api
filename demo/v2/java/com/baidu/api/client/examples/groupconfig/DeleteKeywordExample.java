package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.DeleteKeywordRequest;
import com.baidu.api.sem.nms.v2.DeleteKeywordResponse;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.GroupKeywordItemType;

/**
 * 
 * ClassName: DeleteKeywordExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class DeleteKeywordExample {

	private GroupConfigService service;

	public DeleteKeywordExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public DeleteKeywordResponse deleteKeyword() {
		// Prepare your parameters.
		DeleteKeywordRequest parameters = new DeleteKeywordRequest();
		GroupKeywordItemType type1 = new GroupKeywordItemType();
		type1.setGroupId(228);
		type1.setKeyword("北京");
		type1.setPattern(1);
		parameters.getKeywords().add(type1);
		// Invoke the method.
		DeleteKeywordResponse ret = service.deleteKeyword(parameters);
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
		DeleteKeywordExample example = new DeleteKeywordExample();
		example.deleteKeyword();
	}

}
