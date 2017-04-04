package com.baidu.beidou.api.external.cprogroup.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.facade.GroupPriceFacade;
import com.baidu.beidou.api.external.cprogroup.util.GroupBoMappingUtil;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.IndexMapperUtil;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.IndexMapper;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupIT;
import com.baidu.beidou.cprogroup.bo.CustomInterest;
import com.baidu.beidou.cprogroup.bo.GroupInterestPrice;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.GroupPackConstant;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.error.GroupErrorConst;
import com.baidu.beidou.cprogroup.exception.GroupPackException;
import com.baidu.beidou.cprogroup.facade.GroupITPriceFacade;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.GroupITPriceMgr;
import com.baidu.beidou.cprogroup.service.GroupPackMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.GroupNamePriceOptVo;
import com.baidu.beidou.cprogroup.vo.GroupPackVo;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.pack.constant.PackTypeConstant;
import com.baidu.beidou.pack.service.PackMgr;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.vo.APIResult;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.google.common.base.Objects;

/**
 * 
 * ClassName: GroupPriceFacade  <br>
 * Function: 多维出价facade
 *
 * @author zhangxu
 * @date Sep 17, 2012
 */
public class GroupPriceFacadeImpl implements GroupPriceFacade {
	
	private GroupPackMgr groupPackMgr;
	
	private CproGroupMgr cproGroupMgr;
	
	private CproPlanMgr cproPlanMgr;
	
	private GroupITPriceMgr groupITPriceMgr;
	
	private CproGroupITMgr cproGroupITMgr;
	
	private CustomITMgr customITMgr;
	
	private InterestMgr interestMgr;
	
	private PackMgr packMgr;
	
	
	/**
	 * 设置多维出价配置
	 * @param response
	 * @param prices
	 * @param userId
	 * @param opUser
	 */
	public void setPrice(BaseResponse<PlaceHolderResult>response, 
			List<PriceType> prices,
			int userId,
			int opUser) {
		
		Visitor visitor = new Visitor();
		visitor.setUserid(userId);
		
		// 构造按照推广组将出价设置分组，key为groupId
		Map<Integer, List<PriceType>> groupPrices = new HashMap<Integer, List<PriceType>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupPricesIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(prices.size());
		int success = prices.size();
		
		// 两个标志位，为了性能考虑，后续查询数据库会减少查询
		boolean isAtLeastSetOnePackPrice = false;
		boolean isAtLeastSetOneInterestPrice = false;
		
		// 验证基本参数并按照推广组将出价分组
		for (int index = 0; index < prices.size(); index++) {
			PriceType item = prices.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			int type = item.getType();
			int packType = item.getPackType();
			int price = item.getPrice();
			int id = item.getId1();
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PRICES, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 操作id不合法
			if (id < 1) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PRICES, index);
				apiPosition.addParam(GroupConstant.ID1);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 设置出价类型不合法
			if(type != GroupConstant.GROUP_PACK_PRICE_TYPE &&
					type != GroupConstant.GROUP_INTEREST_PRICE_TYPE){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PRICES, index);
				apiPosition.addParam(GroupConstant.TYPE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 受众组合出价类型不合法
			if(type == GroupConstant.GROUP_PACK_PRICE_TYPE){
				if(packType != PackTypeConstant.TYPE_KEYWORD_PACK &&
						packType != PackTypeConstant.TYPE_ADVANCED_PACK &&
						packType != PackTypeConstant.TYPE_INTEREST_PACK &&
						packType != PackTypeConstant.TYPE_VISIT_PEOPLE_PACK){
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PRICES, index);
					apiPosition.addParam(GroupConstant.PACKTYPE);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
					continue;
				}
			}
			
			// 设置出价范围不合法
			if(!validPriceRange(price)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PRICES, index);
				apiPosition.addParam(GroupConstant.PRICE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.PRICE_NOT_VALID.getValue(),
						GroupConfigErrorCode.PRICE_NOT_VALID.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			IndexMapperUtil.mapIndex(groupId, item, index, groupPrices, groupPricesIndexMap);
			
			if (type == GroupConstant.GROUP_PACK_PRICE_TYPE) {
				isAtLeastSetOnePackPrice = true;
			}
			
			if (type == GroupConstant.GROUP_INTEREST_PRICE_TYPE) {
				isAtLeastSetOneInterestPrice = true;
			}
		}

		// 验证待修改出价的推广组个数
		if (groupPrices.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PRICES);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupPrices.keySet()) {
			
			List<PriceType> priceList = groupPrices.get(groupId);
			IndexMapper indexMapper = groupPricesIndexMap.get(groupId);
			
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
			
			List<CproGroupIT> cproGroupITList = null;
			Map<Integer, InterestCacheObject> interestCacheObjMap = null;
			if (isAtLeastSetOneInterestPrice) {
				//准备验证兴趣点或兴趣组合是否已删除的数据
				cproGroupITList = cproGroupITMgr.findGroupITList(groupId);
				interestCacheObjMap = interestMgr.getInterestCacheMap();
			}
			
			List<GroupPack> groupPacks = null; 
			if (isAtLeastSetOnePackPrice) {
				//准备验证受众组合是否存在的数据
				groupPacks = groupPackMgr.getGroupPackByGroupId(groupId);
			}
			
			for (int index = 0; index < priceList.size(); index++) {
				PriceType priceType = priceList.get(index);
				int type = priceType.getType();
				int packType = priceType.getPackType();
				int price = priceType.getPrice();
				int id = priceType.getId1();
				
				if (!validPriceNotExceedPlanBudget(plan, price)) { 
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PRICES, indexMapper.getIndex(index));
					apiPosition.addParam(GroupConstant.PRICE);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.PRICE_EXCEED_PLAN_BUDGET.getValue(),
							GroupConfigErrorCode.PRICE_EXCEED_PLAN_BUDGET.getMessage(), apiPosition.getPosition(), null);
					continue;
				}
				
