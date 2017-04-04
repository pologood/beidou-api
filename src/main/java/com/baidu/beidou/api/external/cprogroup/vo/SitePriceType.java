package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: SitePriceType
 * Function: 分网站出价URL信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SitePriceType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//站点域名
	private String site;
	
	//单位为元
	private float price;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
