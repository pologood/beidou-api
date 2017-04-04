package com.baidu.beidou.api.external.util.dao;

import java.util.List;

/**
 * 
 * ClassName: ApiBlacklistDao  <br>
 * Function: API黑名单DAO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public interface ApiBlacklistDao {

	/**
	 * 获取所有黑名单用户id
	 * @return List<Integer>
	 */
	public List<Integer> getAllBlacklistUserids();
		
}
