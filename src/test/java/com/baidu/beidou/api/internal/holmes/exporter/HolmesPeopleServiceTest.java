/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.exporter.HolmesPeopleServiceTest.java
 * 1:07:50 AM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.exporter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.internal.holmes.vo.AddHolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class HolmesPeopleServiceTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}
	
	@Resource
	private HolmesPeopleService apiHolmesPeopleService;

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
	@Rollback(true)
	public void testAddHolmesPeople() {
		HolmesPeopleType type = new HolmesPeopleType();
		type.setAlivedays(30);
		type.setHolmesPid(1l);
		type.setName("testHM人群2");
		
		AddHolmesPeopleResult result = apiHolmesPeopleService.addHolmesPeople(userId, type);
		
		System.out.println(result.getStatus());
		System.out.println(result.getPid());
		assertThat(result.getStatus(), is(0));
	}

}
