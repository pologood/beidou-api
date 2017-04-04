package com.baidu.beidou.api.external.user.exporter;

import com.baidu.beidou.api.external.user.vo.request.PaymentRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * ClassName: PayCalculaterService
 * Function: 获取用户上周消费，提供给dr-api，计算配额使用
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public interface PayCalculaterService {
	public static final String DATE_FORMAT = "yyyyMMdd";

	/**
	 * 获取request中用户的上一周消费总额
	 * 
	 * @param user
	 * @param reserveData
	 * @return
	 */
	public ApiResult<Double> getUserCostLastWeek(DataPrivilege user,
			PaymentRequest request, ApiOption apiOption);
}
