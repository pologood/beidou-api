package com.baidu.beidou.api.external.tool.service.vo;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.tool.service.vo.impl.AccountReportDataImpl;
import com.baidu.beidou.api.external.tool.service.vo.impl.GroupReportDataImpl;
import com.baidu.beidou.api.external.tool.service.vo.impl.PlanReportDataImpl;
import com.baidu.beidou.api.external.tool.service.vo.impl.UnitReportDataImpl;

/**
 * 
 * ClassName: ReportDataFactory <br>
 * Function: 根据报告类型得到不同层级业务工厂类
 * 
 * @author caichao
 * @date 2013-09-11
 */
public class ReportDataFactory {
	/**
	 * 根据报告类型返回对应实现类
	 * @param reportType报告类型
	 * @return
	 */
	public static AbstractReportData getReportData(int reportType) {
		switch (reportType) {
		case ReportWebConstants.REPORT_TYPE.ACCOUNT:
			return new AccountReportDataImpl();
		case ReportWebConstants.REPORT_TYPE.PLAN:
			return new PlanReportDataImpl();
		case ReportWebConstants.REPORT_TYPE.GROUP:
			return new GroupReportDataImpl();
		case ReportWebConstants.REPORT_TYPE.UNIT:
			return new UnitReportDataImpl();
		default:
			return null;
		}
	}

}
