package com.baidu.beidou.api.internal.fcindex.service;

import static org.hamcrest.Matchers.is;
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
import com.baidu.beidou.api.internal.fcindex.service.impl.SimpleUserInfoMgrImpl;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class SimpleUserInfoMgrImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 19;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private SimpleUserInfoMgrImpl simpleUserInfoMgr;

	private MfcService mfcService;

	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
		mfcService = context.mock(MfcService.class);
		simpleUserInfoMgr.setMfcService(mfcService);
	}

	@After
	public void afterEach() {
		context.assertIsSatisfied();
	}
	
	@Test
	public void testGetBalanceInfo() {
		final int userId = 499;
		context.checking(new Expectations() {
			{
				double[][] returnValue = new double[1][1];
				returnValue[0][0] = 82687.06;
				oneOf(mfcService).getUserProductBalance(Arrays.asList(new Integer[]{userId}), 
						Arrays.asList(new Integer[]{AccountConfig.MFC_BEIDOU_PRODUCTID}), 
						AccountConfig.MFC_OPUID_DEFAULT);
				will(returnValue(returnValue));
			}
		});
		int[] result = simpleUserInfoMgr.getBalanceInfo(userId);
		assertThat(result.length, is(2));
		System.out.println(result[0] + "|" + result[1]);
		assertThat(result[0], is(8268706));
		//assertThat(result[1], is(41));
		//assertThat(result[1], is(27));
	}
	
	//@Test
	public void testGetTotalValidBudget() {
		int userId = 19;
		int result = simpleUserInfoMgr.getTotalValidBudget(userId);
		assertThat(result, is(0));
		
		int userId2 = 499;
		int result2 = simpleUserInfoMgr.getTotalValidBudget(userId2);
		//assertThat(result2, is(1970));
		assertThat(result2, is(3070));
	}
	
	@Test
	public void testGetWeekAvgCost() {
		int userId = 19;
		int result = simpleUserInfoMgr.getWeekAvgCost(userId);
		assertThat(result, is(11004));
	}
	
}
