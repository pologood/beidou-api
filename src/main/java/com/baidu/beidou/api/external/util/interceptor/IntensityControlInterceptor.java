package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.cache.IntensityControlCache;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 
 * ClassName: IntensityControlInterceptor  <br>
 * Function: 负荷控制拦截器，对于特定的方法调用，主动睡眠线程一定时间，防止对后台数据库压力过大
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class IntensityControlInterceptor implements MethodInterceptor {

	private static final Log LOG = LogFactory.getLog(IntensityControlInterceptor.class);

	@SuppressWarnings("rawtypes")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String funtionName = invocation.getMethod().getName();
		Object obj = invocation.proceed();
		if (obj instanceof ApiResult) {
			ApiResult result = (ApiResult)obj; //与ApiResult耦合
			if(result.getErrors() == null){
				if(IntensityControlCache.FUNCTION_SLEEPTIME_DICT.containsKey(funtionName)){
					int sleepTime = IntensityControlCache.FUNCTION_SLEEPTIME_DICT.get(funtionName);
					LOG.info("funtionName=[" + funtionName + "] will hold on for " + sleepTime + " milliseconds");
					Thread.sleep(sleepTime);
				}
			} 
		} else {
			BaseResponse result = (BaseResponse)obj; //与BaseResponse耦合
			if(result.getErrors() == null){
				if(IntensityControlCache.FUNCTION_SLEEPTIME_DICT.containsKey(funtionName)){
					int sleepTime = IntensityControlCache.FUNCTION_SLEEPTIME_DICT.get(funtionName);
					LOG.info("funtionName=[" + funtionName + "] will hold on for " + sleepTime + " milliseconds");
					Thread.sleep(sleepTime);
				}
			} 
		}
		
		return obj;
	}
	
}
