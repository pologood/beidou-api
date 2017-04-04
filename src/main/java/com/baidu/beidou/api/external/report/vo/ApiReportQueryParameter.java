package com.baidu.beidou.api.external.report.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ReportQueryParameter <br>
 * Function: api 报告模块查询参数
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 26, 2011
 */
public class ApiReportQueryParameter {

	private int userid;
	
	private Date startDate;
	
	private Date endDate;
	
	private List<Integer> planIds;
	
	private List<Integer> groupIds;
	
	private List<Long> unitIds;
	
	private List<Integer> keywordIds;
	
	private int reportType;
	
	private boolean idOnly;
	
	private int statRange;
	
	private boolean isNeedTransHolmes;
	
	private boolean isNeedUv;
	
	private int isNeedBudget;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getPlanIds() {
		return planIds;
	}

	public void setPlanIds(List<Integer> planIds) {
		this.planIds = planIds;
	}

	public List<Integer> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}

	public List<Long> getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(List<Long> unitIds) {
		this.unitIds = unitIds;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	
	public boolean isIdOnly() {
		return idOnly;
	}

	public void setIdOnly(boolean idOnly) {
		this.idOnly = idOnly;
	}

	public int getStatRange() {
		return statRange;
	}

	public void setStatRange(int statRange) {
		this.statRange = statRange;
	}

	public boolean isNeedTransHolmes() {
		return isNeedTransHolmes;
	}

	public void setNeedTransHolmes(boolean isNeedTransHolmes) {
		this.isNeedTransHolmes = isNeedTransHolmes;
	}

	public boolean isNeedUv() {
		return isNeedUv;
	}

	public void setNeedUv(boolean isNeedUv) {
		this.isNeedUv = isNeedUv;
	}
    
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("userid",userid)
		.append("startDate", startDate)
        .append("endDate", endDate)
        .append("planIds", planIds)
        .append("groupIds", groupIds)
        .append("unitIds", unitIds)
        .append("keywordIds", keywordIds)
        .append("reportType",reportType)
        .append("statRange", statRange)
        .append("idOnly", idOnly)
        .append("isNeedTransHolmes", isNeedTransHolmes)
        .append("isNeedUv", isNeedUv)
        .append("isNeedBudget",isNeedBudget)
        .toString();
	}

	public int getIsNeedBudget() {
		return isNeedBudget;
	}

	public void setIsNeedBudget(int isNeedBudget) {
		this.isNeedBudget = isNeedBudget;
	}

	
}
