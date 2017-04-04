package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: TradeSitePriceType
 * Function: 分行业网站出价信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class TradeSitePriceType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// 推广组Id
	private long groupId;
	
	// 分行业出价列表
	private List<TradePriceType> tradePriceList;
	
	// 分网站出价列表
	private List<SitePriceType> sitePriceList;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<TradePriceType> getTradePriceList() {
		return tradePriceList;
	}

	public void setTradePriceList(List<TradePriceType> tradePriceList) {
		this.tradePriceList = tradePriceList;
	}

	public List<SitePriceType> getSitePriceList() {
		return sitePriceList;
	}

	public void setSitePriceList(List<SitePriceType> sitePriceList) {
		this.sitePriceList = sitePriceList;
	}
	
}
