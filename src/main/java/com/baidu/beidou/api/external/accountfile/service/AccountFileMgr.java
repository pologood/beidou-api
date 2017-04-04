package com.baidu.beidou.api.external.accountfile.service;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;

/**
 * 
 * ClassName: AccountFileMgr  <br>
 * Function: 处理queue中监听账户信息请求
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public interface AccountFileMgr {

	/**
	 * 处理jms插入的获取账户信息请求
	 * @param task 
	 * @return 
	 */
	public void process(ApiAccountFileTask task);

}
