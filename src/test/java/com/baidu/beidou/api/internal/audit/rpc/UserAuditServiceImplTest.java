package com.baidu.beidou.api.internal.audit.rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.UserAuditService;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.UserInfo;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;

public class UserAuditServiceImplTest {

	@Test
	public void testGetUserInfo() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.23.244.60:8080/api/UserAuditService", "UTF-8", new ExceptionHandler());
		
		UserAuditService exporter = ProxyFactory.createProxy(UserAuditService.class, proxy);
		
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(499);
		userIds.add(1274564);
		
		AuditResult<UserInfo> result = exporter.getUserInfo(userIds);
		
		if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
			System.out.println(result);
		}
	}
		
}
