package com.baidu.beidou.api.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baidu.beidou.api.internal.fcindex.service.SimpleUserInfoMgr;

/**
 * 
 * ClassName: ServiceLocator Function: Spring注入服务定位器
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 19, 2011
 */
public class ServiceLocator {

	private static ServiceLocator locator = null;

	private BeanFactory factory = null;

	private ServiceLocator() {
		String[] fn = new String[] { "applicationContext.xml", "applicationContext-extend-test.xml" };
		factory = new ClassPathXmlApplicationContext(fn);
	}

	public void setFactory(BeanFactory factory) {
		this.factory = factory;
	}

	public static ServiceLocator getInstance() {
		if (locator == null) {
			locator = new ServiceLocator();
		}
		return locator;
	}

	public SimpleUserInfoMgr getSimpleUserInfoMgr() {
		return (SimpleUserInfoMgr) factory.getBean("simpleUserInfoMgr");
	}
	
	public Object getBeanByName(String beanName) {
		return factory.getBean(beanName);
	}
	
}
