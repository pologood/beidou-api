package com.baidu.beidou.api.external.accountfile.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.constant.ApiAccountFileTaskConstant;
import com.baidu.beidou.api.external.accountfile.error.AccountFileErrorCode;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;

/**
 * 
 * ClassName: LoadControlInterceptor  <br>
 * Function: 作负载限制的拦截器，叫做throttle（节流阀）
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 16, 2012
 */
public class LoadControlInterceptor extends AbstractDataPrivilege{
	
	private static final Log LOG = LogFactory.getLog(LoadControlInterceptor.class);
	
	private UserMgr userMgr;
	
	private ApiAccountFileTaskMgr apiAccountFileTaskMgr;
	
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ApiResult<GetAccountFileIdResponse> result = null;
		try {
			// 如果开启节流阀，才进行单一用户并发控制
			if(AccountFileWebConstants.ENABLE_THROTTLE){
				result = new ApiResult<GetAccountFileIdResponse>();
				PaymentResult payment = new PaymentResult();
				payment.setTotal(1);
				result.setPayment(payment);
				
				Object[] params = invocation.getArguments();
				DataPrivilege user = (DataPrivilege) params[0];
				
				if (user == null) {
					result = ApiResultBeanUtils.addApiError(result,
							UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
									.getMessage(), null, null);
					return result;
				}
				
				User dataUser = userMgr.findUserBySFid(user.getDataUser());
				if (dataUser == null) {
					result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNAUTHORIZATION.getValue(),
							GlobalErrorCode.UNAUTHORIZATION.getMessage(),
							PositionConstant.USER, 
							null);
					return result;
				}
				
				// 根据userid，处理中状态查询任务数量,如果超过设置阈值则禁止其再提交请求，整体控制用户请求数量
				List<Integer> status = new ArrayList<Integer>();
				//status.add(ApiAccountFileTaskConstant.TASK_STATUS_NEW);
				status.add(ApiAccountFileTaskConstant.TASK_STATUS_DOING);
				List<ApiAccountFileTask> doingTasks = apiAccountFileTaskMgr.findByUserIdAndStatus(dataUser.getUserid(), status);
				if(doingTasks.size() >= AccountFileWebConstants.CONCURRENT_TASKS_PER_USER){
					LOG.warn("userid=[" + dataUser.getUserid() + "] request too frequently and system denied his requests");
					result = ApiResultBeanUtils.addApiError(result,
							AccountFileErrorCode.ACCOUNTFILE_FORBIDDEN_DUE_TO_LOADCONTROL.getValue(),
							AccountFileErrorCode.ACCOUNTFILE_FORBIDDEN_DUE_TO_LOADCONTROL.getMessage(),
							PositionConstant.USER, 
							null);
					return result;
				}
			}
			
			result = (ApiResult<GetAccountFileIdResponse>)(invocation.proceed());
			
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(null, GlobalErrorCode.SYSTEM_BUSY.getValue(), 
					GlobalErrorCode.SYSTEM_BUSY.getMessage(), PositionConstant.SYS,null);
		}
		return result;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public ApiAccountFileTaskMgr getApiAccountFileTaskMgr() {
		return apiAccountFileTaskMgr;
	}

	public void setApiAccountFileTaskMgr(ApiAccountFileTaskMgr apiAccountFileTaskMgr) {
		this.apiAccountFileTaskMgr = apiAccountFileTaskMgr;
	}
	

}
