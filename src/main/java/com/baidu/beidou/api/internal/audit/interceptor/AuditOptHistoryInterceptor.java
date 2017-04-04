package com.baidu.beidou.api.internal.audit.interceptor;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.tool.facade.OptHistoryFacade;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;

/**
 * 
 * ClassName: OptHistoryInterceptor  <br>
 * Function: 记录审核历史操作记录
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 30, 2012
 */
public class AuditOptHistoryInterceptor implements MethodInterceptor {
	
	private static final Log LOG = LogFactory.getLog(AuditOptHistoryInterceptor.class);

	private OptHistoryFacade optHisFacade;
	
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		try {
			Object[] params = invocation.getArguments();
			Object obj = (Object) params[params.length - 1];
			
			int opid = 0;
			if (obj instanceof Integer) {
				opid = (Integer)obj;
			} else {
				opid = 0;
			}
			
			String ip = "API"; // 如果ip为空就为API
			
			int clientType = 0 ; //默认访问来源就是api=0
			
			List<OptContent> content = (List<OptContent>) SessionHolder.getSession().get(ApiConstant.KEY_OPTHISTORY_CONTENT);
			//记录操作历史
			optHisFacade.addOptHistory(opid, ip, clientType, content);
			
		} catch (Exception e) {
			LOG.error("fail to take down opt history info. " + e.getMessage(), e);
		}
		return result;
	}

	public OptHistoryFacade getOptHisFacade() {
		return optHisFacade;
	}

	public void setOptHisFacade(OptHistoryFacade optHisFacade) {
		this.optHisFacade = optHisFacade;
	}
	
}
