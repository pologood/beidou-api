/**
 * beidou-core-535#com.baidu.beidou.test.common.AbstractTestCase.java
 * 下午4:06:22 created by Darwin(Tianxin)
 */
package com.baidu.beidou.test.common;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.baidu.beidou.util.ThreadContext;

/**
 * 
 * @author Darwin(Tianxin)
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-extend-test.xml" })
public abstract class AbstractTestCaseLegacy extends AbstractTransactionalJUnit4SpringContextTests  {

	static{
		ThreadContext.putUserId(1);
	}
	
	@Test
	public void test(){
	}
}
