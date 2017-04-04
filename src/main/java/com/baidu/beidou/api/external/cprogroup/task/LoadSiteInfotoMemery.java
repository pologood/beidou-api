package com.baidu.beidou.api.external.cprogroup.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.service.UnionSiteMgr;


/**
 * ClassName: LoadSiteInfotoMemery
 * Function: 负责网站信息的内存加载，由CT调度
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class LoadSiteInfotoMemery {
	
	private static Log log = LogFactory.getLog(LoadSiteInfotoMemery.class);
	
	private UnionSiteMgr unionSiteMgr;
	
	public UnionSiteMgr getUnionSiteMgr() {
		return unionSiteMgr;
	}

	public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
		this.unionSiteMgr = unionSiteMgr;
	}		
	
	/**
	 * 载入新的的站点信息
	 * 2009-4-24
	 * zengyunfeng
	 * @version 1.1.3
	 * @throws Exception
	 */
	public void reloadSiteInfo() throws Exception
	{
		log.info("reloadSiteInfo is start");
		//加载内存
		unionSiteMgr.loadSiteInfo();
		
		log.info("reloadSiteInfo is over");
	}
	
	public void reloadSiteSizeInfo() throws Exception
	{
		log.info("reloadSiteSizeInfo is start");
		//加载内存
		unionSiteMgr.loadSiteSizeInfo();
		
		log.info("reloadSiteSizeInfo is over");
	}
}
