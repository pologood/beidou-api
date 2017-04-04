package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.baidu.beidou.api.external.util.vo.AccessLog;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 
 * ClassName: AccessLogInterceptor <br>
 * Function: DR-API记录调用日志 <br>
 * 
 * @author zhangxu
 * @Modifer XuXiaohu 2012-12-24 上午10:57:46
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 21, 2011
 */
public class AccessLogInterceptor implements MethodInterceptor {

	private AccessLogSupport accessLogSupport;

	@SuppressWarnings("rawtypes")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object obj = invocation.proceed();
		long end = System.currentTimeMillis();
		boolean isErrors = false;
		if (obj instanceof ApiResult) {
			ApiResult result = (ApiResult) obj; // 与ApiResult耦合
			if (result.getErrors() != null) {
				isErrors = true;
			}
		} else {
			BaseResponse result = (BaseResponse) obj; // 与BaseResponse耦合
			if (result.getErrors() != null) {
				isErrors = true;
			}
		}
		String interfaceName = invocation.getMethod().getDeclaringClass()
				.getName();
		String methodName = invocation.getMethod().getName();
		Object[] params = invocation.getArguments();

		AccessLog accessLog = new AccessLog(interfaceName, methodName, params,
				obj, isErrors, end - start);
		accessLogSupport.log(accessLog);
		return obj;
	}

	public void setAccessLogSupport(AccessLogSupport accessLogSupport) {
		this.accessLogSupport = accessLogSupport;
	}

}
