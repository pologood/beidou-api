package com.baidu.beidou.api.external.cprogroup.facade;

import java.util.List;

import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 
 * ClassName: GroupPriceFacade  <br>
 * Function: 多维出价facade
 *
 * @author zhangxu
 * @date Sep 17, 2012
 */
public interface GroupPriceFacade {

	/**
	 * 设置多维出价配置
	 * @param response
	 * @param prices
	 * @param userId
	 * @param opUser
	 */
	public void setPrice(BaseResponse<PlaceHolderResult>response, 
			List<PriceType> prices,
			int userId,
			int opUser);
	
}
