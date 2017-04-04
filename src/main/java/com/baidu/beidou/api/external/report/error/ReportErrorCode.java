package com.baidu.beidou.api.external.report.error;

/**
 * 
 * ClassName: ReportErrorCode  <br>
 * Function: report前端错误返回码
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 7, 2012
 */
public enum ReportErrorCode {
	
	UNAUTHORIZATION_STATRANGE_PLAN(8000, "StatIds do not have no authorization under the plan statRange"),
	UNAUTHORIZATION_STATRANGE_GROUP(8001, "StatIds do not have no authorization under the group statRange"),
	UNAUTHORIZATION_STATRANGE_UNIT(8002, "StatIds do not have no authorization under the unit statRange"),
	UNAUTHORIZATION_STATRANGE_CTKEYWORD(8003, "StatIds do not have no authorization under the keyword statRange"),
	UNAUTHORIZATION_STATRANGE_QTKEYWORD(8004, "StatIds do not have no authorization under the keyword statRange"),
	UNEXPECTED_PARAMETER_STATIDS(8005, "StatIds can not be empty under the statRange"),
	UNEXPECTED_PARAMETER_TOO_LONG_STATIDS(8006, "StatIds is too long"),
	UNEXPECTED_PARAMETER_STATRANGE_REPORTTYPE_MISMATCH(8007, "Report type and statRange mismatch"),
	UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE(8008, "End date is not after start date"),
	UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_TOO_EARLY(8009, "Date should be after 2008-11-13"),
	UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_CT_TOO_EARLY(8010, "When query keyword report, start date should be after 2011-06-23"),
	UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_QT_TOO_EARLY(8011, "When query keyword report, start date should be after 2011-06-23"),
	WRONG_REPORTID(8012, "reportId is not valid"),
	GET_FILEURL_FAIL(8013,"the report file cannot be found"),
	GET_REPORTSTATE_NOTFOUND(8014,"the report id cannot be found"),
	REPORT_FORBIDDEN_DUE_TO_LOADCONTROL(8015,"your request too frequently, hold on for a while"),
	;

	private int value = 0;
	private String message = null;

	private ReportErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

}