				if (type == GroupConstant.GROUP_PACK_PRICE_TYPE) {  // -------------- 设置受众组合出价
					GroupPack groupPack = validGroupPackExist(groupPacks, packType, id);
					if (groupPack == null) { 
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.PRICES, indexMapper.getIndex(index));
						apiPosition.addParam(GroupConstant.ID1);
						response = DRAPIMountAPIBeanUtils.addApiError(response,
								GroupConfigErrorCode.PACK_NOT_FOUND_IN_GROUP.getValue(),
								GroupConfigErrorCode.PACK_NOT_FOUND_IN_GROUP.getMessage(), apiPosition.getPosition(), null);
						continue;
					}
					
					if (price == GroupConstant.API_PRICE_NOT_UPDATE_FLAG) {
						cancelPackPrice(userId, visitor, groupId, optContents, groupPack);
					} else {
						modPackPrice(userId, visitor, groupId, optContents, groupPack, price);
					}
					
				} else if (type == GroupConstant.GROUP_INTEREST_PRICE_TYPE) { // -------------- 设置兴趣出价
					//验证推广组的定向方式是否正确
					if(TargettypeUtil.isPack(group.getTargetType())){
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.PRICES, indexMapper.getIndex(index));
						apiPosition.addParam(GroupConstant.GROUPID);
						response = DRAPIMountAPIBeanUtils.addApiError(response,
								GroupConfigErrorCode.GROUP_IS_NOT_PACK_TARGET.getValue(),
								GroupConfigErrorCode.GROUP_IS_NOT_PACK_TARGET.getMessage(), apiPosition.getPosition(), null);
						continue;
					}
					
					if (!validInterestExist(id, cproGroupITList, interestCacheObjMap)) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.PRICES, indexMapper.getIndex(index));
						apiPosition.addParam(GroupConstant.ID1);
						response = DRAPIMountAPIBeanUtils.addApiError(response,
								GroupConfigErrorCode.INTEREST_NOT_FOUND_IN_GROUP.getValue(),
								GroupConfigErrorCode.INTEREST_NOT_FOUND_IN_GROUP.getMessage(), apiPosition.getPosition(), null);
						continue;
					}
					
					if (price == GroupConstant.API_PRICE_NOT_UPDATE_FLAG) {
						cancelGroupITPrice(userId, visitor, optContents, group, cproGroupITList, interestCacheObjMap, id);
					} else {
						modGroupITPrice(userId, visitor, optContents, group, cproGroupITList, interestCacheObjMap, id, price);
					}
					
				}
			}
		}
			
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
	
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
	
		response.getOptions().setSuccess(success);
			
	}
	
	/**
	 * 修改受众组合出价
	 */
	public void modPackPrice(Integer userId, 
			Visitor visitor,
			Integer groupId,
			List<OptContent> optContents, 
			GroupPack pack, 
			Integer price) {
		
		boolean isSuccess = false;
		
		//保存历史操作记录
		final OpTypeVo opTypeMod = OptHistoryConstant.OPTYPE_GROUP_PACK_INTEREST_PRICE;
		GroupNamePriceOptVo before = null;
		
		// 修改出价
		if(!Objects.equal(pack.getPrice(), price)){
			if(pack.getPrice() != null && pack.getPrice() > 0){
				before = new GroupNamePriceOptVo();
				String packName = packMgr.getPackNameByPackIdAndPackType(pack.getPackId(), pack.getPackType(), userId);
				before.setName(new String[]{packName});
				before.setPrice(pack.getPrice());
			}
			
			//修改出价
			pack.setPrice(price);
			pack.setModTime(new Date());
			pack.setModUser(visitor.getUserid());
			isSuccess = groupPackMgr.updateGroupPack(pack);
			
		}
		if(isSuccess){
			GroupNamePriceOptVo after = new GroupNamePriceOptVo();
			String packName = packMgr.getPackNameByPackIdAndPackType(pack.getPackId(), pack.getPackType(), userId);
			after.setName(new String[]{packName});
			after.setPrice(price);
			OptContent content = new OptContent(userId, opTypeMod.getOpType(),
					opTypeMod.getOpLevel(), groupId,
					opTypeMod.getTransformer().toDbString(before), //before
					opTypeMod.getTransformer().toDbString(after)); //after
			optContents.add(content);
		}
	
		return;
	}

	/**
	 * 取消受众组合出价
	 */
	public void cancelPackPrice(Integer userId, 
			Visitor visitor,
			Integer groupId,
			List<OptContent> optContents, 
			GroupPack pack) {
		
		boolean isSuccess = false;
		
		//保存历史操作记录
		final OpTypeVo opTypeMod = OptHistoryConstant.OPTYPE_GROUP_PACK_INTEREST_PRICE;
		GroupNamePriceOptVo before = new GroupNamePriceOptVo();
		String packName = packMgr.getPackNameByPackIdAndPackType(pack.getPackId(), pack.getPackType(), userId);
		before.setName(new String[]{packName});
		before.setPrice(pack.getPrice());
		
		// 删除单独出价，即将price置为0
		pack.setPrice(0);
		pack.setModTime(new Date());
		pack.setModUser(visitor.getUserid());
		isSuccess = groupPackMgr.updateGroupPack(pack);
		
		if(isSuccess){
			if (before.getPrice() != 0) {
				//保存历史操作记录
				OptContent content = new OptContent(userId, opTypeMod.getOpType(),
						opTypeMod.getOpLevel(), groupId,
						opTypeMod.getTransformer().toDbString(before), null);
				optContents.add(content);
			}
		}
		
		return;
	}
	
	/**
	 * 修改兴趣点或者兴趣组合出价
	 */
	public void modGroupITPrice(Integer userId, 
			Visitor visitor, 
			List<OptContent> optContent,
			CproGroup group,
			List<CproGroupIT> cproGroupITList,
			Map<Integer, InterestCacheObject> interestCacheObjMap,
			Integer interestId, 
			Integer price) {
		
		Integer groupId = group.getGroupId();
		
		// 构造需新增或更新的对象list
		List<GroupInterestPrice> addPriceList = new ArrayList<GroupInterestPrice>();
		List<GroupInterestPrice> modPriceList = new ArrayList<GroupInterestPrice>();
		
		boolean needModifyPrice = true;
		List<GroupInterestPrice> dbPriceList = groupITPriceMgr.findInterestPriceByGroupId(groupId);
		//保存历史操作记录
		final OpTypeVo opTypeMod = OptHistoryConstant.OPTYPE_GROUP_INTEREST_PRICE;
		for(GroupInterestPrice iidPrice : dbPriceList){
			if(iidPrice.getIid().equals(interestId)){
				if(!Objects.equal(iidPrice.getPrice(), price)){
					//保存历史操作记录
					GroupNamePriceOptVo before = new GroupNamePriceOptVo();
					String interestName = "";
					if(interestId < InterestConstant.MAX_INTEREST_ID){//兴趣点
						interestName = interestCacheObjMap.get(interestId).getName();
					}
					else{//兴趣组合
						CustomInterest customIT = customITMgr.getCustomITById(userId, interestId);
						interestName = customIT.getName();
					}
					before.setName(new String[]{interestName});
					before.setPrice(iidPrice.getPrice());
					
					//修改出价
					iidPrice.setPrice(price);
					iidPrice.setModTime(new Date());
					iidPrice.setModUser(visitor.getUserid());
					modPriceList.add(iidPrice);
					
					//保存历史操作记录
					GroupNamePriceOptVo after = new GroupNamePriceOptVo();
					after.setName(new String[]{interestName});
					after.setPrice(price);
					OptContent content = new OptContent(userId, opTypeMod.getOpType(),
							opTypeMod.getOpLevel(), groupId,
							opTypeMod.getTransformer().toDbString(before), //before
							opTypeMod.getTransformer().toDbString(after)); //after
					optContent.add(content);
					
					break;
				}
				else{
					needModifyPrice = false;
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(modPriceList)){
			groupITPriceMgr.updateGroupITPrice(modPriceList);
		} else if(needModifyPrice){
			GroupInterestPrice gip = new GroupInterestPrice();
			gip.setGroupId(group.getGroupId());
			gip.setPlanId(group.getPlanId());
			gip.setUserId(group.getUserId());
			gip.setIid(interestId);
			gip.setType(interestId > InterestConstant.MAX_INTEREST_ID ? 1 : 0);
			gip.setPrice(price);
			gip.setAddUser(visitor.getUserid());
			gip.setAddTime(new Date());
			gip.setModUser(visitor.getUserid());
			gip.setModTime(new Date());
			
			addPriceList.add(gip);
			groupITPriceMgr.insertGroupITPrice(addPriceList);
			
			//保存历史操作记录
			GroupNamePriceOptVo after = new GroupNamePriceOptVo();
			String interestName = "";
			if(interestId < InterestConstant.MAX_INTEREST_ID){//兴趣点
				interestName = interestCacheObjMap.get(interestId).getName();
			}
			else{//兴趣组合
				CustomInterest customIT = customITMgr.getCustomITById(userId, interestId);
				interestName = customIT.getName();
			}
			after.setName(new String[]{interestName});
			after.setPrice(price);
			OptContent content = new OptContent(userId, opTypeMod.getOpType(),
					opTypeMod.getOpLevel(), groupId,
					null, opTypeMod.getTransformer().toDbString(after));
			optContent.add(content);
		}
		
	}
	
	/**
	 * 取消兴趣点或者兴趣组合出价
	 */
	public void cancelGroupITPrice(Integer userId, 
			Visitor visitor,
			List<OptContent> optContent, 
			CproGroup group,
			List<CproGroupIT> cproGroupITList,
			Map<Integer, InterestCacheObject> interestCacheObjMap,
			Integer interestId) {
		
		Integer groupId = group.getGroupId();
		
		// 验证是否对兴趣组合或兴趣点设置了出价
		Map<Integer, Integer> groupidInterestidMap = new HashMap<Integer, Integer>();
		groupidInterestidMap.put(groupId, interestId);
		List<GroupInterestPrice> groupInterestPriceList = groupITPriceMgr.getPriceByGroupIdAndIId(groupidInterestidMap);
		
		boolean isSelected = false;
		//保存历史操作记录
		final OpTypeVo opTypeMod = OptHistoryConstant.OPTYPE_GROUP_INTEREST_PRICE;
		GroupNamePriceOptVo before = null;
		for(GroupInterestPrice interestPrice : groupInterestPriceList){
			if(interestPrice.getIid().equals(interestId)){
				isSelected = true;
				
				before = new GroupNamePriceOptVo();
				String interestName = "";
				if(interestId < InterestConstant.MAX_INTEREST_ID){//兴趣点
					interestName = interestCacheObjMap.get(interestId).getName();
				}
				else{//兴趣组合
					CustomInterest customIT = customITMgr.getCustomITById(userId, interestId);
					interestName = customIT.getName();
				}
				before.setName(new String[]{interestName});
				before.setPrice(interestPrice.getPrice());

				break;
			}
		}

		// 如果没有设置出价，则直接返回
		if(!isSelected){
			return;
		}
		
		// 构造需删除的对象Map
		List<Integer> interestIdList = new ArrayList<Integer>();
		interestIdList.add(interestId);
		Map<Integer, List<Integer>> delPriceMap = new HashMap<Integer, List<Integer>>();
		delPriceMap.put(groupId, interestIdList);
		
		int successCount = groupITPriceMgr.delPriceByGroupIdAndIId(delPriceMap);
		if(successCount > 0){
			if (before.getPrice() != 0) {
				OptContent content = new OptContent(userId, opTypeMod.getOpType(),
						opTypeMod.getOpLevel(), groupId,
						opTypeMod.getTransformer().toDbString(before), null);
				optContent.add(content);
			}
		}
		
		return;
	}
	
	/**
	 * 验证受众组合是否存在
	 * @return 如果存在就返回该关联关系对象
	 */
	private GroupPack validGroupPackExist(List<GroupPack> groupPacks, int packType, int packId){
		for (GroupPack groupPack : groupPacks) {
			if (groupPack.getPackType().equals(packType) && 
				groupPack.getPackId().equals(packId)) {
				return groupPack;
			}
		}
		return null;
	}
	
	/**
	 * 验证price是在价格范围内
	 * @param plan
	 * @param price
	 * @return
	 */
	private boolean validPriceRange(Integer price){
		
		// 全局取消出价合法
		if (price == GroupConstant.API_PRICE_NOT_UPDATE_FLAG) {
			return true;
		}
		
		// 验证价格是否在正常范围中
		if(!CproGroupConstant.isValidPriceValueRange(price)){
			return false;
		}
		
		return true;
	}

	/**
	 * 验证price是否超过推广计划预算
	 * @param plan
	 * @param price
	 * @return
	 */
	private boolean validPriceNotExceedPlanBudget(CproPlan plan, Integer price){
		
		// 全局取消出价合法
		if (price == GroupConstant.API_PRICE_NOT_UPDATE_FLAG) {
			return true;
		}
		
		// 验证出价是否小于计划的预算
		if(plan.getBudget() * 100 < price){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证兴趣点是否关联到推广组中
	 */
	private boolean validInterestExist(Integer interestId, 
			List<CproGroupIT> cproGroupITList, 
			Map<Integer, InterestCacheObject> interestCacheObjMap){
		boolean isSelected = false;
		for(CproGroupIT cproGroupIT : cproGroupITList){
			if(cproGroupIT.getIid().equals(interestId)){
				isSelected = true;
				break;
			}
			//对于单独出价的兴趣是推广组关联兴趣的父兴趣，则允许单独出价
			if(interestCacheObjMap.get(cproGroupIT.getIid()).getParentId().intValue() == interestId.intValue()){
				isSelected = true;
				break;
			}
			//检查一级兴趣点下属的二级兴趣点
			if(interestId < InterestConstant.MAX_INTEREST_ID && interestCacheObjMap.get(cproGroupIT.getIid()) != null 
					&& interestCacheObjMap.get(cproGroupIT.getIid()).getParentId() == 0){
				Set<Integer> childrenKeys = interestCacheObjMap.get(cproGroupIT.getIid()).getChildrenKeys();
				if(childrenKeys.contains(interestId)){
					isSelected = true;
					break;
				}
			}
		}
		return isSelected;
	}
	
	public GroupPackMgr getGroupPackMgr() {
		return groupPackMgr;
	}

	public void setGroupPackMgr(GroupPackMgr groupPackMgr) {
		this.groupPackMgr = groupPackMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

	public GroupITPriceMgr getGroupITPriceMgr() {
		return groupITPriceMgr;
	}

	public void setGroupITPriceMgr(GroupITPriceMgr groupITPriceMgr) {
		this.groupITPriceMgr = groupITPriceMgr;
	}

	public CproGroupITMgr getCproGroupITMgr() {
		return cproGroupITMgr;
	}

	public void setCproGroupITMgr(CproGroupITMgr cproGroupITMgr) {
		this.cproGroupITMgr = cproGroupITMgr;
	}

	public CustomITMgr getCustomITMgr() {
		return customITMgr;
	}

	public void setCustomITMgr(CustomITMgr customITMgr) {
		this.customITMgr = customITMgr;
	}

	public InterestMgr getInterestMgr() {
		return interestMgr;
	}

	public void setInterestMgr(InterestMgr interestMgr) {
		this.interestMgr = interestMgr;
	}

	public PackMgr getPackMgr() {
		return packMgr;
	}

	public void setPackMgr(PackMgr packMgr) {
		this.packMgr = packMgr;
	}
	

}
