package com.baidu.beidou.api.external.interest.exporter;

import com.baidu.beidou.api.external.interest.vo.CustomInterestType;
import com.baidu.beidou.api.external.interest.vo.InterestType;
import com.baidu.beidou.api.external.interest.vo.request.GetAllCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetInterestRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * InterfaceName: InterestService  <br>
 * Function: 通过InterestService，查询用户的兴趣组合工具
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public interface InterestService {

	/**
	 * 获取系统默认的所有兴趣点
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<InterestType> 兴趣id、名称列表
	 */
	public ApiResult<InterestType> getInterest(DataPrivilege user, GetInterestRequest request, ApiOption apiOption);
	
	/**
	 * 获取用户所有自定义兴趣组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<CustomInterestType> 自定义兴趣组合列表
	 */
	public ApiResult<CustomInterestType> getAllCustomInterest(DataPrivilege user, GetAllCustomInterestRequest request, ApiOption apiOption);
	
	/**
	 * 根据id获取用户自定义兴趣组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<CustomInterestType> 自定义兴趣组合列表
	 */
	public ApiResult<CustomInterestType> getCustomInterest(DataPrivilege user, GetCustomInterestRequest request, ApiOption apiOption);
	
}
