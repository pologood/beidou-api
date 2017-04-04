package com.baidu.beidou.api.external.accountfile.config.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.baidu.beidou.api.external.accountfile.config.AccountFileItem;
import com.baidu.beidou.api.external.accountfile.config.xml.Item;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.handler.Handler;

/**
 * 
 * ClassName: AccountFileItemConfigImpl  <br>
 * Function: 账户信息结构处理项
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class AccountFileItemImpl extends AccountFileItem {
	
	/** The spring context */
	private ApplicationContext springContext;
	
	/** 处理的handler */
	private String handlerBean;
	
	/** 配置文件的映射bean对象 */
	private Item delegate;
	
	/** 配置输出的文件头分隔符 */
	private String[] headers;
	
	public AccountFileItemImpl(Item item, ApplicationContext springContext){
		this.springContext = springContext;
		this.handlerBean = item.getHandlerBean();
		this.delegate = item;
		this.headers = StringUtils.split(item.getHeader(), AccountFileWebConstants.HEADERS_SEPERATOR);
	}
	
	/**
	 * 动态查找对应的handler
	 * @param 
	 * @return Handler
	 */
	public Handler getHandler(){
		Object obj = this.springContext.getBean(handlerBean);
		if(obj == null){
			throw new RuntimeException("spring context中找不到对应的bean：" + handlerBean);
		}
		
		if(obj instanceof Handler){
			return (Handler)obj;
		}
		
		throw new RuntimeException(handlerBean + "没有实现" + Handler.class.getName() + "接口");

	}
	
	// 以下通过代理获取
	public int getId(){
		return this.delegate.getId();
	}

	public String getName(){
		return this.delegate.getName();
	}

	public String getFilename(){
		return this.delegate.getFilename();
	}

	public String[] getHeader(){
		return this.headers;
	}

	// getter and setter
	public ApplicationContext getSpringContext() {
		return springContext;
	}

	public void setSpringContext(ApplicationContext springContext) {
		this.springContext = springContext;
	}

	public String getHandlerBean() {
		return handlerBean;
	}

	public void setHandlerBean(String handlerBean) {
		this.handlerBean = handlerBean;
	}

	public Item getDelegate() {
		return delegate;
	}

	public void setDelegate(Item delegate) {
		this.delegate = delegate;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	
	
}
