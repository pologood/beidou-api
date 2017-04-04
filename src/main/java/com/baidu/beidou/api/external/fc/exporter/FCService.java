package com.baidu.beidou.api.external.fc.exporter;

import com.baidu.beidou.api.external.fc.vo.FCCampaignType;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitIdType;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitType;
import com.baidu.beidou.api.external.fc.vo.FCUnitType;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCUnitIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitIdByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitIdByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;

/**
 * 
 * ClassName: FCServiceImpl  <br>
 * Function: FC推广计划、推广单元查询
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
@RPCService(serviceName = "FCService")
public interface FCService {
	
	/**
	 * 获取所有FC推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCCampaignType> 包括推广计划id和名称列表
	 */
	public ApiResult<FCCampaignType> getFCCampaign(DataPrivilege user, GetFCCampaignRequest request, ApiOption apiOption);

	/**
	 * 获取所有FC推广计划id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广计划id列表
	 */
	public ApiResult<Long> getFCCampaignId(DataPrivilege user, GetFCCampaignIdRequest request, ApiOption apiOption);
	
	/**
	 * 根据指定id获取所有FC推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCCampaignType> 包括推广计划id和名称列表
	 */
	public ApiResult<FCCampaignType> getFCCampaignByFCCampaignId(DataPrivilege user, GetFCCampaignByFCCampaignIdRequest request, ApiOption apiOption);
	
	/**
	 * 根据推广计划id获取其下所有推广单元
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCUnitType> 包括推广单元id和名称列表
	 */
	public ApiResult<FCUnitType> getFCUnitByFCCampaignId(DataPrivilege user, GetFCUnitByFCCampaignIdRequest request, ApiOption apiOption);
	
	
	/**
	 * 根据推广计划id列表获取其下所有推广单元
	 * 
	 * @param user
	 *            用户信息，包含操作者和被操作者id
	 * @param param
	 *            调用参数
	 * @param options
	 *            用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse<AdInfo> 包含创意信息
	 */
	@RPCMethod(methodName = "getFCUnitByFCCampaignIds", returnType = ReturnType.ARRAY)
	public BaseResponse<FCCampaignUnitType> getFCUnitByFCCampaignIds(BaseRequestUser user, GetFCUnitByFCCampaignIdsRequest param, BaseRequestOptions options);

	/**
	 * 根据推广计划id获取其下所有推广单元id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广单元id列表
	 */
	public ApiResult<Long> getFCUnitIdByFCCampaignId(DataPrivilege user, GetFCUnitIdByFCCampaignIdRequest request, ApiOption apiOption);

	/**
	 * 根据推广计划id列表获取其下所有推广单元id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广单元id列表
	 */
	@RPCMethod(methodName = "getFCUnitIdByFCCampaignIds", returnType = ReturnType.ARRAY)
	public BaseResponse<FCCampaignUnitIdType> getFCUnitIdByFCCampaignIds(BaseRequestUser user, GetFCUnitIdByFCCampaignIdsRequest param, BaseRequestOptions options);
		
	/**
	 * 根据推广单元id获取推广单元信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCUnitType> 包括推广单元id和名称列表
	 */
	public ApiResult<FCUnitType> getFCUnitByFCUnitId(DataPrivilege user, GetFCUnitByFCUnitIdRequest request, ApiOption apiOption);
	
}
