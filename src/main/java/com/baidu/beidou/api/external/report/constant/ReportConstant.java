package com.baidu.beidou.api.external.report.constant;

/**
 * 
 * ClassName: ReportConstant  <br>
 * Function: 前端参数不同接口对应的字段名称
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public class ReportConstant {
	
	public interface POSITION_GET_REPORT_ID{
		public static final String POSITION_PERFORMANCEDATA = "performanceData";
		public static final String POSITION_STARTDATE = "startDate";
		public static final String POSITION_ENDDATE = "endDate";
		public static final String POSITION_IDONLY = "idOnly";
		public static final String POSITION_FORMAT = "format";
		public static final String POSITION_REPORTTYPE = "reportType";
		public static final String POSITION_STATRANGE = "statRange";
		public static final String POSITION_STATIDS = "statIds";
	}
	
	public interface POSITION_GET_REPORT_STATE{
		public static final String POSITION_REPORTID = "reportId";
	}
	
	public interface POSITION_GET_REPORT_FILEURL{
		public static final String POSITION_REPORTID = "reportId";
	}
	
}
