package com.baidu.beidou.api.external.report.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TradeStatViewItem extends AbstractStatViewItem {
	
	private static final long serialVersionUID = 4578389666041020020L;
	
	private int userId;
	
	private String userName;
	
	private int planId;
	
	private String planName;
	
	private int groupId;
	
	private String groupName;
	
	private String selfTradeName;
	
	private Integer selfTradeId;
	
	private String firstTrade; //一级行业
	private String secondTrade; //二级行业
	
	//给前端的数据是压平的，如果是一级，则level一定为0，如果是只选了的2级，level也为0
	private int level = 0;
	
	private int firstTradeId = 0; 
	private int secondTradeId = 0;
	
	private boolean withData=false;//是否有Dorise数据
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
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
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
	public String getFirstTrade() {
		return firstTrade;
	}
	public void setFirstTrade(String firstTrade) {
		this.firstTrade = firstTrade;
	}
	public String getSecondTrade() {
		return secondTrade;
	}
	public void setSecondTrade(String secondTrade) {
		this.secondTrade = secondTrade;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public boolean isWithData() {
		return withData;
	}
	public void setWithData(boolean withData) {
		this.withData = withData;
	}
	public String getSelfTradeName() {
		return selfTradeName;
	}
	public void setSelfTradeName(String selfTradeName) {
		this.selfTradeName = selfTradeName;
	}
	public Integer getSelfTradeId() {
		return selfTradeId;
	}
	public void setSelfTradeId(Integer selfTradeId) {
		this.selfTradeId = selfTradeId;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("day", day)
		.append("userid", userId)
		.append("username", userName)
		.append("planid", planId)
		.append("planname", planName)
		.append("groupid", groupId)
		.append("groupname", groupName)
		.append("tradeName", selfTradeName)
		.append("tradeId", selfTradeId)
        .append("srchs", srchs)
        .append("clks", clks)
        .append("cost", cost)
        .append("ctr", ctr)
        .append("acp", acp)
        .append("cpm", cpm)
        .append("srchuv", srchuv)
        .append("clkuv", clkuv)
        .append("srsur", srsur)
        .append("cusur", cusur)
        .append("cocur", cocur)
        .append("arrivalRate", arrivalRate)
        .append("hopRate", hopRate)
        .append("avgResTime", avgResTime)
        .append("directTrans", directTrans)
        .append("indirectTrans", indirectTrans)
        .toString();
	}

}
