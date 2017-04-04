package com.baidu.beidou.api.external.site.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.baidu.beidou.api.external.site.service.SiteFileMgr;
import com.baidu.beidou.util.TaskLogUtils;

public class ApiSiteFileTask extends QuartzJobBean {
	
	private static final Log log = LogFactory.getLog(ApiSiteFileTask.class);
	
	private SiteFileMgr siteFileMgr;
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {	
		 refreshSite();
	}
	
	public void refreshSite() {
		TaskLogUtils.info("refresh site file begin");
		try{
			siteFileMgr.refresh();
			TaskLogUtils.info("refresh site file end");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public SiteFileMgr getSiteFileMgr() {
		return siteFileMgr;
	}

	public void setSiteFileMgr(SiteFileMgr siteFileMgr) {
		this.siteFileMgr = siteFileMgr;
	}

}
