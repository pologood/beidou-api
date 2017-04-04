/*******************************************************************************
 * CopyRight (c) 2000-2012 Baidu Online Network Technology (Beijing) Co., Ltd. All rights reserved.
 * Filename:    AccessLogSupport.java
 * Creator:     <a href="mailto:xuxiaohu@baidu.com">Xu,Xiaohu</a>
 * Create-Date: 2012-12-24 上午10:57:46
 *******************************************************************************/
package com.baidu.beidou.api.external.util.interceptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.JsonUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.task.TaskThreadFactory;
import com.baidu.beidou.api.external.util.vo.AccessLog;

/**
 * Used to decouple access log from {@link AccessLogInterceptor}
 * 
 * @author <a href="mailto:xuxiaohu@baidu.com">Xu,Xiaohu</a>
 * @version 2012-12-24 上午10:57:46
 */
public class AccessLogSupport {

	private ExecutorService executorService;

	private Log logger;

	private ThreadFactory threadFactory;

	public AccessLogSupport(String logName) {
		logger = LogFactory.getLog(logName);
		threadFactory = new TaskThreadFactory("ApiAccessLog");
	}

	public void log(AccessLog log) throws Throwable {
		LogTask logTask = new LogTask(log);
		Thread newThread = threadFactory.newThread(logTask);
		executorService.submit(newThread);
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	private class LogTask implements Runnable {
		private AccessLog log;

		public LogTask(AccessLog log) {
			this.log = log;
		}

		public void run() {
			StringBuilder accessLog = new StringBuilder();
			accessLog.append(log.getInterfaceName()).append('\t')
					.append(log.getMethodName()).append('\t');
			accessLog.append(JsonUtils.toJson(log.getParams()))
					.append('\t')
					.append(JsonUtils.toJson(log.getSerializeObj()))
					.append('\t');
			
			if (log.hasErrors()) {
				accessLog.append(ApiConstant.API_STATUS_ERROR);
			} else {
				accessLog.append(ApiConstant.API_STATUS_OK);
			}
			accessLog.append('\t').append(log.getAccessTime());

			logger.info(accessLog.toString());
		}

	}
}
