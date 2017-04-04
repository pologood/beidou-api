package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: SiteInfo  <br>
 * Function: 网站信息
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class SiteInfo implements Serializable{
	
	private static final long serialVersionUID = 1132219242791L;

	private String site;
	
	private int firstTradeId;
	
	private int secondTradeId;
	
	private String reserved;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public int getFirstTradeId() {
		return firstTradeId;
	}

	public void setFirstTradeId(int firstTradeId) {
		this.firstTradeId = firstTradeId;
	}

	public int getSecondTradeId() {
		return secondTradeId;
	}

	public void setSecondTradeId(int secondTradeId) {
		this.secondTradeId = secondTradeId;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("site",site)
		.append("firstTradeId", firstTradeId)
		.append("secondTradeId", secondTradeId)
		.append("reserved", reserved)
        .toString();
	}
	
}
