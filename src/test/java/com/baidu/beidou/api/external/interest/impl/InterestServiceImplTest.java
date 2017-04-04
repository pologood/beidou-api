package com.baidu.beidou.api.external.interest.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.interest.exporter.InterestService;
import com.baidu.beidou.api.external.interest.vo.CustomInterestType;
import com.baidu.beidou.api.external.interest.vo.InterestType;
import com.baidu.beidou.api.external.interest.vo.request.GetAllCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetInterestRequest;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class InterestServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private InterestService interestService;

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
	public void testGetAllInterest() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetInterestRequest request = new GetInterestRequest();
		ApiResult<InterestType> result  = interestService.getInterest(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(77));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		InterestType itType = (InterestType)result.getData().get(1);
		assertThat(itType.getInterestId(), is(602));
		assertThat(itType.getInterestName(), is("白领"));
		assertThat(itType.getParentId(), is(0));
	}
	
	@Test
	public void testGetAllCustomInterest() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetAllCustomInterestRequest request = new GetAllCustomInterestRequest();
		ApiResult<CustomInterestType> result  = interestService.getAllCustomInterest(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(10));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
		// 验证结果
		CustomInterestType itType = (CustomInterestType)result.getData().get(8);
		assertThat(itType.getCustomItId(), is(100002));
		assertThat(itType.getCustomItName(), is("学生"));
		assertThat(itType.getCustomItCollections().length, is(3));
		assertThat(itType.getCustomItCollections()[0].getInterests().length, is(3));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getInterestId(), is(13));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getInterestName(), is("影视"));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getParentId(), is(0));
	}
	
	@Test
	public void testGetGetCustomInterest() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetCustomInterestRequest request = new GetCustomInterestRequest();
		request.setCustomItIds(new int[]{100001, 100002, 100003});
		ApiResult<CustomInterestType> result  = interestService.getCustomInterest(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(3));
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		// 验证结果
		CustomInterestType itType = (CustomInterestType)result.getData().get(1);
		assertThat(itType.getCustomItId(), is(100002));
		assertThat(itType.getCustomItName(), is("学生"));
		assertThat(itType.getCustomItCollections().length, is(3));
		assertThat(itType.getCustomItCollections()[0].getInterests().length, is(3));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getInterestId(), is(13));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getInterestName(), is("影视"));
		assertThat(itType.getCustomItCollections()[0].getInterests()[1].getParentId(), is(0));
	}
	
	@Test
	public void testGetGetCustomInterestNegative() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetCustomInterestRequest request = new GetCustomInterestRequest();
		request.setCustomItIds(new int[]{100001, 100002, 9999});
		ApiResult<CustomInterestType> result  = interestService.getCustomInterest(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GlobalErrorCode.UNAUTHORIZATION.getValue()) );
		
	}
	
}
