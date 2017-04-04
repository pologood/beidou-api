package com.baidu.beidou.api.external.tool.exporter;

import com.baidu.beidou.api.external.tool.vo.AdInfo;
import com.baidu.beidou.api.external.tool.vo.AdvancedPackType;
import com.baidu.beidou.api.external.tool.vo.FCKeyword;
import com.baidu.beidou.api.external.tool.vo.KeywordPackType;
import com.baidu.beidou.api.external.tool.vo.PackType;
import com.baidu.beidou.api.external.tool.vo.SiteInfo;
import com.baidu.beidou.api.external.tool.vo.request.GetAdInfoRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAdvancedPackByIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAllAdvancedPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAllKeywordPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetFCKeywordByUnitIdsRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetKeywordPackByIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetLastHistoryRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetOneReportRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetPackByGroupPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetSiteInfoRequest;
import com.baidu.beidou.api.external.tool.vo.response.GetLastHistoryResponse;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;

/**
 * 
 * ClassName: ToolService  <br>
 * Function: 工具服务接口定义
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
@RPCService(serviceName = "ToolService")
public interface ToolService {

	/**
	 * 查询创意其他信息，包括状态和拒绝理由等
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAdInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<AdInfo> getAdInfo(BaseRequestUser user, GetAdInfoRequest param, BaseRequestOptions options);
	
	/**
	 * 查询网站行业等冗余信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getSiteInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<SiteInfo> getSiteInfo(BaseRequestUser user, GetSiteInfoRequest param, BaseRequestOptions options);

	/**
	 * 根据搜索推广单元id获取关键词
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getFCKeywordByUnitIds", returnType = ReturnType.ARRAY)
	public BaseResponse<FCKeyword> getFCKeywordByUnitIds(BaseRequestUser user, GetFCKeywordByUnitIdsRequest param, BaseRequestOptions options);

	/**
	 * 获取用户所有关键词组合id列表
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAllKeywordPackId", returnType = ReturnType.ARRAY)
	public BaseResponse<Integer> getAllKeywordPackId(BaseRequestUser user, GetAllKeywordPackIdRequest param, BaseRequestOptions options);

	/**
	 * 根据关键词组合id获取关键词组合具体配置信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getKeywordPackById", returnType = ReturnType.ARRAY)
	public BaseResponse<KeywordPackType> getKeywordPackById(BaseRequestUser user, GetKeywordPackByIdRequest param, BaseRequestOptions options);

	/**
	 * 获取用户所有高级组合id列表
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAllAdvancedPackId", returnType = ReturnType.ARRAY)
	public BaseResponse<Integer> getAllAdvancedPackId(BaseRequestUser user, GetAllAdvancedPackIdRequest param, BaseRequestOptions options);

	/**
	 * 根据高级组合id获取具体配置信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAdvancedPackById", returnType = ReturnType.ARRAY)
	public BaseResponse<AdvancedPackType> getAdvancedPackById(BaseRequestUser user, GetAdvancedPackByIdRequest param, BaseRequestOptions options);

	/**
	 * 根据推广组-受众组合关联关系的gpid获取受众组合id和类型
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getPackByGroupPackId", returnType = ReturnType.ARRAY)
	public BaseResponse<PackType> getPackByGroupPackId(BaseRequestUser user,
			GetPackByGroupPackIdRequest param, BaseRequestOptions apiOption);
	
	
	/**
	 * 请求一站式报告数据，以及有效推广计划总预算
	 * @param parameters
	 * @return
	 * caichao
	 */
	@RPCMethod(methodName = "getOneReport", returnType = ReturnType.ARRAY)
	public BaseResponse<GetOneReportResponse> getOneReport(BaseRequestUser user,
			GetOneReportRequest param, BaseRequestOptions apiOption);
	/**
	 * 返回历史操作记录最后一次操作时间（不区分操作来源）
	 * @param user
	 * @param param
	 * @param apiOption
	 * @return
	 * caichao
	 */
	@RPCMethod(methodName = "getLastHistory", returnType = ReturnType.ARRAY)
	public BaseResponse<GetLastHistoryResponse> getLastHistory(BaseRequestUser user,
			GetLastHistoryRequest param, BaseRequestOptions apiOption);
	
}
