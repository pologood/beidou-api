package com.baidu.beidou.api.internal.fcindex;

import org.springframework.beans.factory.BeanFactory;

/**
 * 
 * ClassName: ServiceFinder  <br>
 * Function: 凤巢大首页专用服务查找器
 *
 * @author zhangxu
 * @date Feb 17, 2012
 */
public class ServiceFinder {

	private static ServiceFinder finder = null;

	private BeanFactory factory = null;

	private ServiceFinder() {
		//String[] fn = new String[] { "applicationContext.xml" };
		//factory = new ClassPathXmlApplicationContext(fn);
	}

	public void setFactory(BeanFactory factory) {
		this.factory = factory;
	}

	public static ServiceFinder getInstance() {
		if (finder == null) {
			finder = new ServiceFinder();
		}
		return finder;
	}
	
	public Object getBean(String beanName) {
		return factory.getBean(beanName);
	}
}
