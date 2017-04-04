package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.util.LogUtils;
import com.baidu.ctclient.ITaskUsingErrorCode;

/**
 * ClassName: LoadSiteSizeCTTask
 * Function: 加载站点尺寸缓存
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class LoadSiteSizeCTTask implements ITaskUsingErrorCode {
	private static Log LOG = LogFactory.getLog(LoadSiteSizeCTTask.class);
	private LoadSiteInfotoMemery task = null;

	public boolean execute() {
		try {
			task.reloadSiteSizeInfo();
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
