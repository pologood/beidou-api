package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.baidu.beidou.cprogroup.service.UnionSiteMgr;
import com.baidu.beidou.util.LogUtils;

/**
 * ClassName: LoadRegInfotoMemery
 * Function: 负责地域信息的内存加载，不由CT调度，网站行业信息的加载
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class LoadRegInfotoMemery extends QuartzJobBean {

	private static Log log = LogFactory.getLog(LoadRegInfotoMemery.class);

	private int timeout;

	private UnionSiteMgr unionSiteMgr;

	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			this.reloadSiteTradeInfo();
			this.reloadRegInfo();
		} catch (Exception e) {
			String errorMessage = "exception happened in function executeInternal";
			LogUtils.fatal(log, errorMessage);
		}
	}

	/**
	 * reloadSiteTradeInfo: 载入新的的站点行业信息
	 * @version 2.0.0
	 * @author genglei01
	 * @date 2011-12-22
	 */
	public void reloadSiteTradeInfo() throws Exception {
		//加载内存
		unionSiteMgr.loadSiteTradeInfo();
	}

	/**
	 * reloadRegInfo: 加载地域信息，和网站行业js文件
	 * @version 2.0.0
	 * @author genglei01
	 * @date 2011-12-22
	 */
	public void reloadRegInfo() throws Exception {
		//加载内存
		unionSiteMgr.loadRegInfo();
	}

	public UnionSiteMgr getUnionSiteMgr() {
		return unionSiteMgr;
	}

	public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
		this.unionSiteMgr = unionSiteMgr;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
