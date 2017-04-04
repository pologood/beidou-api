package com.baidu.beidou.api.external.cprogroup.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupSitePriceType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: AddSitePriceRequest  <br>
 * Function: 添加分网站出价请求头
 *
 * @author zhangxu
 * @date Jun 3, 2012
 */
public class AddSitePriceRequest implements ApiRequest {

	private GroupSitePriceType[] sitePrices;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(sitePrices)) {
			result = sitePrices.length;
		}
		
		return result;
	}

	public GroupSitePriceType[] getSitePrices() {
		return sitePrices;
	}

	public void setSitePrices(GroupSitePriceType[] sitePrices) {
		this.sitePrices = sitePrices;
	}
	
}
