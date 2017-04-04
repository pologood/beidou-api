package com.baidu.beidou.api.external.util;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.beidou.api.external.util.config.ThruputControlConfigService;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.stat.dao.StatDAO2;

public class SitekvDaoImplTest {

	private static StatDAO2 exporter;

	@BeforeClass
	public static void contextInitialized() {
		String beanName = "statDAO2";
		exporter = (StatDAO2) (ServiceLocator.getInstance().getBeanByName(beanName));
		ThruputControlConfigService thruputControlConfigService = (ThruputControlConfigService) ServiceLocator.getInstance().getBeanByName("thruputControlConfigService");
		thruputControlConfigService.loadConfig();
	}

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
	public void testSitekv() throws Exception {
		
		List<BigInteger> signs = new ArrayList<BigInteger>();
		signs.add(new BigInteger("1231242341232"));
		
		Set<BigInteger> set = new HashSet<BigInteger>(signs);
		
		Date start = new Date();
		Map<BigInteger, String> result = exporter.querySiteValue(set);
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		System.out.println(">>> use " + time + " ms to retreive " + result.size() + " keys");

		for (BigInteger sign : result.keySet()) {
			String literal = result.get(sign);
			System.out.println("sign:" + sign + "\tliteral:" + literal);
		}
		
	}
}
