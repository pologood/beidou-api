package com.baidu.beidou.api.external.user.exporter;

import com.baidu.beidou.api.external.user.vo.AccountInfoType;
import com.baidu.beidou.api.external.user.vo.request.GetAccountInfoRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * ClassName: AccountService
 * Function: 获取用户信息接口
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public interface UserAccountService {

	/**
	 * 获取user.dataUser的账户信息
	 * 
	 * @param user
	 * @param reserveData 保留参数
	 * @return ApiResult中data类型为AccountInfoType
	 */
	public ApiResult<AccountInfoType> getAccountInfo(DataPrivilege user,
			GetAccountInfoRequest request, ApiOption apiOption);

}