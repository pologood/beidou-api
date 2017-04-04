package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.cache.ThruputControlCache;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.constant.ThruputControlConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.beidou.util.SessionHolder;

public class ThruputControlInterceptor implements MethodInterceptor {
	private static final Log LOG = LogFactory.getLog(ThruputControlInterceptor.class);
	private static final Log thruputControlLog = LogFactory.getLog("thruputControlLog");

	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 获取调用接口方法名称
		String funtionName = invocation.getMethod().getName();
		
		// 验证是否包含参数
		Object[] params = invocation.getArguments();
		Object object = (Object) params[0];
		int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(object);
		int type = typeAndUser[0];
		int opUser = typeAndUser[1];
		int userId = typeAndUser[2];
		
		ApiRequest request = (ApiRequest)params[1];
		if(request == null){
			// if request is null then pass
			Object obj = invocation.proceed();
			return obj;
		}
		
		int dataSize = request.getDataSize();
		
		// 业务逻辑处理前：判断是否到达阈值，若到达阈值则wait一段时间（若wait了一定次数后，则该请求处理失败）
		try {
			boolean success = validate(userId, funtionName, dataSize);
			boolean isEverBanned = false;
			int waitTimes = 0;
			while (!success) {
				isEverBanned = true;
				if (waitTimes >= ThruputControlConstant.WAIT_TIME_WHEN_EXCEED_LIMIT) {
					// 因超过阈值而hold住的，需记录日志，以便分析问题
					// 日志格式：操作者 被操作者 请求方法名称 请求大小 应用总当前值 用户当前值 用户方法当前值
					StringBuffer sb = new StringBuffer();
					sb.append("\t").append(opUser);
					sb.append("\t").append(userId);
					sb.append("\t").append(funtionName);
					sb.append("\t").append(dataSize);
					int appDataSize = ThruputControlCache.getAppTotalOptDataSize();
					sb.append("\t").append(appDataSize);
					int userDataSize = ThruputControlCache.getUserOptDataSize(userId);
					sb.append("\t").append(userDataSize);
					int funcDataSize = ThruputControlCache.getUserOptDataSizeByFunctionId(userId, funtionName);
					sb.append("\t").append(funcDataSize);
					sb.append("\t").append(" wait timeout return error");
					thruputControlLog.info(sb.toString());
					return ApiResultBeanUtils.addApiError(type, null,
							GlobalErrorCode.TOO_FREQUENTLY.getValue(),
							GlobalErrorCode.TOO_FREQUENTLY.getMessage(),
							PositionConstant.SYS, null);
				}
				Thread.sleep(ThruputControlConstant.SLEEP_TIME_WHEN_EXCEED_LIMIT);
				waitTimes++;
				success = validate(userId, funtionName, dataSize);
			}
			if(isEverBanned == true){
				// 因超过阈值而hold住的，需记录日志，以便分析问题
				// 日志格式：操作者 被操作者 请求方法名称 请求大小 应用总当前值 用户当前值 用户方法当前值
				StringBuffer sb = new StringBuffer();
				sb.append("\t").append(opUser);
				sb.append("\t").append(userId);
				sb.append("\t").append(funtionName);
				sb.append("\t").append(dataSize);
				int appDataSize = ThruputControlCache.getAppTotalOptDataSize();
				sb.append("\t").append(appDataSize);
				int userDataSize = ThruputControlCache.getUserOptDataSize(userId);
				sb.append("\t").append(userDataSize);
				int funcDataSize = ThruputControlCache.getUserOptDataSizeByFunctionId(userId, funtionName);
				sb.append("\t").append(funcDataSize);
				sb.append("\t").append(" wait " + waitTimes*ThruputControlConstant.SLEEP_TIME_WHEN_EXCEED_LIMIT + " ms");
				thruputControlLog.info(sb.toString());
			}
			
			//LOG.info("before incr:" + ThruputControlCache.getLiteralUserOptDataSize(userId));
			ThruputControlCache.incr(userId, funtionName, dataSize);
			//LOG.info("after incr:" + ThruputControlCache.getLiteralUserOptDataSize(userId));
			SessionHolder.getSession().put(ApiConstant.KEY_THRUPUT_CONTROL, new Object());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.NOPARAM, null);
		}
		
		// 处理请求，执行业务逻辑处理
		Object obj = invocation.proceed();
		
		try {
			// 请求处理完毕后，需将该request的dataSize减掉
			Object flag = (Object) SessionHolder.getSession().get(ApiConstant.KEY_THRUPUT_CONTROL);
			if (flag != null) {
				//LOG.info("before dscr:" + ThruputControlCache.getLiteralUserOptDataSize(userId));
				ThruputControlCache.dscr(userId, funtionName, dataSize);
				//LOG.info("after dscr:" + ThruputControlCache.getLiteralUserOptDataSize(userId));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.NOPARAM, null);
		}
		
		return obj;
		
	}
	
	/**
	 * validate: 判断此次请求是否达到了阈值，共有3个层级的验证：应用总阈值、用户总阈值、用户方法阈值
	 * @version ThruputControlInterceptor
	 * @author genglei01
	 * @date 2012-4-23
	 */
	private boolean validate(Integer userId, String funcName, Integer dataSize) {
		// 判断是否达到整个应用程序的总操作阈值
		int appDataSize = ThruputControlCache.getAppTotalOptDataSize();
		if (appDataSize + dataSize > ThruputControlConstant.APP_THRESHOLD) {
			return false;
		}
		
		// 判断是否达到单个用户的总操作阈值
		int userDataSize = ThruputControlCache.getUserOptDataSize(userId);
		if (userDataSize + dataSize > ThruputControlConstant.USER_THRESHOLD) {
			return false;
		}
		
		// 判断是否达到单个用户单个方法的操作阈值
		int funcDataSize = ThruputControlCache.getUserOptDataSizeByFunctionId(userId, funcName);
		Integer funcDataSizeThreshold = ThruputControlConstant.FUNCTION_THRESHOLD.get(funcName);
		if (funcDataSizeThreshold != null
				&& funcDataSize + dataSize > funcDataSizeThreshold) {
			return false;
		}
		
		return true;
	}
}
