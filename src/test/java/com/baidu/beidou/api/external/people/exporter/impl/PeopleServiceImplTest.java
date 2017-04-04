package com.baidu.beidou.api.external.people.exporter.impl;

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
import com.baidu.beidou.api.external.people.exporter.PeopleService;
import com.baidu.beidou.api.external.people.vo.PeopleType;
import com.baidu.beidou.api.external.people.vo.request.GetAllPeopleRequest;
import com.baidu.beidou.api.external.people.vo.request.GetPeopleRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class PeopleServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private PeopleService peopleService;
	
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
	public void testGetAllPeople() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetAllPeopleRequest request = new GetAllPeopleRequest();
		
		ApiResult<PeopleType> result  = peopleService.getAllPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(25));
		PeopleType people = result.getData().get(0);
		assertThat(people.getPid(), is(37l));
		assertThat(people.getName(), is("VTTest25"));
		assertThat(people.getCookieNum(), is(0l));
		assertThat(people.getAliveDays(), is(30));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
			
	}
	
	@Test
	public void testGetPeople() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetPeopleRequest request = new GetPeopleRequest();
		long[] pids = new long[]{17,18};
		request.setPeopleIds(pids);
		
		ApiResult<PeopleType> result  = peopleService.getPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getData().size(), is(2));
		assertThat(result.getErrors(), nullValue());
		PeopleType people = result.getData().get(0);
		assertThat(people.getPid(), is(17l));
		assertThat(people.getName(), is("似的放大点"));
		assertThat(people.getCookieNum(), is(0l));
		assertThat(people.getAliveDays(), is(30));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

	}
	
	@Test
	public void testGetPeopleNullPeopleIds() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetPeopleRequest request = new GetPeopleRequest();
		//long[] pids = new long[]{999,998,997};
		//request.setPeopleIds(pids);
		
		ApiResult<PeopleType> result  = peopleService.getPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getData(), nullValue());
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(0));
		assertThat(result.getPayment().getSuccess(), is(0));

	}
	
	@Test
	public void testGetPeopleNotAuthorized() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetPeopleRequest request = new GetPeopleRequest();
		long[] pids = new long[]{999,998,997};
		request.setPeopleIds(pids);
		
		ApiResult<PeopleType> result  = peopleService.getPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getData(), nullValue());
	}
	
}
