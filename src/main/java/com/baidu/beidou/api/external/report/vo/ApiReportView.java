package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ApiReportView  <br>
 * Function: 插入到JMS中的对象类型
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportView implements Serializable{
	
	private static final long serialVersionUID = 4727910230850784183L;	
	
	private long id;
	
	private String reportid;
	
	private int userid;
	
	private int opuser;

	/**
	 * 	performanceData	String[]	指定绩效数据	选填；默认为srchs,click,cost,ctr,acp,cpm
		取值范围为srchs,click,cost,ctr,acp,cpm任意组合，报表按照顺序输出绩效数据。
		srchs: 展现次数
		click：点击次数
		cost：消费（￥）
		ctr：点击率
		acp：平均点击价格
		cpm：前次展现成本
	 */
	private int performanceField;
	
	/**
	 * startDate	dateTime	统计开始时间，格式参考：2010-08-01	
	 * 必填，从2008-11-13开始
	 */
	private Date  startDate;
	
	/**
	 * startDate	dateTime	统计结束时间，格式参考：2010-08-01
	 */
	private Date endDate;
	
	/**
	 * reportType	int	报告类型	必填；
		1. 账户报告
		2. 推广计划报告
		3. 推广组报告
		4. 创意报告
		5. 搜客关键词报告
		6. 主题词报告
		7. 网站报告
	 */
	private int reportType;
	
	/**
	 * statRange	int	统计范围	选填
		1. 账户
		2. 推广计划
		3. 推广组
		4. 创意
		5. 关键词
		6. 主题词。
		统计范围不能细于当前要查看的报告类型reportType。
		统计范围为账户，可以查看任一报告类型；
		统计范围为推广计划，可以查看计划、组、创意、网站、搜客关键词、主题词报告；
		统计范围为推广组，可以查看组、创意、网站、搜客关键词、主题词报告；
		统计范围为创意，只能查看创意报告；
		统计范围为关键词，只能查看搜客关键词报告；
		统计范围为主题词，只能查看主题词报告。
	 */
	private int reportRange;
	
	/**
	 * statIds	long[]	统计范围下的id集合。根据StatRange的不同类型填写不同id，需要与statRange参数配合一起使用。	选填，默认NULL，表示统计范围为全账户。
		staRange为2时填写推广计划id;
		staRange为3时填写推广组id;
		staRange为4时填写创意id;
		staRange为5时填写关键词keywordid;
		staRange为6时填写主题词wordid
		id至多100个。
	 */
	private List<Long> limitIds;
	
	/**
	 * idOnly	int	是否只需要id	选填；默认为false
	 */
	private boolean idOnly;
	
	/**
	 * 	format	int	报告文件格式	选填，默认值为1；
		1：zip压缩包格式
		2：csv格式
	 */
	private boolean isZip;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReportid() {
		return reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getOpuser() {
		return opuser;
	}

	public void setOpuser(int opuser) {
		this.opuser = opuser;
	}

	public int getPerformanceField() {
		return performanceField;
	}

	public void setPerformanceField(int performanceField) {
		this.performanceField = performanceField;
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

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public int getReportRange() {
		return reportRange;
	}

	public void setReportRange(int reportRange) {
		this.reportRange = reportRange;
	}

	public List<Long> getLimitIds() {
		return limitIds;
	}

	public void setLimitIds(List<Long> limitIds) {
		this.limitIds = limitIds;
	}

	public boolean isIdOnly() {
		return idOnly;
	}

	public void setIdOnly(boolean idOnly) {
		this.idOnly = idOnly;
	}
	
	public boolean isZip() {
		return isZip;
	}

	public void setZip(boolean isZip) {
		this.isZip = isZip;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("userid",userid)
		.append("opuser;", opuser)
        .append("performanceField;", performanceField)
        .append("startDate", startDate)
        .append("endDate", endDate)
        .append("reportType", reportType)
        .append("reportRange", reportRange)
        .append("limitIds", limitIds)
        .append("idOnly",idOnly)
        .append("iszip", isZip)
        .toString();
	}
	
}
