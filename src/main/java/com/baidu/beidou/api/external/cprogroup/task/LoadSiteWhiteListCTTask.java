package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.service.UnionSiteMgr;
import com.baidu.ctclient.ITaskUsingErrorCode;

/**
 * ClassName: LoadSiteWhiteListCTTask
 * Function: 导入站点白名单CT任务，加载百度自主流量网站
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class LoadSiteWhiteListCTTask implements ITaskUsingErrorCode {

	private static Log LOG = LogFactory.getLog(LoadSiteWhiteListCTTask.class);

	private UnionSiteMgr unionSiteMgr = null;

	public boolean execute() {
		unionSiteMgr.loadWhiteSiteList();
		return true;
	}

	public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
		this.unionSiteMgr = unionSiteMgr;
	}

}
