package com.baidu.beidou.api.internal.fcindex.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.account.service.MfcService;
import com.baidu.beidou.api.internal.fcindex.service.SimpleUserInfoMgr;
import com.baidu.beidou.cproplan.dao.CproPlanDao;
import com.baidu.beidou.user.dao.UserAvgCostDao;

public class SimpleUserInfoMgrImpl implements SimpleUserInfoMgr {

	private static final Log log = LogFactory.getLog(SimpleUserInfoMgrImpl.class);

	private CproPlanDao cproPlanDao;
	
	private UserAvgCostDao userAvgCostDao = null;
	
	private MfcService mfcService;
	
	/**
	 * 获得指定用户的beidou账户余额(单位为分)和可消费天数<br>
	 * 可消费天数=帐面余额/(有效推广计划预算之和) 向下取整
	 * @param userId
	 * @return int[] 用户余额（单位为分）和预计可消费天数
	 */
	public int[] getBalanceInfo(Integer userId){
		if(userId==null){
			return new int[]{0,0};
		}
		int[] result = new int[2];

		// 账户余额
		Double balance = mfcService.getUserBalance(userId);
		if (balance == null) {
			log.error("MFC response error");
		}
		if(balance == null || balance == 0){
			return new int[]{0,0};
		}
		result[0] = new Double(balance*100).intValue();
		result[1] = getTotalValidBudget(userId)*100;	//单位为分
		if(result[1]>0){
			result[1] = result[0]/result[1];
		}else{
			result[1] = -1;
		}
 		return result;
	}
	
	/**
	 * 返回用户的有效预算，单位为分
	 * @param userid
	 * @return
	 */
	public int getTotalValidBudget(Integer userId){
		List<Integer> userIdList = new ArrayList<Integer>();
		if(userId <= 0){
			log.error("getTotalValidBudget input userid is invalid");
			return 0;
		}
		userIdList.add(userId);
		Map<Integer, Integer> result = cproPlanDao.getAllValidBudgetByUserIds(userIdList);
		if(result != null && result.size() == 1){
			if(!result.containsKey(userId)){
				log.error("getTotalValidBudget userid got from DAO does not equal to input");
			} 
			return result.get(userId);
		} else {
			return 0;
		}
	}
	
	/**
	 * 返回用户最近七天平均消费（单位：分），经过四舍五入
	 * @param userId
	 * @return
	 */
	public int getWeekAvgCost(Integer userId){
		if(userId==null){
			return 0;
		}
		double yuan = userAvgCostDao.getWeekAvgCost(userId);
		return Long.valueOf(Math.round(yuan * 100)).intValue();
	}

	public CproPlanDao getCproPlanDao() {
		return cproPlanDao;
	}

	public void setCproPlanDao(CproPlanDao cproPlanDao) {
		this.cproPlanDao = cproPlanDao;
	}

	public MfcService getMfcService() {
		return mfcService;
	}

	public void setMfcService(MfcService mfcService) {
		this.mfcService = mfcService;
	}

    public void setUserAvgCostDao(UserAvgCostDao userAvgCostDao) {
        this.userAvgCostDao = userAvgCostDao;
    }
	
}
