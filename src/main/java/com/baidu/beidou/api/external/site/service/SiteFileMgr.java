package com.baidu.beidou.api.external.site.service;

/**
 * 
 * 刷新联盟站点的服务接口，在服务启动以及每天定时调用更新数据
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-7-22 下午5:14:55
 */
public interface SiteFileMgr {

	void refresh();
	
}
