package com.baidu.beidou.api.external.tool.vo.request;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.tool.vo.OneReportRequestType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.beidou.util.DateUtils;

public class GetOneReportRequest implements ApiRequest {

	private OneReportRequestType oneReportRequestType;

	public OneReportRequestType getOneReportRequestType() {
		return oneReportRequestType;
	}

	public void setOneReportRequestType(OneReportRequestType oneReportRequestType) {
		this.oneReportRequestType = oneReportRequestType;
	}

	public int getDataSize() {
		//return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		if(oneReportRequestType == null){
			return 0;
		}
		int defaultSize = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		if(oneReportRequestType.getStatRange() == ReportWebConstants.REPORT_TYPE.ACCOUNT){
			return defaultSize * DateUtils.getDaysBetweenFromAndTo(oneReportRequestType.getStartDate(), oneReportRequestType.getEndDate());
		}
		if (oneReportRequestType.getStatIds() == null || oneReportRequestType.getStatIds().length == 0) {
			return defaultSize * DateUtils.getDaysBetweenFromAndTo(oneReportRequestType.getStartDate(), oneReportRequestType.getEndDate());
		}

		return oneReportRequestType.getStatIds().length * DateUtils.getDaysBetweenFromAndTo(oneReportRequestType.getStartDate(), oneReportRequestType.getEndDate());
	}
}
