package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: UnionsiteVo  <br>
 * Function: 联盟站点数据 <br>
 * 
 */
public class UnionsiteVo implements AbstractVo {

	private String siteUrl;

	private String siteName;

	private int firstTradeId;

	private int secondTradeId;

	public String[] toStringArray() {
		String[] str = new String[4];
		str[0] = String.valueOf(this.getSiteUrl());
		str[1] = String.valueOf(this.getSiteName());
		str[2] = String.valueOf(this.getFirstTradeId());
		str[3] = String.valueOf(this.getSecondTradeId());
		return str;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

}
