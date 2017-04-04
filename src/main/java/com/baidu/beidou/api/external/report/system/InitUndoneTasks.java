package com.baidu.beidou.api.external.report.system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.facade.ApiReportFacade;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.util.HostnameConfig;

/**
 * 
 * ClassName: InitUndoneTasks  <br>
 * Function: 系统初始化时需要处理status为未处理和超时重试的任务，做到原子性
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 16, 2012
 */
public class InitUndoneTasks {

	private static final Log LOG = LogFactory.getLog(InitUndoneTasks.class);
	
	private ApiReportTaskMgr apiReportTaskMgr;
	
	private ApiReportFacade apiReportFacade;

	public void init(){
		if(ReportWebConstants.ENABLE_INIT_ATOMICITY){
			String hostname = HostnameConfig.getHOSTNAME();
			List<Integer> status = new ArrayList<Integer>();
			status.add(ApiReportTaskConstant.TASK_STATUS_NEW);
			status.add(ApiReportTaskConstant.TASK_STATUS_DOING);
			//status.add(ApiReportTaskConstant.TASK_STATUS_TIMEOUT_WITH_RETRY);
			List<ApiReportTask> undoneTasks = apiReportTaskMgr.findTasksByStatusAndMachieid(status, hostname);
			LOG.info("find " + undoneTasks.size() + " undone tasks");
			for(ApiReportTask undoneTask:undoneTasks){
				LOG.info("process undone task:" + undoneTask);
				apiReportFacade.addTask(undoneTask);
				LOG.info("put undone task into jms queue, taskid=[" + undoneTask.getId() + "]");
			}
		} else {
			LOG.warn("system init will not handle previous tasks that not put into JMS and timeout fail tasks");
		}
		
		// 如果下载文件夹不存在，则新建一个
		File file = new File(ReportWebConstants.DOWNLOAD_FILE_SAVE_PATH);
		if(!file.exists()){
			LOG.info("mkdir" + file.getAbsolutePath());
			file.mkdir();
		}
		
	}

	public ApiReportTaskMgr getApiReportTaskMgr() {
		return apiReportTaskMgr;
	}

	public void setApiReportTaskMgr(ApiReportTaskMgr apiReportTaskMgr) {
		this.apiReportTaskMgr = apiReportTaskMgr;
	}

	public ApiReportFacade getApiReportFacade() {
		return apiReportFacade;
	}

	public void setApiReportFacade(ApiReportFacade apiReportFacade) {
		this.apiReportFacade = apiReportFacade;
	}
	
}
