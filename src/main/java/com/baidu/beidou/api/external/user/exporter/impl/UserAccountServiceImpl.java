package com.baidu.beidou.api.external.user.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;

import com.baidu.beidou.account.constant.AccountConfig;
import com.baidu.beidou.account.service.MfcService;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.user.exporter.UserAccountService;
import com.baidu.beidou.api.external.user.vo.AccountInfoType;
import com.baidu.beidou.api.external.user.vo.request.GetAccountInfoRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * ClassName: AccountServiceImpl
 * Function: 获取用户信息接口
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class UserAccountServiceImpl implements UserAccountService {

	private UserMgr userMgr = null;

	private MfcService mfcService = null;

	public ApiResult<AccountInfoType> getAccountInfo(DataPrivilege user,
			GetAccountInfoRequest request, ApiOption apiOption) {
		
		ApiResult<AccountInfoType> result = new ApiResult<AccountInfoType>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		payment.setSuccess(1);
		result.setPayment(payment);

		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		User userInfo = userMgr.findUserBySFid(user.getDataUser());
		if (userInfo == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		} else {

			List<Integer> userIds = new ArrayList<Integer>();
			userIds.add(userInfo.getUserid());
			List<Integer> products = new ArrayList<Integer>();
			products.add(AccountConfig.MFC_BEIDOU_PRODUCTID);
//			List<Integer> accountIds = new ArrayList<Integer>();
//			accountIds.add(AccountConfig.MFC_TECH_ACCOUNTIDS);
//			accountIds.add(AccountConfig.MFC_BEIDOU_ACCOUNTIDS);

			Mapper mapper = BeanMapperProxy.getMapper();
			AccountInfoType account = mapper.map(userInfo, AccountInfoType.class);

			Double mfcDataResult = mfcService.getUserBalance(userInfo.getUserid());
			//double[][] mfcDataResult = mfcService.getUserAccountBalance(userIds, accountIds, 0);
			if (mfcDataResult == null) {
				account.setBalance(0f);
				//account.setWangmeng_balance(0f);
			} else {
				account.setBalance(mfcDataResult.floatValue());
				// account.setWangmeng_balance(Double.valueOf(mfcDataResult[0][1])
				// .floatValue());
			}

			result = ApiResultBeanUtils.addApiResult(result, account);
		}
		return result;
	}

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
}
