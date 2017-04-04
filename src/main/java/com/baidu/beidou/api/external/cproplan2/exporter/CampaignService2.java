package com.baidu.beidou.api.external.cproplan2.exporter;

import com.baidu.beidou.api.external.cproplan2.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * ClassName: CampaignService
 * Function: 推广计划设置，包含新建、修改及查询
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public interface CampaignService2 {

	/**
	 * addCampaign: 添加推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return CampaignType 包含新增分配的推广计划ID
	 */
	public ApiResult<CampaignType> addCampaign(DataPrivilege user,
			AddCampaignRequest request,	ApiOption apiOption);

	/**
	 * 修改推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return CampaignType
	 */
	public ApiResult<CampaignType> updateCampaign(DataPrivilege user,
			UpdateCampaignRequest request, ApiOption apiOption);

	/**
	 * 查询该用户下的所有推广计划信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return CampaignType
	 */
	ApiResult<CampaignType> getCampaign(DataPrivilege user,
			GetCampaignRequest request, ApiOption apiOption);

	/**
	 * 查询该用户下所有的推广计划ID
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return Long 推广计划ID
	 */
	ApiResult<Long> getCampaignId(DataPrivilege user,
			GetCampaignIdRequest request, ApiOption apiOption);

	/**
	 * 根据指定的推广计划ID获取推广计划信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return CampaignType
	 */
	ApiResult<CampaignType> getCampaignByCampaignId(DataPrivilege user,
			GetCampaignByCampaignIdRequest request, ApiOption apiOption);
}
