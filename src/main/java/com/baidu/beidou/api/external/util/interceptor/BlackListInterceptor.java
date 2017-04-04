package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.cache.ApiBlacklistCache;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;

/**
 * 
 * ClassName: BlackListInterceptor  <br>
 * Function: 黑名单拦截器，处于黑名单中的dataUser禁止其进行api操作
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class BlackListInterceptor implements MethodInterceptor {

	private static final Log LOG = LogFactory.getLog(BlackListInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result;
		Object[] params = invocation.getArguments();

		Object obj = (Object) params[0];
		int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(obj);
		int type = typeAndUser[0];
		int datauser = typeAndUser[2];

		if(ApiBlacklistCache.BLACKLIST_USERID_SET != null && ApiBlacklistCache.BLACKLIST_USERID_SET.contains(datauser)){
			LOG.info("userid=[" + datauser + "] found in blacklist, so forbidden to access!");
			result = ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.SYS, null);
			return result;
		}
		return invocation.proceed();
	}

}
