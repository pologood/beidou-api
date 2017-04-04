package com.baidu.beidou.api.external.cprogroup.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: DeleteTradePriceRequest  <br>
 * Function: 删除分行业出价请求头
 *
 * @author zhangxu
 * @date Jun 3, 2012
 */
public class DeleteTradePriceRequest implements ApiRequest {

	private GroupTradeType[] tradePrices;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(tradePrices)) {
			result = tradePrices.length;
		}
		
		return result;
	}

	public GroupTradeType[] getTradePrices() {
		return tradePrices;
	}

	public void setTradePrices(GroupTradeType[] tradePrices) {
		this.tradePrices = tradePrices;
	}
	
	
}
