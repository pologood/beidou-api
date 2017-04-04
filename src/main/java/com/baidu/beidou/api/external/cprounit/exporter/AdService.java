package com.baidu.beidou.api.external.cprounit.exporter;

import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.cprounit.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.CopyAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.DeleteAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByAdIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdIdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.ReplaceAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.SetAdStatusRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * ClassName: AdService
 * Function: unit层级对外接口
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public interface AdService {

	/**
	 * 批量添加创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return AdType 包含新增分配的推广创意ID
	 */
	public ApiResult<AdType> addAd(DataPrivilege user,
			AddAdRequest request, ApiOption apiOption);

	/**
	 * 批量删除创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return
	 */
	public ApiResult<Object> deleteAd(DataPrivilege user, 
			DeleteAdRequest request, ApiOption apiOption);

	/**
	 * 查询推广组下的全部创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return AdType
	 */
	public ApiResult<AdType> getAdByGroupId(DataPrivilege user,
			GetAdByGroupIdRequest request, ApiOption apiOption);

	/**
	 * 查询推广组下的全部创意ID
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return Long 推广创意ID
	 */
	public ApiResult<Long> getAdIdByGroupId(DataPrivilege user,
			GetAdIdByGroupIdRequest request, ApiOption apiOption);

	/**
	 * 查询指定的创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return AdType
	 */
	public ApiResult<AdType> getAdByAdId(DataPrivilege user,
			GetAdByAdIdRequest request, ApiOption apiOption);

	/**
	 * 修改创意的状态
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return
	 */
	public ApiResult<Object> setAdStatus(DataPrivilege user, 
			SetAdStatusRequest request, ApiOption apiOption);

	/**
	 * 批量修改创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return AdType 更新后的创意信息
	 */
	public ApiResult<AdType> updateAd(DataPrivilege user,
			UpdateAdRequest request, ApiOption apiOption);
	
	/**
	 * 批量替换创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return
	 */
	public ApiResult<Object> replaceAd(DataPrivilege user,
			ReplaceAdRequest request, ApiOption apiOption);
	
	/**
	 * 批量复制创意
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return
	 */
	public ApiResult<Object> copyAd(DataPrivilege user,
			CopyAdRequest request, ApiOption apiOption);
}
