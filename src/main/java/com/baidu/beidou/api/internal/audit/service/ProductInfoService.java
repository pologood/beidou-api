package com.baidu.beidou.api.internal.audit.service;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;

public interface ProductInfoService {
	
	/**
	 * getProductViewList: 根据unitId等信息，获取该创意可拼接生成的产品预览列表，抽查
	 * @version cpweb-699
	 * @author genglei01
	 * @date Mar 5, 2014
	 */
	public List<ProductView> getProductViewList(QueryProduct query);
	
	/**
	 * refuseProduct: 拒绝产品
	 * @version cpweb-699
	 * @author genglei01
	 * @date Mar 5, 2014
	 */
	public boolean refuseProduct(List<Long> productIds, Integer userId);

}
