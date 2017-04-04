package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.service.UnionSiteMgr;
import com.baidu.ctclient.ITaskUsingErrorCode;

/**
 * ClassName: LoadFilmWhiteListCTTask
 * Function: 导入贴片白名单CT任务
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class LoadFilmWhiteListCTTask implements ITaskUsingErrorCode {

	private static Log LOG = LogFactory.getLog(LoadUserWhiteListCTTask.class);

	private UnionSiteMgr unionSiteMgr = null;

	public boolean execute() {
		unionSiteMgr.loadWhiteFilmList();
		return true;
	}

	public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
		this.unionSiteMgr = unionSiteMgr;
	}
}
