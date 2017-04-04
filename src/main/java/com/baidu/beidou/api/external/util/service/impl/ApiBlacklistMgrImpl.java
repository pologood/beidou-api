package com.baidu.beidou.api.external.util.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.dao.ApiBlacklistDao;
import com.baidu.beidou.api.external.util.service.ApiBlacklistMgr;

/**
 * 
 * ClassName: ApiBlacklistMgrImpl  <br>
 * Function: api黑名单
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class ApiBlacklistMgrImpl implements ApiBlacklistMgr{

	private static final Log LOG = LogFactory.getLog(ApiBlacklistMgrImpl.class);
	
	ApiBlacklistDao apiBlacklistDao;
	
	/**
	 * 获取所有黑名单用户id
	 * @return Set<Integer>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<Integer> getAllBlacklistUserids(){
		Set<Integer> set = new HashSet(apiBlacklistDao.getAllBlacklistUserids());
		StringBuffer sb = new StringBuffer();
		for(Integer userid:set){
			sb.append(userid + ",");
		}
		LOG.info("the users that banned to access api are [" + sb.toString() + "]");
		return set;
	}

	public ApiBlacklistDao getApiBlacklistDao() {
		return apiBlacklistDao;
	}

	public void setApiBlacklistDao(ApiBlacklistDao apiBlacklistDao) {
		this.apiBlacklistDao = apiBlacklistDao;
	}

}
