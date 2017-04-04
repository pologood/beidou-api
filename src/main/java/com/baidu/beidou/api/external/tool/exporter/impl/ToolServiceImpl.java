package com.baidu.beidou.api.external.tool.exporter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.vo.AbstractStatViewItem;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.tool.constant.ToolConstant;
import com.baidu.beidou.api.external.tool.error.ToolErrorCode;
import com.baidu.beidou.api.external.tool.exporter.ToolService;
import com.baidu.beidou.api.external.tool.service.GenericToolStatService;
import com.baidu.beidou.api.external.tool.service.vo.AbstractReportData;
import com.baidu.beidou.api.external.tool.service.vo.ReportDataFactory;
import com.baidu.beidou.api.external.tool.validator.ApiReportValidator;
import com.baidu.beidou.api.external.tool.vo.AdInfo;
import com.baidu.beidou.api.external.tool.vo.AdvancedPackType;
import com.baidu.beidou.api.external.tool.vo.FCKeyword;
import com.baidu.beidou.api.external.tool.vo.KeywordPackType;
import com.baidu.beidou.api.external.tool.vo.LastHistoryResponseType;
import com.baidu.beidou.api.external.tool.vo.OneReportRequestType;
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
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.GroupPackMgr;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.fengchao.BDUnit;
import com.baidu.beidou.fengchao.FcFacade;
import com.baidu.beidou.pack.bo.WordPack;
import com.baidu.beidou.pack.bo.WordPackKeyword;
import com.baidu.beidou.pack.service.WordPackKeywordMgr;
import com.baidu.beidou.pack.service.WordPackMgr;
import com.baidu.beidou.tool.service.OptHistoryMgr;
import com.baidu.beidou.tool.vo.OptRecordVo;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.DateUtils;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;
import com.google.common.base.Objects;

/**
 * 
 * ClassName: ToolService <br>
 * Function: 工具服务接口定义
 * 
 * @author zhangxu
 * @date Aug 20, 2012
 */
@RPCService(serviceName = "ToolService")
public class ToolServiceImpl implements ToolService {
	
	private static final Log log = LogFactory.getLog(ToolServiceImpl.class);

	private CproUnitMgr unitMgr;

    private FcFacade fcFacade;
	
	private WordPackMgr wordPackMgr;
	
	private GroupPackMgr groupPackMgr;
	
	private WordPackKeywordMgr wordPackKeywordMgr;
	//add by caichao
	private UserMgr userMgr;
	
	private OptHistoryMgr optHistoryMgr;
	private ApiReportValidator apiReportValidator;
	private  GenericToolStatService<AbstractStatViewItem> accountStatToolService;
	private  GenericToolStatService<AbstractStatViewItem> planStatToolService;
	private  GenericToolStatService<AbstractStatViewItem> groupStatToolService;
	private  GenericToolStatService<AbstractStatViewItem> unitStatToolService;
	//end
	private int adIdsMax;
	private int gpidsMax;
	private int sitesMax;
	private int fcUnitIdsMax;
	private int getKeywordPackByIdMax;
	private int getAdvancedPackByIdMax;

	/**
	 * 查询创意其他信息，包括状态和拒绝理由等
	 * 
	 * @param user
	 *            用户信息，包含操作者和被操作者id
	 * @param param
	 *            调用参数
	 * @param options
	 *            用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse
	 */
	@RPCMethod(methodName = "getAdInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<AdInfo> getAdInfo(BaseRequestUser user, GetAdInfoRequest param, BaseRequestOptions options) {

		BaseResponse<AdInfo> response = new BaseResponse<AdInfo>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user,param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

		// 检查传入的创意id列表不为空
		long[] adIds = param.getAdIds();
		if (ArrayUtils.isEmpty(adIds)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.ADIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (adIds.length > adIdsMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.ADIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.AD_TOOMANY_NUM.getValue(),
					ToolErrorCode.AD_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 配额总数设为查询数量
		option.setTotal(adIds.length);

		// 遍历adId查询数据库取出创意信息，对于删除或者userid不符合的过滤掉
		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();
		for (int idIndex = 0; idIndex < adIds.length; idIndex++) {
			UnitInfoView unitInfo = unitMgr.findUnitById(userId, adIds[idIndex]);

			if (unitInfo == null
					|| unitInfo.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.ADIDS, idIndex);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						ToolErrorCode.NO_UNIT.getValue(), ToolErrorCode.NO_UNIT
								.getMessage(), apiPosition.getPosition(), null);
				continue;
			}

			if (!unitInfo.getUserid().equals(userId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.ADIDS, idIndex);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						ToolErrorCode.WRONG_USER.getValue(),
						ToolErrorCode.WRONG_USER.getMessage(), apiPosition.getPosition(), null);
			} else {
				unitList.add(unitInfo);
			}
		}

