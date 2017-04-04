package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

/**
 * @author zhuqian
 *
 */
public class GroupSiteVO implements Serializable {

	private Integer siteId;
	private String siteUrl;
	private Integer firstTradeId;
	private String firstTradeName;
	private Integer secondTradeId;
	private String secondTradeName;
	private CmpLevelVO heat;
	
	public String toString(){
		return "["+siteId + ", " + siteUrl + ", " + heat.getCmpLevel() + ", " + heat.getCmpSize()+"]";
	}
	
	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	/**
	 * @param siteUrl the siteUrl to set
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	/**
	 * @return the firstTradeId
	 */
	public Integer getFirstTradeId() {
		return firstTradeId;
	}
	/**
	 * @param firstTradeId the firstTradeId to set
	 */
	public void setFirstTradeId(Integer firstTradeId) {
		this.firstTradeId = firstTradeId;
	}
	/**
	 * @return the firstTradeName
	 */
	public String getFirstTradeName() {
		return firstTradeName;
	}
	/**
	 * @param firstTradeName the firstTradeName to set
	 */
	public void setFirstTradeName(String firstTradeName) {
		this.firstTradeName = firstTradeName;
	}
	/**
	 * @return the secondTradeId
	 */
	public Integer getSecondTradeId() {
		return secondTradeId;
	}
	/**
	 * @param secondTradeId the secondTradeId to set
	 */
	public void setSecondTradeId(Integer secondTradeId) {
		this.secondTradeId = secondTradeId;
	}
	/**
	 * @return the secondTradeName
	 */
	public String getSecondTradeName() {
		return secondTradeName;
	}
	/**
	 * @param secondTradeName the secondTradeName to set
	 */
	public void setSecondTradeName(String secondTradeName) {
		this.secondTradeName = secondTradeName;
	}
	/**
	 * @return the heat
	 */
	public CmpLevelVO getHeat() {
		return heat;
	}
	/**
	 * @param heat the heat to set
	 */
	public void setHeat(CmpLevelVO heat) {
		this.heat = heat;
	}
		
}
