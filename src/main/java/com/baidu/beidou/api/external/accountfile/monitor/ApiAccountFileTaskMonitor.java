package com.baidu.beidou.api.external.accountfile.monitor;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.constant.ApiAccountFileTaskConstant;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileFacade;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.api.external.util.HostnameConfig;
import com.baidu.beidou.util.TaskLogUtils;

/**
 * 
 * ClassName: ApiAccountFileTaskMonitor  <br>
 * Function: 处理出错超时的任务监控类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 15, 2012
 */
public class ApiAccountFileTaskMonitor extends QuartzJobBean {
	
	private static final Log log = LogFactory.getLog(ApiAccountFileTaskMonitor.class);
	
	private ApiAccountFileTaskMgr apiAccountFileTaskMgr;
	
	private AccountFileFacade accountFileFacade;
	
	private int timeoutRetryTimeThreshold = AccountFileWebConstants.TASK_TIMEOUT_MINUTES;
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {	
		 monitorTimeoutTask();
	}
	
	/**
	 * 处理超时任务
	 */
	public void monitorTimeoutTask() {
		TaskLogUtils.info("monitor accountfile timeout tasks begin");
		Calendar time = null;
		try{
			String hostname = HostnameConfig.getHOSTNAME();
			time = Calendar.getInstance();
			time.add(Calendar.MINUTE, -timeoutRetryTimeThreshold);
			List<ApiAccountFileTask> timeoutTasks = apiAccountFileTaskMgr.findByStatusAndMachineIdAndModtime(ApiAccountFileTaskConstant.TASK_STATUS_DOING,hostname, time.getTime());
			if(CollectionUtils.isEmpty(timeoutTasks)){
				TaskLogUtils.info("no accountfile timeout task find");
				return;
			}
			TaskLogUtils.info("find " + timeoutTasks.size() + " timeout tasks");
			for(ApiAccountFileTask timeoutTask:timeoutTasks){
				TaskLogUtils.info("process timeout task:" + timeoutTask);
				// 对于超时任务直接标记为失败
				apiAccountFileTaskMgr.updateStatus(timeoutTask.getUserid(), timeoutTask.getFileid(), ApiAccountFileTaskConstant.TASK_STATUS_FAIL);
				TaskLogUtils.info("update task status to fail done,taskid=[" + timeoutTask + "]");
			}
			TaskLogUtils.info("monitor accountfile timeout tasks end");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public ApiAccountFileTaskMgr getApiAccountFileTaskMgr() {
		return apiAccountFileTaskMgr;
	}

	public void setApiAccountFileTaskMgr(ApiAccountFileTaskMgr apiAccountFileTaskMgr) {
		this.apiAccountFileTaskMgr = apiAccountFileTaskMgr;
	}

	public AccountFileFacade getAccountFileFacade() {
		return accountFileFacade;
	}

	public void setAccountFileFacade(AccountFileFacade accountFileFacade) {
		this.accountFileFacade = accountFileFacade;
	}
	

}
