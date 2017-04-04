package com.baidu.beidou.api.external.fc.exporter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.fc.constant.FCPlanConstant;
import com.baidu.beidou.api.external.fc.error.FCPlanErrorCode;
import com.baidu.beidou.api.external.fc.exporter.FCService;
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
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.fengchao.BDPlan;
import com.baidu.beidou.fengchao.BDUnit;
import com.baidu.beidou.fengchao.FcFacade;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
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
public class FCServiceImpl implements FCService {
	
    private FcFacade fcFacade;
	
	// FC推广计划设置相关阀值限制
	private int getFCCampaignByFCCampaignIdMax;
	private int getFCUnitByFCUnitIdMax;
	private int getFCUnitByFCCampaignIdsMax;

	/**
	 * 获取所有FC推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCCampaignType> 包括推广计划id和名称列表
	 */
	public ApiResult<FCCampaignType> getFCCampaign(DataPrivilege user, GetFCCampaignRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<FCCampaignType> result = new ApiResult<FCCampaignType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(user.getDataUser());
		if(CollectionUtils.isNotEmpty(fcPlans)){
			payment.setTotal(fcPlans.size());
			for(BDPlan fcPlan: fcPlans){
				FCCampaignType type = new FCCampaignType();
				type.setCampaignId(fcPlan.getPlanid());
				type.setCampaignName(fcPlan.getPlanname());
				result = ApiResultBeanUtils.addApiResult(result, type);
				payment.increSuccess();
			}
		}
		
		result.setPayment(payment);
		return result;
	}

	/**
	 * 获取所有FC推广计划id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广计划id列表
	 */
	public ApiResult<Long> getFCCampaignId(DataPrivilege user, GetFCCampaignIdRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<Long> result = new ApiResult<Long>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(user.getDataUser());
		if(CollectionUtils.isNotEmpty(fcPlans)){
			payment.setTotal(fcPlans.size());
			for(BDPlan fcPlan: fcPlans){
				result = ApiResultBeanUtils.addApiResult(result, fcPlan.getPlanid());
				payment.increSuccess();
			}
		}
		
		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 根据指定id获取所有FC推广计划
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCCampaignType> 包括推广计划id和名称列表
	 */
	public ApiResult<FCCampaignType> getFCCampaignByFCCampaignId(DataPrivilege user, GetFCCampaignByFCCampaignIdRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<FCCampaignType> result = new ApiResult<FCCampaignType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCampaignIds() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.CAMPAIGN_IDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request != null && request.getCampaignIds() != null) {
			if (request.getCampaignIds().length > getFCCampaignByFCCampaignIdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.CAMPAIGN_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						FCPlanErrorCode.TOOMANY_NUM.getValue(),
						FCPlanErrorCode.TOOMANY_NUM.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			String position = PositionConstant.NOPARAM;
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					position, null);
			return result;
		}
		
		Map<Long, FCCampaignType> planMap = new HashMap<Long, FCCampaignType>();
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(user.getDataUser());
		if(CollectionUtils.isNotEmpty(fcPlans)){
			for(BDPlan fcPlan: fcPlans){
				FCCampaignType type = new FCCampaignType();
				type.setCampaignId(fcPlan.getPlanid());
				type.setCampaignName(fcPlan.getPlanname());
				planMap.put(type.getCampaignId(), type);	
			}
		}
		
		long[] fcCampaignIds = request.getCampaignIds();
		for (int index = 0; index < fcCampaignIds.length; index++) {
			payment.setTotal(fcCampaignIds.length);
			long fcCampaignId = fcCampaignIds[index];
			if(planMap.containsKey(fcCampaignId)){
				result = ApiResultBeanUtils.addApiResult(result, planMap.get(fcCampaignId));
				payment.increSuccess();
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.CAMPAIGN_IDS, index);

				ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 根据推广计划id获取其下所有推广单元
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCUnitType> 包括推广单元id和名称列表
	 */
	public ApiResult<FCUnitType> getFCUnitByFCCampaignId(DataPrivilege user, GetFCUnitByFCCampaignIdRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<FCUnitType> result = new ApiResult<FCUnitType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCampaignId() < 1) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		Set<Long> planIdSet = new HashSet<Long>();
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(user.getDataUser());
		if(CollectionUtils.isNotEmpty(fcPlans)){
			for(BDPlan fcPlan: fcPlans){
				planIdSet.add(fcPlan.getPlanid());
			}
		}
		
		long fcPlanId = request.getCampaignId();
		if(!planIdSet.contains(fcPlanId)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);

			ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
					apiPosition.getPosition(), null);
		} else {
            List<BDUnit> fcUnits =
                    fcFacade.getFcUnitlist(user.getDataUser(), fcPlanId, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
			if(CollectionUtils.isNotEmpty(fcUnits)){
				payment.setTotal(fcUnits.size());
				for(BDUnit fcUnit: fcUnits){
					FCUnitType type = new FCUnitType();
					type.setUnitId(fcUnit.getUnitid());
					type.setUnitName(fcUnit.getUnitname());
					result = ApiResultBeanUtils.addApiResult(result, type);
					payment.increSuccess();
				}
			}
		}
		
		result.setPayment(payment);
		return result;
	}
	
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
	public BaseResponse<FCCampaignUnitType> getFCUnitByFCCampaignIds(BaseRequestUser user, GetFCUnitByFCCampaignIdsRequest param, BaseRequestOptions options){
		
		BaseResponse<FCCampaignUnitType> response = new BaseResponse<FCCampaignUnitType>();
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
		long[] campaignIds = param.getCampaignIds();
		if (ArrayUtils.isEmpty(campaignIds)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					FCPlanErrorCode.PARAM_EMPTY.getValue(),
					FCPlanErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (campaignIds.length > getFCCampaignByFCCampaignIdMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					FCPlanErrorCode.TOOMANY_NUM.getValue(),
					FCPlanErrorCode.TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		// 配额总数设为查询数量
		option.setTotal(campaignIds.length);
		
		Map<Long, BDPlan> planIdMap = new HashMap<Long, BDPlan>();
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(userId);
		if(CollectionUtils.isNotEmpty(fcPlans)){
			for(BDPlan fcPlan: fcPlans){
				planIdMap.put(fcPlan.getPlanid(), fcPlan);
			}
		}
		
		List<FCCampaignUnitType> result = new ArrayList<FCCampaignUnitType>();
		for (int i = 0; i < campaignIds.length; i++) {
			long fcPlanId = campaignIds[i];
			if(!planIdMap.containsKey(fcPlanId)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);

				DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
						apiPosition.getPosition(), null);
			} else {
				FCCampaignUnitType campaignUnitType = new FCCampaignUnitType();
				campaignUnitType.setCampaignId(fcPlanId);
				campaignUnitType.setCampaignName(planIdMap.get(fcPlanId).getPlanname());
                List<BDUnit> fcUnits = fcFacade.getFcUnitlist(userId, fcPlanId, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				List<FCUnitType> unitTypes = new ArrayList<FCUnitType>();
				if(CollectionUtils.isNotEmpty(fcUnits)){
					for(BDUnit fcUnit: fcUnits){
						FCUnitType type = new FCUnitType();
						type.setUnitId(fcUnit.getUnitid());
						type.setUnitName(fcUnit.getUnitname());
						unitTypes.add(type);
					}
				}
				campaignUnitType.setUnits(unitTypes.toArray(new FCUnitType[]{}));
				result.add(campaignUnitType);
			}
		}

		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new FCCampaignUnitType[]{}));

