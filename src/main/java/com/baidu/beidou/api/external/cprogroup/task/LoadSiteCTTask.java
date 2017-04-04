package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.util.LogUtils;
import com.baidu.ctclient.ITaskUsingErrorCode;

/**
 * ClassName: LoadSiteCTTask
 * Function: 导入站点信息CT任务
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class LoadSiteCTTask implements ITaskUsingErrorCode {
	private static Log LOG = LogFactory.getLog(LoadSiteCTTask.class);
	private LoadSiteInfotoMemery task = null;
	
	public boolean execute() {
		try {
			task.reloadSiteInfo();
			return true;
		} catch (Exception e) {
			LogUtils.fatal(LOG, e.getMessage(), e);
			return false;
		}
		
	}
	
	public void setTask(LoadSiteInfotoMemery task) {
		this.task = task;
	}
}
