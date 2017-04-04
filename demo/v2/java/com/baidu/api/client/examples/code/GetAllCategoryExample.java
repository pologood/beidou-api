package com.baidu.api.client.examples.code;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.CodeService;
import com.baidu.api.sem.nms.v2.GetAllCategoryRequest;
import com.baidu.api.sem.nms.v2.GetAllCategoryResponse;

/**
 * 
 * ClassName: GetAllCategoryExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class GetAllCategoryExample {

	private CodeService service;

	public GetAllCategoryExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(CodeService.class);
	}

	public GetAllCategoryResponse getAllCategory() {
		// Prepare your parameters.
		GetAllCategoryRequest parameters = new GetAllCategoryRequest();
		// Invoke the method.
		GetAllCategoryResponse ret = service.getAllCategory(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getCategorys()));
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
		GetAllCategoryExample example = new GetAllCategoryExample();
		example.getAllCategory();
	}

}
