package com.baidu.beidou.api.external.util.config.monitor;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.baidu.beidou.api.external.util.config.ThruputControlConfigService;
import com.baidu.beidou.api.external.util.config.impl.ThruputControlConfigServiceImpl;

/**
 * 
 * ClassName: ThruputControlConfigMonitor  <br>
 * Function: 监控流量控制配置文件更新的监视器
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 20, 2012
 */
public class ThruputControlConfigMonitor extends QuartzJobBean {
	
	private static final Log log = LogFactory.getLog(ThruputControlConfigMonitor.class);
	
	private ThruputControlConfigService thruputControlConfigService;
	
	private static long modifiedTime = 0l;
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {	
		monitorConfFileUpdate();
	}
	
	/**
	 * 监控配置文件是否有更新，有则重新加载
	 */
	public void monitorConfFileUpdate() {
		try{
			File file = new File(ThruputControlConfigServiceImpl.class.getResource("/thruput-config.xml").getFile());
			long currentModifiedTime = file.lastModified();
			if(currentModifiedTime != modifiedTime){
				log.info("Detect ThruputControlConfig changed!");
				thruputControlConfigService.loadConfig();
				modifiedTime = currentModifiedTime;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 应用程序初始化的时候需要单独设置此起始监控修改时间
	 */
	public static void updateModifiedTime(){
		File file = new File(ThruputControlConfigServiceImpl.class.getResource("/thruput-config.xml").getFile());
		long currentModifiedTime = file.lastModified();
		modifiedTime = currentModifiedTime;
	}

	public ThruputControlConfigService getThruputControlConfigService() {
		return thruputControlConfigService;
	}

	public void setThruputControlConfigService(
			ThruputControlConfigService thruputControlConfigService) {
		this.thruputControlConfigService = thruputControlConfigService;
	}

}
