package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 一站式报告请求数据定义
 * @author caichao
 * @date 2013-9-9
 */
public class OneReportRequestType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 504355811100868053L;

	/**
	 * startDate	dateTime	统计开始时间，格式参考：2010-08-01	
	 * 必填，从2008-11-13开始
	 */
	Date startDate;

	/**
	 * startDate	dateTime	统计结束时间，格式参考：2010-08-01
	 */
	Date endDate;

	/**
	 * reportType	int	报告类型	必填；
		1. 账户报告
		2. 推广计划报告
		3. 推广组报告
		4. 创意报告
	 */
	int reportType;

	/**
	 * statRange	int	统计范围	选填
		1. 账户
		2. 推广计划
		3. 推广组
		4. 创意
		统计范围不能细于当前要查看的报告类型reportType。
		统计范围为账户，可以查看任一报告类型；
		统计范围为推广计划，可以查看计划、组、创意报告；
		统计范围为推广组，可以查看组、创意报告；
		统计范围为创意，只能查看创意报告；
	 */
	int statRange;

	/**
	 * statIds	long[]	统计范围下的id集合。根据StatRange的不同类型填写不同id，需要与statRange参数配合一起使用。	选填，默认NULL，表示统计范围为全账户。
		staRange为2时填写推广计划id;
		staRange为3时填写推广组id;
		staRange为4时填写创意id;
		id至多100个。
	 */
	long[] statIds;

	/**
	 * 是否需要统计有效计划预算
	 */
	int isNeedbudget;

	public OneReportRequestType() {

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

	public int getIsNeedbudget() {
		return isNeedbudget;
	}

	public void setIsNeedbudget(int isNeedbudget) {
		this.isNeedbudget = isNeedbudget;
	}

}
