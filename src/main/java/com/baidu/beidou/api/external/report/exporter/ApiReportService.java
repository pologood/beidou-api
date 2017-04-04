package com.baidu.beidou.api.external.report.exporter;

import com.baidu.beidou.api.external.report.vo.request.GetReportFileUrlRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportIdRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportStateRequest;
import com.baidu.beidou.api.external.report.vo.response.GetReportFileUrlResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportStateResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: ApiReportService  <br>
 * Function: 报告服务
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public interface ApiReportService {

	/**
	 * 
	 * 获取报告reportId <br>
	 * 
	 * 主要功能为： <br>
	 * 1. 验证请求包装类参数 <br>
	 * 2. 插入数据库新的任务 <br>
	 * 3. 插入新到任务到JMS中 <br>
	 * 4. 返回reportId <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportIdResponse> getReportId(DataPrivilege user,
			GetReportIdRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 查询reportId状态 <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportStateResponse> getReportState(DataPrivilege user,
			GetReportStateRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 获取报告reportId对应到下载url
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportFileUrlResponse> getReportFileUrl(DataPrivilege user,
			GetReportFileUrlRequest request, 
			ApiOption apiOption);
	
}
