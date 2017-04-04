package com.baidu.beidou.api.external.cprogroup.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: AddTradePriceRequest  <br>
 * Function: 添加分行业出价请求头
 *
 * @author zhangxu
 * @date Jun 3, 2012
 */
public class AddTradePriceRequest implements ApiRequest {

	private GroupTradePriceType[] tradePrices;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(tradePrices)) {
			result = tradePrices.length;
		}
		
		return result;
	}
	
	public GroupTradePriceType[] getTradePrices() {
		return tradePrices;
	}

	public void setTradePrices(GroupTradePriceType[] tradePrices) {
		this.tradePrices = tradePrices;
	}
	
}
