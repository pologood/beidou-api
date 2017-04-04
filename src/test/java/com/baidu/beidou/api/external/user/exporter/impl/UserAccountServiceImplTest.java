package com.baidu.beidou.api.external.user.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.annotation.Resource;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.account.constant.AccountConfig;
import com.baidu.beidou.account.service.MfcService;
import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.user.vo.AccountInfoType;
import com.baidu.beidou.api.external.user.vo.request.GetAccountInfoRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

//@RunWith(JMock.class)
@Ignore
public class UserAccountServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 480786;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private UserAccountServiceImpl userAccountService;
	
	private MfcService mfcService;
	
	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
		mfcService = context.mock(MfcService.class);
		userAccountService.setMfcService(mfcService);
	}

	@After
	public void afterEach() {
		context.assertIsSatisfied();
	}

	// -- getPlanNamebyId test cases --//
	@Test
	public void testGetAccountInfo() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		final Double balance = 87.04;
		context.checking(new Expectations() {
			{
				double[][] returnValue = new double[1][1];
				returnValue[0][0] = balance;
				oneOf(mfcService).getUserProductBalance(Arrays.asList(new Integer[]{userId}), 
						Arrays.asList(new Integer[]{AccountConfig.MFC_BEIDOU_PRODUCTID}), 
						AccountConfig.MFC_OPUID_DEFAULT);
				will(returnValue(returnValue));
			}
		});
		
		GetAccountInfoRequest request = new GetAccountInfoRequest();
		ApiResult<AccountInfoType> result  = userAccountService.getAccountInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		AccountInfoType accountInfo = (AccountInfoType)result.getData().get(0);
		assertThat(accountInfo.getUserid(), is(userId));
		assertThat(accountInfo.getUsername(), is("cprotest1"));
		assertThat(accountInfo.getBalance(), is(balance.floatValue()));
		
	}
}
