package com.baidu.beidou.api.internal.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

/**
 * ClassName: DataLogInterceptor <br>
 * Function: 记录API请求日志 <br>
 * 记录格式为：类路径 \t 方法 \t json格式化过调用参数<br>
 * 例如：<code>com.baidu.beidou.api.internal.business.exporter.NameService	getKeywordLiteral	[[{"keywordid":389950,"atomid":2419}]] </code>
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class DataLogInterceptor implements MethodInterceptor {
	private static final Log ACCESS_LOG = LogFactory.getLog("apiaccess");

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object[] params = invocation.getArguments();
		String interfaceName = invocation.getMethod().getDeclaringClass()
				.getName();
		StringBuilder accessLog = new StringBuilder();
		accessLog.append(interfaceName).append('\t').append(
				invocation.getMethod().getName()).append('\t').append(
				JSONUtil.serialize(params));
		ACCESS_LOG.info(accessLog.toString());
		return invocation.proceed();
	}

}
