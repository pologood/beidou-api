package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.SitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.TradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.TradeSitePriceType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetTradeSitePriceRequest
 * Function: 设置分行业网站出价请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetTradeSitePriceRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -2995077155369761762L;
	
	private TradeSitePriceType tradeSitePrice;	// 分行业/分网站出价
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (tradeSitePrice != null) {
			result = 0;
			List<TradePriceType> tradePriceList = tradeSitePrice.getTradePriceList();
			List<SitePriceType> sitePriceList = tradeSitePrice.getSitePriceList();
			
			if (CollectionUtils.isNotEmpty(tradePriceList)) {
				result += tradePriceList.size();
			}
			
			if (CollectionUtils.isNotEmpty(sitePriceList)) {
				result += sitePriceList.size();
			}
		}
		
		return result;
	}

	public TradeSitePriceType getTradeSitePrice() {
		return tradeSitePrice;
	}

	public void setTradeSitePrice(TradeSitePriceType tradeSitePrice) {
		this.tradeSitePrice = tradeSitePrice;
	}
}
