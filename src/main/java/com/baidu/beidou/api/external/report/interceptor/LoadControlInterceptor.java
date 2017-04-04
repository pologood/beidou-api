package com.baidu.beidou.api.external.report.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.error.ReportErrorCode;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.util.HostnameConfig;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
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
	
	private ApiReportTaskMgr apiReportTaskMgr;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ApiResult<GetReportIdResponse> result = null;
		try {
			// 如果开启节流阀，才进行单一用户并发控制
			if(ReportWebConstants.ENABLE_THROTTLE){
				result = new ApiResult<GetReportIdResponse>();
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
				
				// 根据userid，处理中状态，machineid查询任务,如果超过设置阈值则禁止其再提交report请求
				// 注意这里配置的是单机的数量限制！！！
				String hostname = HostnameConfig.getHOSTNAME();
				List<Integer> status = new ArrayList<Integer>();
				status.add(ApiReportTaskConstant.TASK_STATUS_NEW);
				status.add(ApiReportTaskConstant.TASK_STATUS_DOING);
				List<ApiReportTask> doingTasks = apiReportTaskMgr.findTasksByUseridAndStatusAndMachieid(user.getDataUser(),status, hostname);
				if(doingTasks.size() >= ReportWebConstants.CONCURRENT_TASKS_PER_USER){
					LOG.warn("userid=[" + user.getDataUser() + "] request too frequently and system denied his requests");
					result = ApiResultBeanUtils.addApiError(result,
							ReportErrorCode.REPORT_FORBIDDEN_DUE_TO_LOADCONTROL.getValue(),
							ReportErrorCode.REPORT_FORBIDDEN_DUE_TO_LOADCONTROL.getMessage(),
							PositionConstant.USER, 
							null);
					return result;
				}
			}
			
			result = (ApiResult<GetReportIdResponse>)(invocation.proceed());
			
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

	public ApiReportTaskMgr getApiReportTaskMgr() {
		return apiReportTaskMgr;
	}

	public void setApiReportTaskMgr(ApiReportTaskMgr apiReportTaskMgr) {
		this.apiReportTaskMgr = apiReportTaskMgr;
	}
	

}
