package com.baidu.beidou.api.internal.fcindex.webservice;

import com.baidu.beidou.api.internal.fcindex.ServiceFinder;
import com.baidu.beidou.api.internal.fcindex.service.SimpleUserInfoMgr;



/**
 * 
 * ClassName: UserAccountManager
 * Function: 提供给凤巢大首页用的服务,目前凤巢只用getUserBudget，而getUserBalanceInfo是FC从MFC拿的
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 20, 2011
 */
public class UserAccountManager {
	
	private SimpleUserInfoMgr simpleUserInfoMgr = (SimpleUserInfoMgr)ServiceFinder.getInstance().getBean("simpleUserInfoMgr");

	/**
	 * 获得指定用户的beidou账户余额(单位为分)和可消费天数<br>
	 * 可消费天数=帐面余额/(有效推广计划预算之和) 向下取整
	 * @param userId
	 * @return int[] 用户余额（单位为分）和预计可消费天数
	 */
	public int[] getUserBalanceInfo(int userid){
		return simpleUserInfoMgr.getBalanceInfo(userid);
	}
	
	/**
	 * 获得指定用户最近七天平均消费（单位：分），经过四舍五入<br>
	 * @param userid
	 * @return
	 */
	public int getUserBudget(int userid){
		return simpleUserInfoMgr.getWeekAvgCost(userid);
	}

}
