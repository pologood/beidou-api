
package com.baidu.beidou.api.internal.dsp.rpc;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.dsp.exporter.DspPeopleService;
import com.baidu.beidou.api.internal.dsp.vo.DspPeopleCodeResult;
import com.baidu.beidou.api.internal.dsp.vo.DspPeoplePidResult;
import com.baidu.beidou.api.internal.dsp.vo.PeopleResult;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;

@Ignore
public class DspPeopleServiceTest {

	@Test
	@Ignore
	public void testGetDspPeopleCode() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/DspPeopleService", "UTF-8",
				new ExceptionHandler());

		DspPeopleService exporter = ProxyFactory.createProxy(DspPeopleService.class, proxy);

		Integer userId = 499;
		Integer type = 1;

		

		DspPeopleCodeResult result = exporter.getDspPeopleCode(userId,type);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());
		System.out.println("code:" + result.getCode());
		System.out.println("jsid:" + result.getJsid());

	}
	
	@Test
	public void testGetDspPeoplePid() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://m1-beidou-api00.m1:8080/api/DspPeopleService", "UTF-8",
				new ExceptionHandler());

		DspPeopleService exporter = ProxyFactory.createProxy(DspPeopleService.class, proxy);

		Integer userId = 19;

		

		DspPeoplePidResult result = exporter.getDspPeoplePid(userId);

		System.out.println("status:" + result.getStatus());
		System.out.println("errMsg:" + result.getErrorMsg());
		System.out.println("pid:" + result.getPid());

	}
	
	@Test
	@Ignore
	public void testGetPeoples() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/DspPeopleService", "UTF-8",
				new ExceptionHandler());
		
		DspPeopleService exporter = ProxyFactory.createProxy(DspPeopleService.class, proxy);
		
		Integer userId = 18;
		PeopleResult result = exporter.getPeoples(userId);
		System.out.println(result.getPeopleList().size());
		Assert.assertTrue(result.getPeopleList().size() >= 0);
	}
}
