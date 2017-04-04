package com.baidu.beidou.api.external.cprogroup.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupItItem;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.cprogroup.bo.Interest;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.vo.CustomInterestVo;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;

public class ItUtils {

	public final static Map<Integer, CustomInterestVo> transVo2Map(
			List<CustomInterestVo> voList) {
		if(voList == null || voList.size() == 0){
			return Collections.emptyMap();
		}
		Map<Integer, CustomInterestVo> voMap = new HashMap<Integer, CustomInterestVo>();
		for(CustomInterestVo o : voList){
			voMap.put(o.getId(), o);
		}
		return voMap;
	}
	
	public final static List<GroupItItem> transItIdList2GroupItItemList(int[] ids){
		if(ids == null){
			return new ArrayList<GroupItItem>();
		}
		List<GroupItItem> result = new ArrayList<GroupItItem>(ids.length);
		for(int index = 0; index < ids.length; index++){
			result.add(new GroupItItem(ids[index], false));
		}
		return result;
	}
	
	/**
	 * 验证兴趣点（组合）是否是存在于推广组设置中的
	 * @param result
	 * @param fromInterestIds
	 * @param toInterestIdsSet
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @param validInterestIdsSet 保存合法的it集合
	 * @return boolean 是否验证成功
	 */
	public static boolean validateDeleteInterestExist(ApiResult<Object> result,
			List<GroupItItem> fromInterestIds, 
			Set<Integer> toInterestIdsSet, 
			String apiErrorParam,
			String position, 
			boolean exitWhenError,
			Set<Integer> validInterestIdsSet) {
		
		//如果兴趣点为空，则不做验证
		if(fromInterestIds == null || fromInterestIds.size() == 0){
			return true;
		}
		
		for(int index = 0; index < fromInterestIds.size(); index++){
			GroupItItem interestItem = fromInterestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			
			if(toInterestIdsSet.contains(interestId)){
				validInterestIdsSet.add(interestId);
				continue;
			}
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(apiErrorParam);
			apiPosition.addParam(position, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue(),
					GroupConfigErrorCode.INTEREST_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
			
			interestItem.setHasError(true);
			validInterestIdsSet.remove(interestId);
			
			if(exitWhenError){
				return false;
			} else {
				continue;
			}
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证兴趣点（组合）是否是有效存在的
	 * @param result
	 * @param interestIds
	 * @param interestMap
	 * @param customInterestMap
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @param validInterestIdsSet 保存合法的it集合
	 * @return boolean 是否验证成功
	 */
	public static boolean validateInterestExist(ApiResult<Object> result,
			List<GroupItItem> interestIds, 
			Map<Integer, Interest> interestMap,
			Map<Integer, CustomInterestVo> customInterestMap, 
			String apiErrorParam,
			String position,
			boolean exitWhenError,
			Set<Integer> validInterestIdsSet) {
		
		//如果兴趣点为空，则不做验证
		if(interestIds == null || interestIds.size() == 0){
			return true;
		}
		
		for(int index = 0; index < interestIds.size(); index++){
			GroupItItem interestItem = interestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			
			//如果是兴趣点
			if(interestId <= InterestConstant.MAX_INTEREST_ID){
				
				if(interestMap.containsKey(interestId)){
					validInterestIdsSet.add(interestId);
					continue;
				}
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(apiErrorParam);
				apiPosition.addParam(position, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
				
				interestItem.setHasError(true);
				validInterestIdsSet.remove(interestId);
				
				if(exitWhenError){
					return false;
				} else {
					continue;
				}
			} else {
				//验证是否是有效的兴趣组合
				if(customInterestMap.containsKey(interestId)){
					validInterestIdsSet.add(interestId);
					continue;
				}
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(apiErrorParam);
				apiPosition.addParam(position, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
				
				interestItem.setHasError(true);
				validInterestIdsSet.remove(interestId);
				
				if(exitWhenError){
					return false;
				} else {
					continue;
				}
			}
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证关联兴趣结合的不包含在排除兴趣中，反之亦然
	 * @param result
	 * @param fromInterestIds
	 * @param toInterestIds
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @param validInterestIdsSet 保存合法的it集合
	 * @return boolean 验证是否成功
	 */
	public static boolean validateInterestNotDuplicate(ApiResult<Object> result,
			List<GroupItItem> fromInterestIds, 
			Set<Integer> toInterestIdsSet, 
			String apiErrorParam,
			String position, 
			boolean exitWhenError,
			Set<Integer> validInterestIdsSet) {
		if(fromInterestIds == null || fromInterestIds.size() == 0){
			return true;
		}
		
		for(int index = 0; index < fromInterestIds.size(); index++){
			GroupItItem interestItem = fromInterestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			if(toInterestIdsSet.contains(interestId)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(apiErrorParam);
				apiPosition.addParam(position, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getValue(),
						GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getMessage(), apiPosition.getPosition(), null);
				
				interestItem.setHasError(true);
				validInterestIdsSet.remove(interestId);
				
				if(exitWhenError){
					return false;
				} else {
					continue;
				}
			}
			validInterestIdsSet.add(interestId);
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证删除关联兴趣时，兴趣不应该重复出现
	 * @param result
	 * @param fromInterestIds
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @return boolean 验证是否成功
	 */
	public static boolean validateInterestNotShowMoreThanOnce(ApiResult<Object> result,
			List<GroupItItem> fromInterestIds, 
			String apiErrorParam,
			String position, 
			boolean exitWhenError) {
		if(fromInterestIds == null || fromInterestIds.size() == 0){
			return true;
		}
		
		Set<Integer> alreadyCountInterestIdsSet = new HashSet<Integer>();
		for(int index = 0; index < fromInterestIds.size(); index++){
			GroupItItem interestItem = fromInterestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			if(alreadyCountInterestIdsSet.contains(interestId)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(apiErrorParam);
				apiPosition.addParam(position, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_DUP_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_DUP_ERROR.getMessage(), apiPosition.getPosition(), null);
				
				interestItem.setHasError(true);
				alreadyCountInterestIdsSet.remove(interestId);
				
				if(exitWhenError){
					return false;
				} else {
					continue;
				}
			}
			alreadyCountInterestIdsSet.add(interestId);
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证关联兴趣结合的子节点不包含在排除兴趣中，反之亦然
	 * @param result
	 * @param fromInterestIds
	 * @param toInterestIds
	 * @param cache
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @param validInterestIdsSet 保存合法的it集合
	 * @return boolean 验证是否成功
	 */
	public static boolean validateInterestNotDuplicateInChildren(ApiResult<Object> result,
			List<GroupItItem> fromInterestIds, 
			Set<Integer> toInterestIdsSet, 
			Map<Integer, InterestCacheObject> cache, 
			String apiErrorParam,
			String position, 
			boolean exitWhenError,
			Set<Integer> validInterestIdsSet) {
		if(fromInterestIds == null || fromInterestIds.size() == 0){
			return true;
		}

		loop: for(int index = 0; index < fromInterestIds.size(); index++){
			GroupItItem interestItem = fromInterestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			
			if(interestId <= InterestConstant.MAX_INTEREST_ID){
				InterestCacheObject o = cache.get(interestId);
				if(o == null){
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(apiErrorParam);
					apiPosition.addParam(position, index);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue(),
							GroupConfigErrorCode.INTEREST_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
					
					interestItem.setHasError(true);
					validInterestIdsSet.remove(interestId);
					
					if(exitWhenError){
						return false;
					} else {
						continue;
					}
				}
				List<Integer> childrenKeys = o.getAllChildrenKeys();
				if(childrenKeys != null){
					for(Integer key : childrenKeys){
						if(toInterestIdsSet.contains(key)){
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(apiErrorParam);
							apiPosition.addParam(position, index);
							
							result = ApiResultBeanUtils.addApiError(result,
									GroupConfigErrorCode.INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue(),
									GroupConfigErrorCode.INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getMessage(), apiPosition.getPosition(), null);
							
							interestItem.setHasError(true);
							validInterestIdsSet.remove(interestId);
							
							if(exitWhenError){
								return false;
							} else {
								continue loop;
							}
						}
					}
				}
				validInterestIdsSet.add(interestId);
			}
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 验证关联兴趣结合的子节点不包含在排除兴趣中，反之亦然
	 * @param result
	 * @param fromInterestIds
	 * @param toInterestIds
	 * @param cache
	 * @param apiErrorParam
	 * @param position
	 * @param exitWhenError 是否发现错误就强制返回
	 * @param validInterestIdsSet 保存合法的it集合
	 * @return boolean 验证是否成功
	 */
	public static boolean validateInterestNotDuplicateInParent(ApiResult<Object> result,
			List<GroupItItem> fromInterestIds, 
			Set<Integer> toInterestIdsSet, 
			Map<Integer, InterestCacheObject> cache, 
			String apiErrorParam,
			String position, 
			boolean exitWhenError,
			Set<Integer> validInterestIdsSet) {
		if(fromInterestIds == null || fromInterestIds.size() == 0){
			return true;
		}

		loop: for(int index = 0; index < fromInterestIds.size(); index++){
			GroupItItem interestItem = fromInterestIds.get(index);
			Integer interestId = interestItem.getItId();
			if(interestItem.isHasError()){
				continue;
			}
			
			if(interestId <= InterestConstant.MAX_INTEREST_ID){
				InterestCacheObject o = cache.get(interestId);
				if(o == null){
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(apiErrorParam);
					apiPosition.addParam(position, index);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue(),
							GroupConfigErrorCode.INTEREST_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
					
					interestItem.setHasError(true);
					validInterestIdsSet.remove(interestId);
					
					if(exitWhenError){
						return false;
					} else {
						continue;
					}
				}
				Integer parentId = o.getParentId();
				if(parentId != null && parentId != 0){
					if(toInterestIdsSet.contains(parentId)){
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(apiErrorParam);
						apiPosition.addParam(position, index);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.INTEREST_PARENT_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue(),
								GroupConfigErrorCode.INTEREST_PARENT_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getMessage(), apiPosition.getPosition(), null);
						
						interestItem.setHasError(true);
						validInterestIdsSet.remove(interestId);
						
						if(exitWhenError){
							return false;
						} else {
							continue loop;
						}
					}
				}
				validInterestIdsSet.add(interestId);
			}
		}
		
		if(result.hasErrors()){
			return false;
		}
		return true;
	}
	
}
