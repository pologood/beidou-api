package com.baidu.beidou.api.external.cprogroup.service;

import java.util.List;

import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.tool.vo.OptContent;

/**
 * ClassName: ApiGroupService
 * Function: 推广组新增及修改内部service
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public interface ApiGroupService {
	
	//整个添加推广组的函数有问题，不能使用groupMgr.commit，应该使用groupMgr.add
	/**
	 * addGroup: 新增推广组
	 * @version ApiGroupService
	 * @author genglei01
	 * @date 2012-1-9
	 */
	public ApiResult<GroupType> addGroup(ApiResult<GroupType> result,
			int index, GroupType group, List<OptContent> optContents);
	
	/**
	 * updateGroup: 修改推广组
	 * @version ApiGroupService
	 * @author genglei01
	 * @date 2012-1-9
	 */
	public ApiResult<GroupType> updateGroup(ApiResult<GroupType> result, 
			DataPrivilege user, GroupType[] groupTypes);
	
	/**
	 * updateGroup: 修改推广组一些重要状态位和出价
	 * @version ApiGroupService
	 * @author zhangxu04
	 * @date 2012-11-02
	 */
	public ApiResult<AdditionalGroupType> updateAdditionalGroup(ApiResult<AdditionalGroupType> result, 
			DataPrivilege user, AdditionalGroupType[] groupTypes);

}
