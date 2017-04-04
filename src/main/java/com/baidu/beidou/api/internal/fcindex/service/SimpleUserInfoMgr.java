package com.baidu.beidou.api.internal.fcindex.service;

public interface SimpleUserInfoMgr {

	/**
	 * 获得指定用户的beidou账户余额(单位为分)和可消费天数<br>
	 * 可消费天数=帐面余额/(有效推广计划预算之和) 向下取整
	 * @param userid
	 * @return
	 */
	public int[] getBalanceInfo(Integer userId);
	
	/**
	 * 返回用户的有效预算，单位为分
	 * @param userid
	 * @return
	 */
	public int getTotalValidBudget(Integer userId);
	
	/**
	 * 返回用户最近七天平均消费（单位：分），经过四舍五入
	 * @param userId
	 * @return
	 */
	public int getWeekAvgCost(Integer userId);
	
}
