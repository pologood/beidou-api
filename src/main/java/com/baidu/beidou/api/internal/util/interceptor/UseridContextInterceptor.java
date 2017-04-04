package com.baidu.beidou.api.internal.util.interceptor;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.baidu.beidou.util.ThreadContext;

public class UseridContextInterceptor implements MethodInterceptor{
	private Map<String, Integer> methodNameMap;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String methodName = invocation.getMethod().getName();
		
		Integer index = methodNameMap.get(methodName);
		
		if(index != null){
			Object[] params = invocation.getArguments();
			Integer userId = (Integer)params[index];
			ThreadContext.putUserId(userId);
		}
		return invocation.proceed();
	}

	public Map<String, Integer> getMethodNameMap() {
		return methodNameMap;
	}

	public void setMethodNameMap(Map<String, Integer> methodNameMap) {
		this.methodNameMap = methodNameMap;
	}

	
}
