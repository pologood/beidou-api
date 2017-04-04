package com.baidu.beidou.api.external.util.service;


/**
 * 
 * ClassName: IntensityControlMgr  <br>
 * Function: 负载控制mgr，负责读取本地配置文件，内容为 <p>
 * 			functionName, sleepTime的关联对，当调用functionName时，主动暂定sleepTime秒
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public interface IntensityControlMgr {

	/**
	 * 加载functionName, sleepTime的关联对
	 */
	public void loadFunctionAndSleepTimeMap();
	
}
