package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.service.UnionSiteMgr;
import com.baidu.ctclient.ITaskUsingErrorCode;

public class LoadShadowIdeaWhiteListCTTask implements ITaskUsingErrorCode {

	private static Log LOG = LogFactory.getLog(LoadShadowIdeaWhiteListCTTask.class);
	private UnionSiteMgr unionSiteMgr = null;
	
	public boolean execute() {
		unionSiteMgr.loadWhiteShadowIdeaList();
		return true;
	}

	public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
		this.unionSiteMgr = unionSiteMgr;
	}
}
