package com.baidu.beidou.api.external.cprogroup.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;
import com.baidu.beidou.cprogroup.vo.VtPeopleExcludeMapperVo;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;

/**
 * 
 * ClassName: APIGroupUtil  <br>
 * Function: API推广组设置工具类
 * 
 * @author zhangxu
 * @date Sep 4, 2012
 */
public class APIGroupUtil {
	
	public static Set<Long> transVtVo2LongSet(List<CproGroupVTVo> vtVoList) {
		Set<Long> result = new HashSet<Long>();
		for (CproGroupVTVo people : vtVoList) {
			result.add(people.getId());
		}
		return result;
	}
	
	public static Set<Long> transExcludeVtVo2LongSet(List<VtPeopleExcludeMapperVo> vtVoList) {
		Set<Long> result = new HashSet<Long>();
		for (VtPeopleExcludeMapperVo vo : vtVoList) {
			result.add(vo.getPid());
		}
		return result;
	}
	
	public static List<Integer> trans2GroupIdList(Integer groupId) {
		List<Integer> result = new ArrayList<Integer>(1);
		result.add(groupId);
		return result;
	}
	
	/**
	 * 验证API中GroupType中的type值是否合法，包含了是否启用高级组合设置
	 * @param groupType
	 * @return boolean 
	 */
	public static boolean isAdvancedTargetType(Integer groupType){
		if (groupType == null || groupType == 0 || groupType < GroupConstant.API_GROUP_TYPE_ADVANCED) {
			return false;
		}
		boolean isAdvancedTargetType = ((GroupConstant.API_GROUP_TYPE_ADVANCED ^ groupType) > 0) ;
		return isAdvancedTargetType;
	}
	
	/**
	 * 
	 * 判断推广组所属的推广计划是否是仅移动的
	 *
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static boolean isInheritWirelessGroup(CproGroupMgr cproGroupMgr, Integer userId, Integer groupId) {
		List<Integer> groupIdList = new ArrayList<Integer>();
		groupIdList.add(groupId);
		Map<Integer, Integer> groupPromotionTypeMapping = cproGroupMgr.getGroupPromotionTypeMapping(userId, groupIdList);
		int promotionType = groupPromotionTypeMapping.get(groupId);
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 判断推广组所属的推广计划是否是仅移动的
	 *
	 * @param userId
	 * @param planId
	 * @return
	 */
	public static boolean isInheritWirelessPlan(CproPlanMgr cproPlanMgr, Integer userId, Integer planId) {
		List<Integer> planIdList = new ArrayList<Integer>();
		planIdList.add(planId);
		Map<Integer, Integer> groupPromotionTypeMapping = cproPlanMgr.getPlanPromotionTypeMapping(userId, planIdList);
		int promotionType = groupPromotionTypeMapping.get(planId);
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS) {
			return true;
		}
		return false;
	}
	
}
