package com.baidu.beidou.api.external.util.interceptor;

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
 * Function: 记录历史操作记录
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 30, 2012
 */
public class OptHistoryInterceptor implements MethodInterceptor {
	
	private static final Log LOG = LogFactory.getLog(OptHistoryInterceptor.class);

	private OptHistoryFacade optHisFacade;
	
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		try {
			Object[] params = invocation.getArguments();
			Object obj = (Object) params[0];
			
			int opid = 0;
			String ip = "";
			String tokenType = "";
			if (obj instanceof DataPrivilege) {
				DataPrivilege user = (DataPrivilege) params[0];
				ApiOption apioption = (ApiOption) params[2];
				opid = user.getOpUser();
				ip = apioption.getOptions().get(ApiConstant.CLIENT_IP_FROM_APIOPTION);
				tokenType = apioption.getOptions().get(ApiConstant.CLIENT_TYPE_FROM_APIOPTION);
			} else {
				BaseRequestUser user = (BaseRequestUser) params[0];
				BaseRequestOptions apioption = (BaseRequestOptions) params[2];
				opid = new Long(user.getOpUser()).intValue();
				ip = apioption.getOptions().get(ApiConstant.CLIENT_IP_FROM_APIOPTION);
				tokenType = apioption.getOptions().get(ApiConstant.CLIENT_TYPE_FROM_APIOPTION);
			}
			
			if(StringUtils.isEmpty(ip)){
				ip = "API"; // 如果ip为空就为API
			}
			
			int clientType = 0 ; //默认访问来源就是api=0
			try{
				int type = Integer.valueOf(tokenType);
				clientType = ApiConstant.DRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP.get(type);
			} catch(Exception e){
				LOG.warn("Error occurred converting DRApi to beidou client type ", e);
			}
			
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
