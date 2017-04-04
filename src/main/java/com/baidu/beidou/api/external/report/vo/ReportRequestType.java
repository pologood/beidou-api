package com.baidu.beidou.api.external.report.vo;

import java.util.Date;

/**
 * 
 * ClassName: ReportRequestType  <br>
 * Function: 请求参数
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 12, 2012
 */
public class ReportRequestType {
	
	/**
	 * 	performanceData	String[]	指定绩效数据	选填；默认为srchs,click,cost,ctr,acp,cpm
		取值范围为srchs,click,cost,ctr,acp,cpm任意组合。
		srchs: 展现次数
		click：点击次数
		cost：消费（￥）
		ctr：点击率
		acp：平均点击价格
		cpm：千次展现成本
	 */
	String[] performanceData;
	
	/**
	 * startDate	dateTime	统计开始时间，格式参考：2010-08-01	
	 * 必填，从2008-11-13开始
	 */
	Date  startDate;
	
	/**
	 * startDate	dateTime	统计结束时间，格式参考：2010-08-01
	 */
	Date endDate;
	
	/**
	 * idOnly	boolean	是否只需要id	选填；默认为false，显示全部字面；
	 */
	boolean idOnly;
	
	/**
	 * reportType	int	报告类型	必填；
		1. 账户报告
		2. 推广计划报告
		3. 推广组报告
		4. 创意报告
		5. 搜客关键词报告
		6. 主题词报告
		7. 有展现网站报告
	    8. 有展现兴趣报告
	    9. 受众组合报告
	    10. 关键词组合报告
	    11. 地域报告
	    12. 性别报告
	    13. 自选兴趣报告
	    14. 自选网站报告
	    15. 自选行业报告
	    16. APP报告
	    17. 设备报告
	    18. 附加信息—电话报告
	    19. 附加信息-短信报告
	    20. 附加信息-咨询报告
	    21. 附加信息-子链报告
	 */
	int reportType;
	
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
	int statRange;
	
	/**
	 * statIds	long[]	统计范围下的id集合。根据StatRange的不同类型填写不同id，需要与statRange参数配合一起使用。	选填，默认NULL，表示统计范围为全账户。
		staRange为2时填写推广计划id;
		staRange为3时填写推广组id;
		staRange为4时填写创意id;
		staRange为5时填写关键词keywordid;
		staRange为6时填写主题词wordid
		id至多100个。
	 */
	long[] statIds;
	
	/**
	 * 	format	int	报告文件格式	选填，默认值为0；
		0：zip压缩包格式
		1：csv格式
	 */
	int format;
	
	public ReportRequestType(){
		
	}

	public String[] getPerformanceData() {
		return performanceData;
	}

	public void setPerformanceData(String[] performanceData) {
		this.performanceData = performanceData;
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

	public boolean isIdOnly() {
		return idOnly;
	}

	public void setIdOnly(boolean idOnly) {
		this.idOnly = idOnly;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public int getStatRange() {
		return statRange;
	}

	public void setStatRange(int statRange) {
		this.statRange = statRange;
	}

	public long[] getStatIds() {
		return statIds;
	}

	public void setStatIds(long[] statIds) {
		this.statIds = statIds;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}
	
}