		return response;
	}
	
	/**
	 * 根据推广计划id获取其下所有推广单元id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广单元id列表
	 */
	public ApiResult<Long> getFCUnitIdByFCCampaignId(DataPrivilege user, GetFCUnitIdByFCCampaignIdRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<Long> result = new ApiResult<Long>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCampaignId() < 1) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		Set<Long> planIdSet = new HashSet<Long>();
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(user.getDataUser());
		if(CollectionUtils.isNotEmpty(fcPlans)){
			for(BDPlan fcPlan: fcPlans){
				planIdSet.add(fcPlan.getPlanid());
			}
		}
		
		long fcPlanId = request.getCampaignId();
		if(!planIdSet.contains(fcPlanId)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);

			ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
					apiPosition.getPosition(), null);
		} else {
            List<BDUnit> fcUnits =
                    fcFacade.getFcUnitlist(user.getDataUser(), fcPlanId, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
			if(CollectionUtils.isNotEmpty(fcUnits)){
				payment.setTotal(fcUnits.size());
				for(BDUnit fcUnit: fcUnits){
					result = ApiResultBeanUtils.addApiResult(result, fcUnit.getUnitid());
					payment.increSuccess();
				}
			}
		}
		
		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 根据推广计划id列表获取其下所有推广单元id
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<Long> 包括推广单元id列表
	 */
	@RPCMethod(methodName = "getFCUnitIdByFCCampaignIds", returnType = ReturnType.ARRAY)
	public BaseResponse<FCCampaignUnitIdType> getFCUnitIdByFCCampaignIds(BaseRequestUser user, GetFCUnitIdByFCCampaignIdsRequest param, BaseRequestOptions options){

		BaseResponse<FCCampaignUnitIdType> response = new BaseResponse<FCCampaignUnitIdType>();
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
		long[] campaignIds = param.getCampaignIds();
		if (ArrayUtils.isEmpty(campaignIds)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					FCPlanErrorCode.PARAM_EMPTY.getValue(),
					FCPlanErrorCode.PARAM_EMPTY.getMessage(), apiPosition.getPosition(), null);
			return response;
		}

		// 验证传入参数长度
		if (campaignIds.length > getFCCampaignByFCCampaignIdMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					FCPlanErrorCode.TOOMANY_NUM.getValue(),
					FCPlanErrorCode.TOOMANY_NUM.getMessage(), apiPosition.getPosition(), null);
			return response;
		}
		
		// 配额总数设为查询数量
		option.setTotal(campaignIds.length);
		
		Map<Long, BDPlan> planIdMap = new HashMap<Long, BDPlan>();
		List<BDPlan> fcPlans = fcFacade.getFcPlanlist(userId);
		if(CollectionUtils.isNotEmpty(fcPlans)){
			for(BDPlan fcPlan: fcPlans){
				planIdMap.put(fcPlan.getPlanid(), fcPlan);
			}
		}
		
		List<FCCampaignUnitIdType> result = new ArrayList<FCCampaignUnitIdType>();
		for (int i = 0; i < campaignIds.length; i++) {
			long fcPlanId = campaignIds[i];
			if(!planIdMap.containsKey(fcPlanId)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.POSITION_CAMPAIGN_ID);

				DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
						apiPosition.getPosition(), null);
			} else {
				FCCampaignUnitIdType campaignUnitIdType = new FCCampaignUnitIdType();
				campaignUnitIdType.setCampaignId(fcPlanId);
                List<BDUnit> fcUnits = fcFacade.getFcUnitlist(userId, fcPlanId, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				List<Long> unitTypes = new ArrayList<Long>();
				if(CollectionUtils.isNotEmpty(fcUnits)){
					for(BDUnit fcUnit: fcUnits){
						unitTypes.add(fcUnit.getUnitid());
					}
				}
				campaignUnitIdType.setUnitIds(CollectionsUtil.tranformLongListToLongArray(unitTypes));
				result.add(campaignUnitIdType);
			}
		}

		response.getOptions().setSuccess(result.size());
		response.setData(result.toArray(new FCCampaignUnitIdType[]{}));

		return response;
	}

	/**
	 * 根据推广单元id获取推广单元信息
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<FCUnitType> 包括推广单元id和名称列表
	 */
	public ApiResult<FCUnitType> getFCUnitByFCUnitId(DataPrivilege user, GetFCUnitByFCUnitIdRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<FCUnitType> result = new ApiResult<FCUnitType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getUnitIds() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(FCPlanConstant.UNIT_IDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request != null && request.getUnitIds() != null) {
			if (request.getUnitIds().length > getFCUnitByFCUnitIdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.UNIT_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						FCPlanErrorCode.TOOMANY_NUM.getValue(),
						FCPlanErrorCode.TOOMANY_NUM.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			String position = PositionConstant.NOPARAM;
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					position, null);
			return result;
		}
		
		long[] unitIdArray = request.getUnitIds();
		List<Long> unitIds = new ArrayList<Long>();
		for(long unitId: unitIdArray){
			unitIds.add(unitId);
		}
		Map<Long, FCUnitType> unitMap = new HashMap<Long, FCUnitType>();
		List<BDUnit> fcUnits = fcFacade.getFcUnitListByUnitids(user.getDataUser(),unitIds,null);
		if(CollectionUtils.isNotEmpty(fcUnits)){
			for(BDUnit fcUnit: fcUnits){
				FCUnitType type = new FCUnitType();
				type.setUnitId(fcUnit.getUnitid());
				type.setUnitName(fcUnit.getUnitname());
				unitMap.put(type.getUnitId(), type);	
			}
		}
		
		for (int index = 0; index < unitIdArray.length; index++) {
			payment.setTotal(unitIdArray.length);
			long fcUnitId = unitIdArray[index];
			if(unitMap.containsKey(fcUnitId)){
				result = ApiResultBeanUtils.addApiResult(result, unitMap.get(fcUnitId));
				payment.increSuccess();
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(FCPlanConstant.UNIT_IDS, index);

				ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		result.setPayment(payment);
		return result;
	}

	public int getGetFCCampaignByFCCampaignIdMax() {
		return getFCCampaignByFCCampaignIdMax;
	}

	public void setGetFCCampaignByFCCampaignIdMax(int getFCCampaignByFCCampaignIdMax) {
		this.getFCCampaignByFCCampaignIdMax = getFCCampaignByFCCampaignIdMax;
	}

	public int getGetFCUnitByFCUnitIdMax() {
		return getFCUnitByFCUnitIdMax;
	}

	public void setGetFCUnitByFCUnitIdMax(int getFCUnitByFCUnitIdMax) {
		this.getFCUnitByFCUnitIdMax = getFCUnitByFCUnitIdMax;
	}

	public int getGetFCUnitByFCCampaignIdsMax() {
		return getFCUnitByFCCampaignIdsMax;
	}

	public void setGetFCUnitByFCCampaignIdsMax(int getFCUnitByFCCampaignIdsMax) {
		this.getFCUnitByFCCampaignIdsMax = getFCUnitByFCCampaignIdsMax;
	}

    public FcFacade getFcFacade() {
        return fcFacade;
    }

    public void setFcFacade(FcFacade fcFacade) {
        this.fcFacade = fcFacade;
    }
	
	
}
