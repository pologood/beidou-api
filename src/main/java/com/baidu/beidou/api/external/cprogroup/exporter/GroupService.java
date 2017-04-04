package com.baidu.beidou.api.external.cprogroup.exporter;

import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByGroupIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupIdByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAdditionalGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateGroupRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * ClassName: GroupService
 * Function: 推广组基本设置，包括：新增及修改推广组，获取推广组信息等
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public interface GroupService {

	/**
	 * 添加推广组：group添加成功，并且有一个Ad添加成功，则建立整个推广组，否则全部不建立。
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return GroupType 包含新增分配的推广组ID
	 */
	public ApiResult<GroupType> addGroup(DataPrivilege user, 
			AddGroupRequest request, ApiOption apiOption);

	
	/**
	 * 查询推广计划下所有推广组
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return GroupType
	 */
	public ApiResult<GroupType> getGroupByCampaignId(DataPrivilege user, 
			GetGroupByCampaignIdRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广计划下的所有推广组ID
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return Long 推广组ID
	 */
	public ApiResult<Long> getGroupIdByCampaignId(DataPrivilege user, 
			GetGroupIdByCampaignIdRequest request, ApiOption apiOption);
	
	/**
	 * 查询指定推广组信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return GroupType
	 */
	public ApiResult<GroupType> getGroupByGroupId(DataPrivilege user, 
			GetGroupByGroupIdRequest request, ApiOption apiOption);
	
	
	/**
	 * 修改推广组
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request 必须指定groupId, 推广组的状态可以为：有效，搁置，删除
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return GroupType
	 */
	public ApiResult<GroupType> updateGroup(DataPrivilege user, 
			UpdateGroupRequest request, ApiOption apiOption);
	
	/**
	 * 修改推广组一些重要的状态位以及出价 <br>
	 * 专供Editor使用
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request 必须指定groupId
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return AdditionalGroupType
	 */
	public ApiResult<AdditionalGroupType> updateAdditionalGroup(DataPrivilege user, 
			UpdateAdditionalGroupRequest request, ApiOption apiOption);
	
}
