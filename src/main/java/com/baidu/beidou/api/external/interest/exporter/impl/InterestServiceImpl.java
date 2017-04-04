package com.baidu.beidou.api.external.interest.exporter.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.interest.constant.ItConstant;
import com.baidu.beidou.api.external.interest.error.InterestErrorCode;
import com.baidu.beidou.api.external.interest.exporter.InterestService;
import com.baidu.beidou.api.external.interest.util.InterestUtils;
import com.baidu.beidou.api.external.interest.vo.CustomInterestCollectionType;
import com.baidu.beidou.api.external.interest.vo.CustomInterestType;
import com.baidu.beidou.api.external.interest.vo.InterestType;
import com.baidu.beidou.api.external.interest.vo.request.GetAllCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetCustomInterestRequest;
import com.baidu.beidou.api.external.interest.vo.request.GetInterestRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CustomInterest;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;
import com.baidu.beidou.cprogroup.vo.InterestVo;

/**
 * 
 * ClassName: InterestServiceImpl  <br>
 * Function: 通过InterestService，查询用户的兴趣组合工具
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class InterestServiceImpl implements InterestService {
	
	private InterestMgr interestMgr;
	
	private CustomITMgr customITMgr;
	
	private CproGroupITMgr cproGroupITMgr;
	
	private int getCustomInterestIdMax;

	/**
	 * 获取系统默认的所有兴趣点
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<InterestType> 兴趣id、名称列表
	 */
	public ApiResult<InterestType> getInterest(DataPrivilege user, GetInterestRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<InterestType> result = new ApiResult<InterestType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		List<InterestCacheObject> interestCacheObjects = interestMgr.getInterestCacheList();
		if(CollectionUtils.isNotEmpty(interestCacheObjects)){
			for(InterestCacheObject interest: interestCacheObjects){
				InterestType type = new InterestType();
				type.setInterestId(interest.getId());
				type.setInterestName(interest.getName());
				type.setParentId(interest.getParentId());
				result = ApiResultBeanUtils.addApiResult(result, type);
				if (interest.getChildren().size() != 0) {
					for (InterestCacheObject interestCacheObject : interest.getChildren()) {
						InterestType childType = new InterestType();
						childType.setInterestId(interestCacheObject.getId());
						childType.setInterestName(interestCacheObject.getName());
						childType.setParentId(interestCacheObject.getParentId());
						result = ApiResultBeanUtils.addApiResult(result, childType);
					}
				}
			}
		}
		
		payment.setTotal(1);
		payment.increSuccess();
		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 获取用户所有自定义兴趣组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<CustomInterestType> 自定义兴趣组合列表
	 */
	public ApiResult<CustomInterestType> getAllCustomInterest(DataPrivilege user, GetAllCustomInterestRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<CustomInterestType> result = new ApiResult<CustomInterestType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		List<CustomInterest> customITs = customITMgr.getCustomItListByUserId(user.getDataUser());
		if(CollectionUtils.isNotEmpty(customITs)){
			payment.setTotal(customITs.size());
			for(CustomInterest customIT: customITs){
				CustomInterestType type = new CustomInterestType(customIT.getCid(),customIT.getName()); 
				List<List<InterestVo>> expList = parse2ITPack(customIT.getExpression());
				CustomInterestCollectionType[] collectionType = InterestUtils.parseItPackToItCollection(expList);
				type.setCustomItCollections(collectionType);
				result = ApiResultBeanUtils.addApiResult(result, type);
				payment.increSuccess();
			}
		}
		
		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 根据id获取用户自定义兴趣组合
	 * 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<CustomInterestType> 自定义兴趣组合列表
	 */
	public ApiResult<CustomInterestType> getCustomInterest(DataPrivilege user, GetCustomInterestRequest request, ApiOption apiOption){
		PaymentResult payment = new PaymentResult();
		ApiResult<CustomInterestType> result = new ApiResult<CustomInterestType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCustomItIds() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ItConstant.CUSTOM_INTEREST_IDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request != null && request.getCustomItIds() != null) {
			if (request.getCustomItIds().length > getCustomInterestIdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ItConstant.CUSTOM_INTEREST_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						InterestErrorCode.INTEREST_ID_LIST_TOO_LONG.getValue(),
						InterestErrorCode.INTEREST_ID_LIST_TOO_LONG.getMessage(), 
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
		
		payment.setTotal(request.getCustomItIds().length);
		for (int index = 0; index < request.getCustomItIds().length; index++) {
			int cid = request.getCustomItIds()[index];
			CustomInterest customIT = customITMgr.getCustomITById(user.getDataUser(), cid);
			if(customIT == null || customIT.getUserId() != user.getDataUser()){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ItConstant.CUSTOM_INTEREST_IDS, index);
				ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			CustomInterestType type = new CustomInterestType(customIT.getCid(),customIT.getName()); 
			List<List<InterestVo>> expList = parse2ITPack(customIT.getExpression());
			CustomInterestCollectionType[] collectionType = InterestUtils.parseItPackToItCollection(expList);
			type.setCustomItCollections(collectionType);
			result = ApiResultBeanUtils.addApiResult(result, type);
			payment.increSuccess();
		}

		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 将数据库中的兴趣组合expression解析成前端显示的vo
	 * @param expression
	 * @return
	 */
	public List<List<InterestVo>> parse2ITPack(String expression) {
		String[] packs=expression.split("["+InterestConstant.PACK_SEPARATOR+"]");
		List<List<InterestVo>> itPacks=new LinkedList<List<InterestVo>>();
		for (int i = 0; i < packs.length; i++) {
			String[] ids=packs[i].split(InterestConstant.INTEREST_SEPARATOR);
			List<Integer> selectedIds=new ArrayList<Integer>();
			for (int j = 0; j < ids.length; j++) {
				selectedIds.add(Integer.parseInt(ids[j]));
			}
			itPacks.add(interestMgr.getSelectedTree(selectedIds));
			
		}
		
		return itPacks;
	}

	public int getGetCustomInterestIdMax() {
		return getCustomInterestIdMax;
	}

	public void setGetCustomInterestIdMax(int getCustomInterestIdMax) {
		this.getCustomInterestIdMax = getCustomInterestIdMax;
	}

	public InterestMgr getInterestMgr() {
		return interestMgr;
	}

	public void setInterestMgr(InterestMgr interestMgr) {
		this.interestMgr = interestMgr;
	}

	public CustomITMgr getCustomITMgr() {
		return customITMgr;
	}

	public void setCustomITMgr(CustomITMgr customITMgr) {
		this.customITMgr = customITMgr;
	}

	public CproGroupITMgr getCproGroupITMgr() {
		return cproGroupITMgr;
	}

	public void setCproGroupITMgr(CproGroupITMgr cproGroupITMgr) {
		this.cproGroupITMgr = cproGroupITMgr;
	}
	
	
	
}
