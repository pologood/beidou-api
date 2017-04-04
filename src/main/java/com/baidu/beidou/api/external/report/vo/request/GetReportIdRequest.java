package com.baidu.beidou.api.external.report.vo.request;

import com.baidu.beidou.api.external.report.vo.ReportRequestType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetReportIdRequest implements ApiRequest{

	private ReportRequestType reportRequestType;

	public ReportRequestType getReportRequestType() {
		return reportRequestType;
	}

	public void setReportRequestType(ReportRequestType reportRequest) {
		this.reportRequestType = reportRequest;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
	
}
