package com.baidu.beidou.api.external.report.facade;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;

/**
 * 
 * InterfaceName: ApiReportFacadeImpl  <br>
 * Function: jms代理，实现handleMessage接口与插入msg到queue中
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public interface ApiReportFacade {

	/**
	 * addTask 添加任务到jms
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 4, 2012
	 */
	public void addTask(ApiReportTask task);
	
}