		// 转换返回结果
		List<AdInfo> result = new ArrayList<AdInfo>(unitList.size());
		Mapper mapper = BeanMapperProxy.getMapper();
		for (UnitInfoView vo : unitList) {
			AdInfo adInfoVo = mapper.map(vo, AdInfo.class);
			result.add(adInfoVo);
		}

		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new AdInfo[0]));

		return response;
	}

	/**
	 * 查询网站行业等冗余信息
	 * 
	 * @param user
	 *            用户信息，包含操作者和被操作者id
	 * @param param
	 *            调用参数
	 * @param options
	 *            用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse
	 */
	@RPCMethod(methodName = "getSiteInfo", returnType = ReturnType.ARRAY)
	public BaseResponse<SiteInfo> getSiteInfo(BaseRequestUser user, GetSiteInfoRequest param, BaseRequestOptions options) {

		BaseResponse<SiteInfo> response = new BaseResponse<SiteInfo>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user,param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		//int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

		// 检查传入的网站url列表不为空
		String[] sites = param.getSites();
		if (ArrayUtils.isEmpty(sites)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.SITES);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (sites.length > adIdsMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.SITES);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.SITE_TOOMANY_NUM.getValue(),
					ToolErrorCode.SITE_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 配额总数设为查询数量
		option.setTotal(sites.length);

		List<SiteInfo> result = new ArrayList<SiteInfo>(sites.length);
		Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
		for (int i = 0; i < sites.length; i++) {
			Integer siteId = siteIdMap.get(sites[i]);
			if (siteId == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.SITES, i);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						ToolErrorCode.SITE_NOT_FOUND.getValue(),
						ToolErrorCode.SITE_NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			BDSiteInfo site = UnionSiteCache.siteInfoCache.getSiteInfoBySiteId(siteId);
			SiteInfo siteInfo = new SiteInfo();
			siteInfo.setSite(sites[i]);
			siteInfo.setFirstTradeId(site.getFirsttradeid());
			siteInfo.setSecondTradeId(site.getSecondtradeid());
			result.add(siteInfo);
		}

		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new SiteInfo[0]));

		return response;
	}

	/**
	 * 根据搜索推广单元id获取关键词
	 * 
	 * @param user
	 *            用户信息，包含操作者和被操作者id
	 * @param param
	 *            调用参数
	 * @param options
	 *            用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse
	 */
	@RPCMethod(methodName = "getFCKeywordByUnitIds", returnType = ReturnType.ARRAY)
	public BaseResponse<FCKeyword> getFCKeywordByUnitIds(BaseRequestUser user,
			GetFCKeywordByUnitIdsRequest param, BaseRequestOptions options) {

		BaseResponse<FCKeyword> response = new BaseResponse<FCKeyword>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

		// 检查传入的unitIds列表不为空
		long[] unitIds = param.getUnitIds();
		if (ArrayUtils.isEmpty(unitIds)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.UNITIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (unitIds.length > fcUnitIdsMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.UNITIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.FCUNIT_TOOMANY_NUM.getValue(),
					ToolErrorCode.FCUNIT_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		// 配额总数设为查询数量
		option.setTotal(unitIds.length);

		// 验证权限, 先查询用户所有的单元id
		List<Long> idBatchList = CollectionsUtil.tranformLongArrayToLongList(unitIds);
		Set<Long> unitIdSet = new HashSet<Long>();
		List<BDUnit> fcUnits = fcFacade.getFcUnitListByUnitids(userId,idBatchList,null);
		if(CollectionUtils.isNotEmpty(fcUnits)){
			for(BDUnit fcUnit: fcUnits){
				unitIdSet.add(fcUnit.getUnitid());	
			}
		}
		
		// 根据unitId获取凤巢关键词
		List<FCKeyword> result = new ArrayList<FCKeyword>(unitIds.length);
		try {
			for (int i = 0; i < unitIds.length; i++) {
				if (!unitIdSet.contains(unitIds[i])) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(ToolConstant.UNITIDS, i);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNAUTHORIZATION.getValue(),
							GlobalErrorCode.UNAUTHORIZATION.getMessage(), apiPosition.getPosition(), null);
					continue;
				}
				FCKeyword fcKeyword = new FCKeyword();
				List<Long> unitIdList = new ArrayList<Long>();
				unitIdList.add(unitIds[i]);
				List<String> keywords = fcFacade.getFcKeywordsByUnitIds(
						userId, 
						unitIdList,
						CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM);
				fcKeyword.setUnitId(unitIds[i]);
				fcKeyword.setKeywords(keywords.toArray(new String[]{}));
				result.add(fcKeyword);
			}
		} catch (Exception e) {
			log.error("Failed to get fc keywords", e);
			// 配额总数设为1
			option.setTotal(1);
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.UNITIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.FAIL_TO_GET_FC_KEYWORDS.getValue(),
					ToolErrorCode.FAIL_TO_GET_FC_KEYWORDS.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new FCKeyword[0]));
		
		return response;
	}
	
	/**
	 * 获取用户所有关键词组合id列表
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAllKeywordPackId", returnType = ReturnType.ARRAY)
	public BaseResponse<Integer> getAllKeywordPackId(BaseRequestUser user, GetAllKeywordPackIdRequest param, BaseRequestOptions options){

		BaseResponse<Integer> response = new BaseResponse<Integer>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
		
		List<Integer> wordPackIdList = wordPackMgr.getAllWordPackId(userId);
		
		response.getOptions().setSuccess(1);
		response.setData(wordPackIdList.toArray(new Integer[0]));
		
		return response;
	}

	/**
	 * 根据关键词组合id获取关键词组合具体配置信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getKeywordPackById", returnType = ReturnType.ARRAY)
	public BaseResponse<KeywordPackType> getKeywordPackById(BaseRequestUser user, GetKeywordPackByIdRequest param, BaseRequestOptions options){

		BaseResponse<KeywordPackType> response = new BaseResponse<KeywordPackType>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
		
		// 检查传入的id列表不为空
		int[] ids = param.getIds();
		if (ArrayUtils.isEmpty(ids)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.IDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (ids.length > getKeywordPackByIdMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.IDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.IDS_TOOMANY_NUM.getValue(),
					ToolErrorCode.IDS_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		// 配额总数设为查询数量
		option.setTotal(ids.length);
		
		List<Integer> idList = CollectionsUtil.tranformIntArrayToIntegerList(ids);
		List<Integer> wordPackIdList = wordPackMgr.getAllWordPackId(userId);
		Set<Integer> existWordPackIdSet = new HashSet<Integer>(wordPackIdList);
		
		// 转换返回结果
		List<KeywordPackType> result = new ArrayList<KeywordPackType>(idList.size());
		for (int index = 0; index < idList.size(); index++) {
			int packId = idList.get(index);
			if (!existWordPackIdSet.contains(packId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.IDS, index);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			WordPack vo = wordPackMgr.getById(packId, true);
			KeywordPackType keywordPackType = new KeywordPackType();
			keywordPackType.setId(vo.getId());
			keywordPackType.setName(vo.getName());
			keywordPackType.setOptimized(!Objects.equal(vo.getId().intValue() , vo.getRefPackId().intValue()));
			keywordPackType.setTargetType(vo.getTargetType());
			keywordPackType.setAliveDays(vo.getAliveDays());
			if (CollectionUtils.isNotEmpty(vo.getKeywords())) {
				for (WordPackKeyword kw : vo.getKeywords()) {
					KeywordType type = new KeywordType();
					type.setKeyword(kw.getKeyword());
					keywordPackType.getKeywords().add(type);
				}
			}
			result.add(keywordPackType);
		}
		
		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new KeywordPackType[0]));
		
		return response;
	}

	/**
	 * 获取用户所有高级组合id列表
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAllAdvancedPackId", returnType = ReturnType.ARRAY)
	public BaseResponse<Integer> getAllAdvancedPackId(BaseRequestUser user, GetAllAdvancedPackIdRequest param, BaseRequestOptions options){
		
		BaseResponse<Integer> response = new BaseResponse<Integer>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
//		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
//		List<Integer> wordPackIdList = advancedPackMgr.getAllAdvancedPackIds(userId);
		logAdvancePack();
		
		List<Integer> wordPackIdList = Collections.emptyList();
		
		response.getOptions().setSuccess(1);
		response.setData(wordPackIdList.toArray(new Integer[0]));
		
		return response;
	}
	/**
	 * 高级组合已经下线，记录是否有高级组合流量
	 */
	private void logAdvancePack(){
	    log.info("advancepack has been offline!!!");
	}
	/**
	 * 根据高级组合id获取具体配置信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getAdvancedPackById", returnType = ReturnType.ARRAY)
	public BaseResponse<AdvancedPackType> getAdvancedPackById(BaseRequestUser user, GetAdvancedPackByIdRequest param, BaseRequestOptions options){
		this.logAdvancePack();
		
		BaseResponse<AdvancedPackType> response = new BaseResponse<AdvancedPackType>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
//		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
		
		// 检查传入的id列表不为空
		int[] ids = param.getIds();
		if (ArrayUtils.isEmpty(ids)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.IDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (ids.length > getKeywordPackByIdMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.IDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.IDS_TOOMANY_NUM.getValue(),
					ToolErrorCode.IDS_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		// 配额总数设为查询数量
		option.setTotal(ids.length);
		
		List<Integer> idList = CollectionsUtil.tranformIntArrayToIntegerList(ids);
//		List<Integer> advancedPackIdList = new ArrayList<Integer>();
//		List<Integer> advancedPackIdList = advancedPackMgr.getAllAdvancedPackIds(userId);
//		Set<Integer> existWordPackIdSet = new HashSet<Integer>();
		
		// 转换返回结果
		List<AdvancedPackType> result = new ArrayList<AdvancedPackType>(idList.size());
		for (int index = 0; index < idList.size(); index++) {
//			int packId = idList.get(index);
//			if (!existWordPackIdSet.contains(packId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.IDS, index);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), apiPosition.getPosition(), null);
//				continue;
//			}
//			AdvancedPack bo = advancedPackMgr.getAdvancedPackById(packId, userId);
//			AdvancedPackVo vo = advancedPackMgr.getAdvancedPackVo(packId, userId);
//			AdvancedPackType advancedPackType = new AdvancedPackType();
//			advancedPackType.setId(bo.getId());
//			advancedPackType.setName(bo.getName());
//			advancedPackType.setOptimized(!Objects.equal(bo.getId(), bo.getRefPackId()));
//			
//			List<WordPackVo> wordPacks = vo.getSelectedWordPacks();
//			for (WordPackVo wordPackVo : wordPacks) {
//				BasicPackType basicType = new BasicPackType();
//				basicType.setId(wordPackVo.getPackId());
//				basicType.setType(PackTypeConstant.TYPE_KEYWORD_PACK);
//				advancedPackType.getBasicPacks().add(basicType);
//			}
//			
//			List<CustomInterestVo> customInterests = vo.getSelectedCustomInterests();
//			for (CustomInterestVo customInterestVo : customInterests) {
//				BasicPackType basicType = new BasicPackType();
//				basicType.setId(customInterestVo.getId());
//				basicType.setType(PackTypeConstant.TYPE_INTEREST_PACK);
//				advancedPackType.getBasicPacks().add(basicType);
//			}
//			
//			List<InterestVo> interests = vo.getSelectedInterests();
//			for (InterestVo interestVo : interests) {
//				BasicPackType basicType = new BasicPackType();
//				basicType.setId(interestVo.getId());
//				basicType.setType(PackTypeConstant.TYPE_INTEREST);
//				advancedPackType.getBasicPacks().add(basicType);
//			}
//			
//			result.add(advancedPackType);
		}
		
		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new AdvancedPackType[0]));
		
		return response;
	}
	
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
			GetPackByGroupPackIdRequest param, BaseRequestOptions apiOption) {
		
		BaseResponse<PackType> response = new BaseResponse<PackType>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}

		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

		// 检查传入的id列表不为空
		long[] gpids = param.getGpids();
		if (ArrayUtils.isEmpty(gpids)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.GPIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.PARAM_EMPTY.getValue(),
					ToolErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (gpids.length > gpidsMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ToolConstant.GPIDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					ToolErrorCode.IDS_TOOMANY_NUM.getValue(),
					ToolErrorCode.IDS_TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 配额总数设为查询数量
		option.setTotal(gpids.length);

		List<GroupPack> groupPacks = groupPackMgr.getGroupPacksByIds(CollectionsUtil.tranformLongArrayToLongList(gpids));
		Map<Long, GroupPack> groupPackMap = new HashMap<Long, GroupPack>();
		for (GroupPack groupPack : groupPacks) {
			groupPackMap.put(groupPack.getId(), groupPack);
		}
		
		List<PackType> result = new ArrayList<PackType>(gpids.length);
		for (int i = 0; i < gpids.length; i++) {
			Long gpid = gpids[i];
			if (groupPackMap.containsKey(gpid)) {
				GroupPack groupPack = groupPackMap.get(gpid);
				if (userId != groupPack.getUserId()) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(ToolConstant.GPIDS, i);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNAUTHORIZATION.getValue(),
							GlobalErrorCode.UNAUTHORIZATION.getMessage(), apiPosition.getPosition(), null);
					continue;
				}
				PackType packType = new PackType();
				packType.setId(groupPack.getPackId());
				packType.setType(groupPack.getPackType());
				result.add(packType);
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ToolConstant.GPIDS, i);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						ToolErrorCode.GPID_NOT_EXIST.getValue(),
						ToolErrorCode.GPID_NOT_EXIST.getMessage(), apiPosition.getPosition(), null);
			}
		}

		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new PackType[0]));

		return response;
	}
	
	/**
	 * 请求一站式报告数据，以及有效推广计划总预算
	 * @param parameters
	 * @return
	 * caichao
	 */
	@RPCMethod(methodName = "getOneReport", returnType = ReturnType.ARRAY)
	public BaseResponse<GetOneReportResponse> getOneReport(BaseRequestUser user,
			GetOneReportRequest parameters, BaseRequestOptions apiOption){
		BaseResponse<GetOneReportResponse> response = new BaseResponse<GetOneReportResponse>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, parameters);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}
		
		//验证参数
		OneReportRequestType request = parameters.getOneReportRequestType();
		apiReportValidator.validate(user, request, response);
		if(response.getErrors() != null && !response.getErrors().isEmpty()){
			return response;
		}
		//		option.setTotal(parameters.getDataSize());
		//		response.setOptions(option);
		
		ApiReportQueryParameter parameter = getQueryParameter(request,user);
		GenericToolStatService<AbstractStatViewItem>  service = getService(request.getReportType());
		GetOneReportResponse data = new GetOneReportResponse();
		/**
		 * 查询有效预算总额，并封装doris数据到返回对象中
		 */
		List<AbstractStatViewItem> dorisResponse = null;
		try {
			int sumBudget = 0;
			if(ToolConstant.ISNEEDBUDGET != request.getIsNeedbudget()){
				sumBudget = service.querySumBudget(parameter);
			}
			dorisResponse = service.queryStat(parameter);
			AbstractReportData report = ReportDataFactory.getReportData(request.getReportType());
			data = report.fillData(dorisResponse);
			data.setBudget(sumBudget);
			
		} catch (Exception e) {
			log.error("query data from doris fail "+parameter, e);
		}
		
		option.setTotal(dorisResponse.size());
		response.setOptions(option);
		
		response.getOptions().setSuccess(data.getData().size());
		response.setData(new GetOneReportResponse[]{data});
		return response;
		
	}
	
	
	
	private GenericToolStatService<AbstractStatViewItem> getService(int reportType) {
		switch(reportType){
			case ReportWebConstants.REPORT_TYPE.ACCOUNT : 
				return this.accountStatToolService;
			case ReportWebConstants.REPORT_TYPE.PLAN : 
				return this.planStatToolService;
			case ReportWebConstants.REPORT_TYPE.GROUP : 
				return this.groupStatToolService;
			case ReportWebConstants.REPORT_TYPE.UNIT : 
				return this.unitStatToolService;
			default : return this.accountStatToolService;
		 }
	}

	
    //add 
	/**
	 * 将请求参数封装为doris查询参数
	 * @param request
	 * @param user
	 * @return
	 */ 
	private ApiReportQueryParameter getQueryParameter(OneReportRequestType request,BaseRequestUser user) {
		ApiReportQueryParameter parameter = new ApiReportQueryParameter();
		parameter.setUserid(Long.valueOf(user.getDataUser()).intValue());
		parameter.setStartDate(request.getStartDate());
		// 如果endDate大于今日，endDate设置为今天，这里是唯一改变来request对象的地方
		Date tody23 = DateUtils.getCurDateFloor().getTime();
		if(request.getEndDate().after(tody23)){
			parameter.setEndDate(DateUtils.getCurDateCeil().getTime());
		} else {
			parameter.setEndDate(request.getEndDate());
		}
		parameter.setIdOnly(true);
		parameter.setNeedTransHolmes(false);
		parameter.setNeedUv(false);
		parameter.setReportType(request.getReportType());
		parameter.setIsNeedBudget(request.getIsNeedbudget());
		int statRange = request.getStatRange();
		long[] statIds = request.getStatIds();
		if(statRange == 0){
			statRange = ReportWebConstants.REPORT_RANGE.ACCOUNT;
		}
		if(statRange != ReportWebConstants.REPORT_RANGE.ACCOUNT){
			if(statIds != null && statIds.length > 0){
				List<Integer> planOrGroupIds = new ArrayList<Integer>();
				List<Long> unitIds = new ArrayList<Long>();
				for(long id : statIds){
					planOrGroupIds.add(Long.valueOf(id).intValue());
					unitIds.add(id);
				}
				switch(statRange){
					case ReportWebConstants.REPORT_RANGE.PLAN : 
						parameter.setPlanIds(planOrGroupIds);break;
					case ReportWebConstants.REPORT_RANGE.GROUP : 
						parameter.setGroupIds(planOrGroupIds);break;
					case ReportWebConstants.REPORT_RANGE.UNIT : 
						parameter.setUnitIds(unitIds);
				}
			}
		}
		parameter.setStatRange(statRange);
		return parameter;
	}
	
	@RPCMethod(methodName = "getLastHistory", returnType = ReturnType.ARRAY)
	public BaseResponse<GetLastHistoryResponse> getLastHistory(BaseRequestUser user, GetLastHistoryRequest param, BaseRequestOptions apiOption) {
		BaseResponse<GetLastHistoryResponse> response = new BaseResponse<GetLastHistoryResponse>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);

		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}
		
		int userId = Long.valueOf(user.getDataUser()).intValue();
		GetLastHistoryResponse data = new GetLastHistoryResponse();
		LastHistoryResponseType responseType = new LastHistoryResponseType();
		
		OptRecordVo recordVo = optHistoryMgr.findOptHistoryByUserid(userId);
		responseType.setUserId(userId);
		responseType.setTime(recordVo.getOptimeStr());
		
		data.setData(responseType);
		response.getOptions().setSuccess(1);
		response.setData(new GetLastHistoryResponse[]{data});
		return response;
	}
	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public int getAdIdsMax() {
		return adIdsMax;
	}

	public void setAdIdsMax(int adIdsMax) {
		this.adIdsMax = adIdsMax;
	}

	public int getSitesMax() {
		return sitesMax;
	}

	public void setSitesMax(int sitesMax) {
		this.sitesMax = sitesMax;
	}

	public int getFcUnitIdsMax() {
		return fcUnitIdsMax;
	}

	public void setFcUnitIdsMax(int fcUnitIdsMax) {
		this.fcUnitIdsMax = fcUnitIdsMax;
	}

	public int getGetKeywordPackByIdMax() {
		return getKeywordPackByIdMax;
	}

	public void setGetKeywordPackByIdMax(int getKeywordPackByIdMax) {
		this.getKeywordPackByIdMax = getKeywordPackByIdMax;
	}

	public int getGetAdvancedPackByIdMax() {
		return getAdvancedPackByIdMax;
	}

	public void setGetAdvancedPackByIdMax(int getAdvancedPackByIdMax) {
		this.getAdvancedPackByIdMax = getAdvancedPackByIdMax;
	}

	public int getGpidsMax() {
		return gpidsMax;
	}

	public void setGpidsMax(int gpidsMax) {
		this.gpidsMax = gpidsMax;
	}

	public WordPackMgr getWordPackMgr() {
		return wordPackMgr;
	}

	public void setWordPackMgr(WordPackMgr wordPackMgr) {
		this.wordPackMgr = wordPackMgr;
	}

	public WordPackKeywordMgr getWordPackKeywordMgr() {
		return wordPackKeywordMgr;
	}

	public void setWordPackKeywordMgr(WordPackKeywordMgr wordPackKeywordMgr) {
		this.wordPackKeywordMgr = wordPackKeywordMgr;
	}

	public GroupPackMgr getGroupPackMgr() {
		return groupPackMgr;
	}

	public void setGroupPackMgr(GroupPackMgr groupPackMgr) {
		this.groupPackMgr = groupPackMgr;
	}
    //add by caichao
	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	
	public ApiReportValidator getApiReportValidator() {
		return apiReportValidator;
	}

	public void setApiReportValidator(ApiReportValidator apiReportValidator) {
		this.apiReportValidator = apiReportValidator;
	}

	public GenericToolStatService<AbstractStatViewItem> getAccountStatToolService() {
		return accountStatToolService;
	}

	public void setAccountStatToolService(GenericToolStatService<AbstractStatViewItem> accountStatToolService) {
		this.accountStatToolService = accountStatToolService;
	}

	public GenericToolStatService<AbstractStatViewItem> getPlanStatToolService() {
		return planStatToolService;
	}

	public void setPlanStatToolService(GenericToolStatService<AbstractStatViewItem> planStatToolService) {
		this.planStatToolService = planStatToolService;
	}

	public GenericToolStatService<AbstractStatViewItem> getGroupStatToolService() {
		return groupStatToolService;
	}

	public void setGroupStatToolService(GenericToolStatService<AbstractStatViewItem> groupStatToolService) {
		this.groupStatToolService = groupStatToolService;
	}

	public GenericToolStatService<AbstractStatViewItem> getUnitStatToolService() {
		return unitStatToolService;
	}

	public void setUnitStatToolService(GenericToolStatService<AbstractStatViewItem> unitStatToolService) {
		this.unitStatToolService = unitStatToolService;
	}

	public OptHistoryMgr getOptHistoryMgr() {
		return optHistoryMgr;
	}

	public void setOptHistoryMgr(OptHistoryMgr optHistoryMgr) {
		this.optHistoryMgr = optHistoryMgr;
	}

    public FcFacade getFcFacade() {
        return fcFacade;
    }

    public void setFcFacade(FcFacade fcFacade) {
        this.fcFacade = fcFacade;
    }


	
}
