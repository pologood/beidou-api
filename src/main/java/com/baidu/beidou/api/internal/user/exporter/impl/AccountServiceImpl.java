/**
 * 2009-8-19 下午04:20:27
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.exporter.impl;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.account.constant.AccountConfig;
import com.baidu.beidou.account.service.MfcService;
import com.baidu.beidou.api.internal.user.error.UserErrorCode;
import com.baidu.beidou.api.internal.user.exporter.AccountService;
import com.baidu.beidou.api.internal.util.error.GlobalErrorCode;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.constant.UserConstant;
import com.baidu.beidou.user.service.UserMgr;

/**
 * @author zengyunfeng
 * 
 */
public class AccountServiceImpl implements AccountService {
	
	private final static Log LOG = LogFactory.getLog(AccountServiceImpl.class);

	private UserMgr userMgr = null;	
	
	private MfcService mfcService;

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}


	public MfcService getMfcService() {
        return mfcService;
    }

    public void setMfcService(MfcService mfcService) {
        this.mfcService = mfcService;
    }

    /**
	 * 提供给财务中心使用，当beidou账户余额由零调整为非零时，调整balancestat字段为1，上线广告传输。
	 * 当财务中心金额转账到零时，将调整balancestat字段为0，下线广告传输。
	 * 日志中记录下操作时间，以备查询使用
	 * @param userId 用户ID，整型
	 * @param balanceStat 状态值，1为有钱，上线；0没有钱，下线
	 * @return 结果状态码：0 执行成功；-2 参数非法;-100 找不到对应的用户；-1 系统报错，需重试
	 *
	 * @deprecated balancestat字段不再用于触发广告上下线,
	 * 并且已确认线上很多用户有余额但是balancestat为0,所以该字段意义不大,
	 * 跟财务中心确认,他们的调用逻辑是:只有网盟专属资金池用户才会调用api,修改balancestat,其他用户不调用
	 * TODO 这次分产品框架先保留接口旧逻辑,等新框架上线后再下线该接口,节省双方维护代价     by hewei18 20151210
	 */
	public int modUserBalanceStat(Integer userId, Integer balanceStat){
		
		if(userId == null){
			LOG.warn("operation:onlineUserBalanceStat,result fail,userId:NULL" + ",Time:" + new Date());
			return GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(); //-2
		}
		
		User user = userMgr.findUserBySFid(userId);
		
		if(user == null){
			LOG.warn("operation:onlineUserBalanceStat,result fail,userId:not exist" + ",Time:" + new Date());
			return UserErrorCode.NO_USER.getValue(); //-100
		}
		
		if(balanceStat == null 
				|| !( balanceStat == UserConstant.BALANCESTAT_ONLINE ||  balanceStat == UserConstant.BALANCESTAT_OFFLINE)){
			LOG.warn("operation:onlineUserBalanceStat,result fail,balanceStat:error" + ",Time:" + new Date());
			return GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(); //-2
		}

		try {
			// ----------------------->mod by liangshimu,20100916,DoubleCheck用户是否真有钱
			if (UserConstant.BALANCESTAT_ONLINE == balanceStat) {

				double[][] result = mfcService.getUserProductBalance(
						Arrays.asList(new Integer[] {userId}),
						Arrays.asList(new Integer[] {AccountConfig.MFC_BEIDOU_PRODUCTID}),
						AccountConfig.MFC_OPUID_DEFAULT);
				if (result == null || result.length < 1 || result[0] == null || result[0].length < 1) {
					LOG.warn("fail to get user[" + userId + "]'s balance");
					return GlobalErrorCode.SYSTEM_BUSY.getValue(); // -1
				} else {
					if (result[0][0] == 0) {

						// --------------------> cpweb-201-配合财务中心API升级 by hejinggen, 2010-11-08
						result = mfcService.getUserAccount(
								userId,
								Arrays.asList(new Integer[] {AccountConfig.MFC_BEIDOU_PRODUCTID}),
								true//强制读财务中心主库
						);

						if (result == null || result.length < 1 || result[0] == null || result[0].length < 1) {
							// 数据不合规范
							LOG.warn("fail to get user[" + userId + "]'s balance");
							return GlobalErrorCode.SYSTEM_BUSY.getValue(); // -1
						} else {
							if (result[0][0] == 0) {
								LOG.warn("data mismatch:user[" + userId
										+ "]'s balance is zero but try to set it online");
								return GlobalErrorCode.SYSTEM_BUSY.getValue(); // -1
							}
							userMgr.modUserBalanceStat(userId, balanceStat);
						}
						// <--------------------cpweb-201-配合财务中心API升级 by hejinggen, 2010-11-08

					} else {
						userMgr.modUserBalanceStat(userId, balanceStat);
					}
				}
			} else {
				userMgr.modUserBalanceStat(userId, balanceStat);
			}
			// <----------------------mod by liangshimu,20100916
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return GlobalErrorCode.SYSTEM_BUSY.getValue(); // -1
		}
		
		LOG.info("operation:onlineUserBalanceStat,result success,userId:" + userId + ",Time:" + new Date());
		
		return GlobalErrorCode.SUCCESS.getValue(); //0
	}
}