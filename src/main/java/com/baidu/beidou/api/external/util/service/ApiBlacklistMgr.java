package com.baidu.beidou.api.external.util.service;

import java.util.Set;

/**
 * 
 * ClassName: ApiBlacklistMgr  <br>
 * Function: api黑名单
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public interface ApiBlacklistMgr {
	
	/**
	 * 获取所有黑名单用户id
	 * @return Set<Integer>
	 */
	public Set<Integer> getAllBlacklistUserids();

}
