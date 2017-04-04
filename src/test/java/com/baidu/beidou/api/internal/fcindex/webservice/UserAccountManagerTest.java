package com.baidu.beidou.api.internal.fcindex.webservice;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Ignore;
@Ignore
public class UserAccountManagerTest {


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//String endpoint = "http://127.0.0.1:8080/beidou-api/services/user/account";
		String endpoint = "http://10.81.31.95:8230/beidou-api/services/user/account";
		int userid = 19;
		Service service = new Service();
		Call call = (Call)service.createCall();
		call.setTargetEndpointAddress(new URL(endpoint));
		call.setOperationName(new QName(endpoint,"getUserBudget"));
		int result = (Integer)(call.invoke(new Object[]{userid}));
		System.out.println(result);
	}
}
