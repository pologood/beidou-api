/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.rpc.HolmesPeopleServiceRpcTest.java
 * 1:07:17 AM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.holmes.exporter.HolmesPeopleService;
import com.baidu.beidou.api.internal.holmes.vo.AddHolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class HolmesPeopleServiceTest {

	@Test
	public void testAddHolmesPeople() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/HolmesPeopleService", "UTF-8",
				new ExceptionHandler());

		HolmesPeopleService exporter = ProxyFactory.createProxy(HolmesPeopleService.class, proxy);

		Integer userId = 499;

		HolmesPeopleType type = new HolmesPeopleType();
		type.setAlivedays(60);
		type.setHolmesPid(104l);
		type.setName("testHM人群104");

		AddHolmesPeopleResult result = exporter.addHolmesPeople(userId, type);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());
		System.out.println("pid:" + result.getPid());

	}

	@Test
	public void testUpdateHolmesPeopleName() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/HolmesPeopleService", "UTF-8",
				new ExceptionHandler());

		HolmesPeopleService exporter = ProxyFactory.createProxy(HolmesPeopleService.class, proxy);

		Integer userId = 499;
		Long hpid = 104l;
		String name = "test1234";

		HolmesPeopleResult result = exporter.updateHolmesPeopleName(userId, hpid, name);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());

	}

	@Test
	public void testUpdateHolmesPeopleAlivedays() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/HolmesPeopleService", "UTF-8",
				new ExceptionHandler());

		HolmesPeopleService exporter = ProxyFactory.createProxy(HolmesPeopleService.class, proxy);

		Integer userId = 499;
		Long hpid = 104l;
		Integer alivedays = 120;

		HolmesPeopleResult result = exporter.updateHolmesPeopleAlivedays(userId, hpid, alivedays);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());

	}

	@Test
	public void testDeleteHolmesPeople() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/HolmesPeopleService", "UTF-8",
				new ExceptionHandler());

		HolmesPeopleService exporter = ProxyFactory.createProxy(HolmesPeopleService.class, proxy);

		Integer userId = 499;
		Long hpid = 104l;

		HolmesPeopleResult result = exporter.deleteHolmesPeople(userId, hpid);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());

	}

}
