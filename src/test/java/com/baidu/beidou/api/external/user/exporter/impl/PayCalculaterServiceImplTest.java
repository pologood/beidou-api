package com.baidu.beidou.api.external.user.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.user.exporter.PayCalculaterService;
import com.baidu.beidou.api.external.user.vo.request.PaymentRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class PayCalculaterServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 18;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private PayCalculaterService payCalculaterService;

	private static DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(1,19);
		
	private static ApiOption apiOption = DarwinApiHelper.getApiOptions();
	
	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
	}

	@After
	public void afterEach() {
		context.assertIsSatisfied();
	}

	@Test
	public void testgGetUserCostLastWeek() {
		// 构造请求
		PaymentRequest request = new PaymentRequest();
		request.setStartDate("20111201");
		request.setEndDate("20111207");
		Integer[] userIds = new Integer[2];
		userIds[0] = 18;
		userIds[1] = 19;
		request.setUserIds(userIds);
		
		// 发送请求
		ApiResult<Double> result = payCalculaterService.getUserCostLastWeek(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);

		// 返回无error
		assertThat(result.getErrors(), nullValue());
		
		// 验证结果
		List<Double> data = result.getData();
		assertThat(data.size(), is(2));
		assertThat(data.get(0), is(1131.3899999999999));
		assertThat(data.get(1), is(4051.2));
	}
	
}
