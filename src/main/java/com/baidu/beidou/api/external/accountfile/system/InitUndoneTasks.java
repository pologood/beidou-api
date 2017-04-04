package com.baidu.beidou.api.external.accountfile.system;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.constant.ApiAccountFileTaskConstant;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileFacade;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.api.external.util.HostnameConfig;

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
	
	private ApiAccountFileTaskMgr apiAccountFileTaskMgr;
	
	private AccountFileFacade accountFileFacade;

	public void init(){
		if(AccountFileWebConstants.ENABLE_INIT_ATOMICITY){
			String hostname = HostnameConfig.getHOSTNAME();
			List<ApiAccountFileTask> undoneTasks = apiAccountFileTaskMgr.findByStatusAndMachineId(ApiAccountFileTaskConstant.TASK_STATUS_NEW, hostname);
			LOG.info("find " + undoneTasks.size() + " undone tasks");
			for(ApiAccountFileTask undoneTask:undoneTasks){
				LOG.info("process undone task:" + undoneTask);
				accountFileFacade.addTask(undoneTask);
				LOG.info("put undone task into jms queue, taskid=[" + undoneTask.getId() + "]");
			}
		} else {
			LOG.warn("system init will not handle previous tasks that not put into JMS and timeout fail tasks");
		}
		
		// 如果下载文件夹不存在，则新建一个
		File fileDownload = new File(AccountFileWebConstants.DOWNLOAD_FILE_SAVE_PATH);
		if(!fileDownload.exists()){
			LOG.info("mkdir" + fileDownload.getAbsolutePath());
			fileDownload.mkdir();
		}
		File fileTmp = new File(AccountFileWebConstants.TMP_SAVE_PATH);
		if(!fileTmp.exists()){
			LOG.info("mkdir" + fileTmp.getAbsolutePath());
			fileTmp.mkdir();
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
