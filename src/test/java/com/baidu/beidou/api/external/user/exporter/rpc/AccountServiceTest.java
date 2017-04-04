package com.baidu.beidou.api.external.user.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.user.exporter.UserAccountService;
import com.baidu.beidou.api.external.user.vo.AccountInfoType;
import com.baidu.beidou.api.external.user.vo.request.GetAccountInfoRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
@Ignore
public class AccountServiceTest extends ApiBaseRPCTest<UserAccountService> {

	@Test
	public void testGetAccount() throws Exception {
		UserAccountService exporter = getServiceProxy(UserAccountService.class, ApiExternalConstant.USERACCOUNT_SERVICE_URL);

		GetAccountInfoRequest request = new GetAccountInfoRequest();
		ApiResult<AccountInfoType> result = exporter.getAccountInfo(dataUser, request, apiOption);
		System.out.println(result);

	}

}
