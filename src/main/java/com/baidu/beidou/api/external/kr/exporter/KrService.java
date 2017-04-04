package com.baidu.beidou.api.external.kr.exporter;

import com.baidu.beidou.api.external.kr.vo.KRResponse;
import com.baidu.beidou.api.external.kr.vo.KRResultType;
import com.baidu.beidou.api.external.kr.vo.KRSuggestResultType;
import com.baidu.beidou.api.external.kr.vo.request.GetKRBySeedRequest;
import com.baidu.beidou.api.external.kr.vo.request.GetKRSuggestBySeedRequest;
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
 * ClassName: KrService  <br>
 * Function: 关键词推荐
 *
 * @author zhangxu
 * @date Aug 14, 2012
 */
@RPCService(serviceName = "KrService")
public interface KrService {

	/**
	 * 获取推荐关键词
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getKRBySeed", returnType = ReturnType.ARRAY)
	public BaseResponse<KRResultType> getKRBySeed(BaseRequestUser user, GetKRBySeedRequest param, BaseRequestOptions options);
	
	/**
	 * 关键词推荐同时，获取“相关推荐” 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<KRSuggestResultType> 返回种子词推荐种子词
	 */
	public ApiResult<KRSuggestResultType> getKRSuggestBySeed(DataPrivilege user, GetKRSuggestBySeedRequest request, ApiOption apiOption);
	
	/**
	 * 关键词推荐新接口，该接口同时返回推荐关键词和相关推荐词
	 * @param userexternal
	 * @param request
	 * @param apiOption用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return
	 * 下午7:12:20 created by qianlei
	 */
	public ApiResult<KRResponse> getKRAndSuggestBySeed(DataPrivilege user, GetKRBySeedRequest request, ApiOption apiOption);
	
}
