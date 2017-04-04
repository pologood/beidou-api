/*******************************************************************************
 * CopyRight (c) 2000-2012 Baidu Online Network Technology (Beijing) Co., Ltd. All rights reserved.
 * Filename:    AccessLog.java
 * Creator:     <a href="mailto:xuxiaohu@baidu.com">Xu,Xiaohu</a>
 * Create-Date: 2012-12-24 下午12:26:31
 *******************************************************************************/
package com.baidu.beidou.api.external.util.vo;

/**
 * Used to store access log data
 * 
 * @author <a href="mailto:xuxiaohu@baidu.com">Xu,Xiaohu</a>
 * @version 2012-12-24 下午12:26:31
 */
public class AccessLog {

	private String interfaceName;
	private String methodName;
	private Object[] params;
	private Object serializeObj;
	private boolean isErrors;
	private long accessTime;

	public AccessLog(String interfaceName, String methodName, Object[] params,
			Object serializeObj, boolean isErrors, long accessTime) {
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.params = params;
		this.serializeObj = serializeObj;
		this.isErrors = isErrors;
		this.accessTime = accessTime;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object[] getParams() {
		return params;
	}

	public Object getSerializeObj() {
		return serializeObj;
	}

	public boolean hasErrors() {
		return isErrors;
	}

	public long getAccessTime() {
		return accessTime;
	}
}
