package com.baidu.beidou.api.external.cprogroup.exporter;

import com.baidu.beidou.api.external.cprogroup.vo.ExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSimilarPeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlType;
import com.baidu.beidou.api.external.cprogroup.vo.TargetInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.TradeSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.WordType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteAttachInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeletePackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetAttachInfosRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordByWordIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSimilarPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSiteConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTradeSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTradeSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAttachInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateSimilarPeopleRequest;
import com.baidu.beidou.api.external.tool.vo.request.AttachInfoUserRequestType;
import com.baidu.beidou.api.external.tool.vo.response.AttachInfoUserResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;

/**
 * ClassName: GroupConfigService
 * Function: 推广组设置接口
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
@RPCService(serviceName = "GroupConfigService")
public interface GroupConfigService {
	
	/**
	 * 查询推广组的定向方式及配置信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return TargetInfoType
	 */
	public ApiResult<TargetInfoType> getTargetInfo(DataPrivilege user, 
			GetTargetInfoRequest request, ApiOption apiOption);

	/**
	 * 查询推广组网站信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SiteConfigType
	 */
	public ApiResult<SiteConfigType> getSiteConfig(DataPrivilege user, 
			GetSiteConfigRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广组地域信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return RegionConfigType
	 */
	public ApiResult<RegionConfigType> getRegionConfig(DataPrivilege user, 
			GetRegionConfigRequest request, ApiOption apiOption);
		
	/**
	 * 查询推广组的站点过滤信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ExcludeSiteType
	 */
	public ApiResult<ExcludeSiteType> getExcludeSite(DataPrivilege user, 
			GetExcludeSiteRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广组的IP过滤信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ExcludeIpType
	 */
	public ApiResult<ExcludeIpType> getExcludeIp(DataPrivilege user, 
			GetExcludeIpRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广组的分网站点击链接
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SiteUrlType
	 */
	public ApiResult<SiteUrlType> getSiteUrl(DataPrivilege user, 
			GetSiteUrlRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广组的分行业/分网站出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return TradeSitePriceType
	 */
	public ApiResult<TradeSitePriceType> getTradeSitePrice(DataPrivilege user, 
			GetTradeSitePriceRequest request, ApiOption apiOption);
	
	/**
	 * 查询推广组的兴趣设置
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return TradeSitePriceType
	 */
	public ApiResult<InterestInfoType> getInterestInfo(DataPrivilege user, 
			GetInterestInfoRequest request, ApiOption apiOption);
	
	/**
	 * 根据keywordid获取keyword字面
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return KeywordType
	 */
	public ApiResult<KeywordType> getKeyword(DataPrivilege user,
			GetKeywordRequest request, ApiOption apiOption);
	
	/**
	 * getKeywordByWordId: 根据wordid获取keyword字面
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return WordType
	 * 
	 * @version beidou-api 3 plus
	 * @author genglei01
	 * @date 2012-9-28
	 */
	@RPCMethod(methodName = "getKeywordByWordId", returnType = ReturnType.ARRAY)
	public BaseResponse<WordType> getKeywordByWordId(BaseRequestUser user,
			GetKeywordByWordIdRequest param, BaseRequestOptions apiOption);
	
	/**
	 * 设置推广组的定向方式及配置信息
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setTargetInfo(DataPrivilege user, 
			SetTargetInfoRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的网站定向
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setSiteConfig(DataPrivilege user, 
			SetSiteConfigRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的地域定向
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setRegionConfig(DataPrivilege user, 
			SetRegionConfigRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的网站过滤
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setExcludeSite(DataPrivilege user, 
			SetExcludeSiteRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的IP过滤
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setExcludeIp(DataPrivilege user, 
			SetExcludeIpRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的分行业/分网站出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setTradeSitePrice(DataPrivilege user, 
			SetTradeSitePriceRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组的分网站点击链接
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setSiteUrl(DataPrivilege user, 
			SetSiteUrlRequest request, ApiOption apiOption);
	
	/**
	 * 设置关键词
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setKeyword(DataPrivilege user, 
			SetKeywordRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组受众兴趣
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> setInterestInfo(DataPrivilege user, 
			SetInterestInfoRequest request, ApiOption apiOption);
	
	/**
	 * 添加关键词
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addKeyword(DataPrivilege user, 
			AddKeywordRequest request, ApiOption apiOption);
	
	/**
	 * 添加RT关联关系
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addRtRelation(DataPrivilege user, 
			AddRtRelationRequest request, ApiOption apiOption);
	
	/**
	 * 添加VT人群设置
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addVtPeople(DataPrivilege user, 
			AddVtPeopleRequest request, ApiOption apiOption);
	
	/**
	 * 添加投放网站
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addSite(DataPrivilege user, 
			AddSiteRequest request, ApiOption apiOption);
	
	/**
	 * 添加投放行业
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addTrade(DataPrivilege user, 
			AddTradeRequest request, ApiOption apiOption);
	
	/**
	 * 添加投放地域
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addRegion(DataPrivilege user, 
			AddRegionRequest request, ApiOption apiOption);
	
	/**
	 * 添加过滤IP
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addExcludeIp(DataPrivilege user, 
			AddExcludeIpRequest request, ApiOption apiOption);
	
	/**
	 * 添加过滤网站
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addExcludeSite(DataPrivilege user, 
			AddExcludeSiteRequest request, ApiOption apiOption);
	
	/**
	 * 添加分行业出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addTradePrice(DataPrivilege user, 
			AddTradePriceRequest request, ApiOption apiOption);
	
	/**
	 * 添加分网站出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addSitePrice(DataPrivilege user, 
			AddSitePriceRequest request, ApiOption apiOption);
	
	/**
	 * 添加受众兴趣
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> addInterestInfo(DataPrivilege user, 
			AddInterestInfoRequest request, ApiOption apiOption);
	
	/**
	 * 删除关键词
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteKeyword(DataPrivilege user, 
			DeleteKeywordRequest request, ApiOption apiOption);
	
	/**
	 * 删除RT关联关系
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteRtRelation(DataPrivilege user, 
			DeleteRtRelationRequest request, ApiOption apiOption);
	
	/**
	 * 删除VT人群设置
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteVtPeople(DataPrivilege user, 
			DeleteVtPeopleRequest request, ApiOption apiOption);
	
	/**
	 * 删除投放网站
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteSite(DataPrivilege user, 
			DeleteSiteRequest request, ApiOption apiOption);
	
	/**
	 * 删除投放行业
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteTrade(DataPrivilege user, 
			DeleteTradeRequest request, ApiOption apiOption);
	
	/**
	 * 删除投放地域
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteRegion(DataPrivilege user, 
			DeleteRegionRequest request, ApiOption apiOption);
	
	/**
	 * 删除过滤IP
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteExcludeIp(DataPrivilege user, 
			DeleteExcludeIpRequest request, ApiOption apiOption);
	
	/**
	 * 删除过滤网站
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteExcludeSite(DataPrivilege user, 
			DeleteExcludeSiteRequest request, ApiOption apiOption);
	
	/**
	 * 删除分行业出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteTradePrice(DataPrivilege user, 
			DeleteTradePriceRequest request, ApiOption apiOption);
	
	/**
	 * 删除分网站出价
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteSitePrice(DataPrivilege user, 
			DeleteSitePriceRequest request, ApiOption apiOption);
	
	/**
	 * 删除受众兴趣
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return SuccessObject
	 */
	public ApiResult<Object> deleteInterestInfo(DataPrivilege user, 
			DeleteInterestInfoRequest request, ApiOption apiOption);
	
	/**
	 * 设置推广组排除关键词
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "setExcludeKeyword", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> setExcludeKeyword(BaseRequestUser user, SetExcludeKeywordRequest param, BaseRequestOptions options);
	
	/**
	 * 查询推广组排除关键词
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getExcludeKeyword", returnType = ReturnType.ARRAY)
	public BaseResponse<ExcludeKeywordType> getExcludeKeyword(BaseRequestUser user, GetExcludeKeywordRequest param, BaseRequestOptions options);
	
	/**
	 * 添加推广组排除关键词
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "addExcludeKeyword", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> addExcludeKeyword(BaseRequestUser user, AddExcludeKeywordRequest param, BaseRequestOptions options);
	
	/**
	 * 删除推广组排除关键词
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "deleteExcludeKeyword", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> deleteExcludeKeyword(BaseRequestUser user, DeleteExcludeKeywordRequest param, BaseRequestOptions options);
	
	/**
	 * 设置推广组排除人群
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "setExcludePeople", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> setExcludePeople(BaseRequestUser user, SetExcludePeopleRequest param, BaseRequestOptions options);
	
	/**
	 * 查询推广组排除人群
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getExcludePeople", returnType = ReturnType.ARRAY)
	public BaseResponse<ExcludePeopleType> getExcludePeople(BaseRequestUser user, GetExcludePeopleRequest  param, BaseRequestOptions options);
	
	/**
	 * 添加推广组排除人群
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "addExcludePeople", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> addExcludePeople(BaseRequestUser user, AddExcludePeopleRequest param, BaseRequestOptions options);
	
	/**
	 * 删除推广组排除人群
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "deleteExcludePeople", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> deleteExcludePeople(BaseRequestUser user, DeleteExcludePeopleRequest  param, BaseRequestOptions options);
	
	/**
	 * 设置推广组受众组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "setPackInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> setPackInfo(BaseRequestUser user, SetPackInfoRequest param, BaseRequestOptions options);
	
	/**
	 * 查询推广组受众组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getPackInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<PackInfoType> getPackInfo(BaseRequestUser user, GetPackInfoRequest  param, BaseRequestOptions options);
	
	/**
	 * 添加推广组受众组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "addPackInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> addPackInfo(BaseRequestUser user, AddPackInfoRequest  param, BaseRequestOptions options);
	
	/**
	 * 删除推广组受众组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "deletePackInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> deletePackInfo(BaseRequestUser user, DeletePackInfoRequest  param, BaseRequestOptions options);
	
	/**
	 * 出价设置
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "setPrice", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> setPrice(BaseRequestUser user, SetPriceRequest param, BaseRequestOptions options);
	
	/**
	 * 查询出价
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getPrice", returnType = ReturnType.ARRAY)
	public BaseResponse<PriceType> getPrice(BaseRequestUser user, GetPriceRequest param, BaseRequestOptions options);
	
	/**
	 * 获取附加创意
	 * @param user
	 * @param param
	 * @param options
	 * @return
	 */
	@RPCMethod(methodName = "getAttachInfos", returnType = ReturnType.ARRAY)
	public BaseResponse<GroupAttachInfoType> getAttachInfos(BaseRequestUser user, GetAttachInfosRequest param, BaseRequestOptions options);
	
	/**
	 * 批量删除附加创意
	 * @param user
	 * @param param
	 * @param options
	 * @return
	 */
	@RPCMethod(methodName = "deleteAttachInfos", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> deleteAttachInfos(BaseRequestUser user, DeleteAttachInfoRequest param, BaseRequestOptions options);
	
	/**
	 * 更新附加创意信息
	 * @param user
	 * @param param
	 * @param options
	 * @return
	 */
	@RPCMethod(methodName = "updateAttachInfos", returnType = ReturnType.ARRAY)
	public BaseResponse<PlaceHolderResult> updateAttachInfos(BaseRequestUser user, UpdateAttachInfoRequest param, BaseRequestOptions options);
	
	/**
	 * 判断用户是否开通商桥
	 */
	@RPCMethod(methodName = "isBridgeUser", returnType = ReturnType.ARRAY)
	public BaseResponse<AttachInfoUserResponse> isBridgeUser(BaseRequestUser user,
			AttachInfoUserRequestType param, BaseRequestOptions apiOption);
	
	/**
	 * 
	 * 获取排除移动应用
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GroupExcludeAppType> getExcludeApp(DataPrivilege user,
			GetExcludeAppRequest request, ApiOption apiOption);
	
	/**
	 * 
	 * 新增排除移动应用
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<Object> addExcludeApp(DataPrivilege user,
			AddExcludeAppRequest request, ApiOption apiOption);
	
	/**
	 * 
	 * 删除排除移动应用
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<Object> deleteExcludeApp(DataPrivilege user,
			DeleteExcludeAppRequest request, ApiOption apiOption);

    /**
     * 获取相似人群设置
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 请求参数，包含需要查询的推广组ID
     * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return 相似人群设置
     */
    @RPCMethod(methodName = "getSimilarPeople", returnType = ReturnType.ARRAY)
    public BaseResponse<GroupSimilarPeopleType> getSimilarPeople(BaseRequestUser user, GetSimilarPeopleRequest param,
            BaseRequestOptions apiOption);

    /**
     * 修改相似人群设置
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 请求参数，包括推广组ID，相似人群设置
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return 修改结果
     */
    @RPCMethod(methodName = "updateSimilarPeople", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> updateSimilarPeople(BaseRequestUser user, UpdateSimilarPeopleRequest param,
            BaseRequestOptions options);
}
