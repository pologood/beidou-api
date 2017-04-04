package com.baidu.beidou.api.internal.user.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.user.exporter.UserService;
import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class UserServiceTest {

	@Test
	public void testHasPrivilege() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/UserService", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/beidou-api/api/UserService", "UTF-8", new ExceptionHandler());
		
		UserService exporter = ProxyFactory.createProxy(UserService.class, proxy);
		boolean result = exporter.hasPrivilege("23", 527299, 499);
		//378909[bjsf15],832979[ZHUYUN],527299[bjjh15]
		System.out.println("hasPrivilege:" + result);
	}
	
	@Test
    public void testGetUserByUserId() throws Exception{
        McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/UserService", "UTF-8", new ExceptionHandler());
        UserService exporter = ProxyFactory.createProxy(UserService.class, proxy);
        UserServResult<Visitor> result = exporter.getUserByUserId(832979, true, "123");
        //378909[bjsf15],832979[ZHUYUN],527299[bjjh15]
        System.out.println("hasPrivilege:" + result);
    }
	
}
