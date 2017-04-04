package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: OneReportResponseType  <br>
 * Function: api返回类型vo
 * 
 * @author caichao
 * @date 13-9-9
 */
public class OneReportResponseType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6442724291799467381L;
	private String date;
	private int userId;
	private String userName;
	private int campaignId;
	private String campaignName;
	private int groupId;
	private String groupName;
	private long adId;
	/**
	 * 基础统计数据
	 */
	private long srch;//展现
	private long clk;//点击
	private double cost;//单位元
	private BigDecimal ctr;//点击率
	private BigDecimal acp;//平均点击价格
	private BigDecimal cpm;//千次展现成本
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public long getSrch() {
		return srch;
	}
	public void setSrch(long srch) {
		this.srch = srch;
	}
	public long getClk() {
		return clk;
	}
	public void setClk(long clk) {
		this.clk = clk;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public BigDecimal getCtr() {
		return ctr;
	}
	public void setCtr(BigDecimal ctr) {
		this.ctr = ctr;
	}
	public BigDecimal getAcp() {
		return acp;
	}
	public void setAcp(BigDecimal acp) {
		this.acp = acp;
	}
	public BigDecimal getCpm() {
		return cpm;
	}
	public void setCpm(BigDecimal cpm) {
		this.cpm = cpm;
	}
	public long getAdId() {
		return adId;
	}
	public void setAdId(long adId) {
		this.adId = adId;
	}
    //userId userId campaignId campaignName groupId groupName adId srch clk ctr acp cpm
	public String toString(){

		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("date",date)
		.append("userId", userId)
		.append("userName", userName)
		.append("campaignId", campaignId)
		.append("campaignName", campaignName)
		.append("groupId", groupId)
		.append("groupName", groupName)
		.append("adId", adId)
		.append("srch", srch)
		.append("clk", clk)
		.append("ctr", ctr)
		.append("acp", acp)
		.append("cpm", cpm)
        .toString();
		//return sb.toString();
	}
	
}
