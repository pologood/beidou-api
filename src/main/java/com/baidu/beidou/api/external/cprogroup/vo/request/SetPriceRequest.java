package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetPriceRequest  <br>
 * Function: 设置推广组相关出价请求头
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class SetPriceRequest implements ApiRequest {

	private List<PriceType> prices;

	public List<PriceType> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceType> prices) {
		this.prices = prices;
	}
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(prices)) {
			result = prices.size();
		}
		
		return result;
	}
	
}
