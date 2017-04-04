package com.baidu.beidou.api.external.accountfile.facade;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;

/**
 * 
 * ClassName: AccountFileFacade  <br>
 * Function: jms代理，实现handleMessage接口与插入msg到queue中
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public interface AccountFileFacade {

	/**
	 * addTask 添加任务到jms
	 * @version 2.0.1
	 * @author zhangxu
	 * @date Mar 26, 2012
	 */
	public void addTask(ApiAccountFileTask task);
	
}
