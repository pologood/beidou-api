package com.baidu.beidou.api.external.cprogroup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.service.ApiGroupService;
import com.baidu.beidou.api.external.cprogroup.util.APIGroupUtil;
import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.api.external.cprogroup.util.GroupBoMappingUtil;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cproplan.constant.PlanConstant;
import com.baidu.beidou.api.external.cproplan.error.PlanErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.constant.WhiteListCache;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.util.GenderInfoUtil;
import com.baidu.beidou.cprogroup.util.GroupTypeUtil;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.GroupRegionOptVo;
import com.baidu.beidou.cprogroup.vo.GroupSiteOptVo;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.OperationHistoryUtils;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.StringUtils;
import com.google.common.base.Objects;

/**
 * ClassName: ApiGroupServiceImpl
 * Function: 推广组新增及修改内部service
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class ApiGroupServiceImpl implements ApiGroupService {

	private CproGroupMgr cproGroupMgr;

	private CproPlanMgr cproPlanMgr;
	
	private CproUnitMgr unitMgr = null;
	
	private static Log log = LogFactory.getLog(ApiGroupServiceImpl.class);

	/*
	 * 验证新增推广组
	 */
	private ApiError validateAddGroup(CproGroup group, int index) {
		if (group == null || index < 0 || group.getGroupInfo() == null) {
			return null;
		}

		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(GroupConstant.GROUP_INFO, index);

		// 1. 推广组名称：不可为空，长度("stringgbklength")为1-20，

		if (org.apache.commons.lang.StringUtils.isEmpty(group.getGroupName())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);

			return new ApiError(GroupErrorCode.NAME_EMPTY.getValue(),
					GroupErrorCode.NAME_EMPTY.getMessage(), 
					apiPosition.getPosition(), null);
		}

		if (StringUtils.validateHasSpecialChar(group.getGroupName())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);

			return new ApiError(GroupErrorCode.NAME_SPECIAL.getValue(),
					GroupErrorCode.NAME_SPECIAL.getMessage(), 
					apiPosition.getPosition(), group.getGroupName());
		}

		if (!StringUtils.validBeidouGbkStr(group.getGroupName(), true, 1, GroupConstant.LENGTH_GROUP_NAME)) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);

			return new ApiError(GroupErrorCode.LENGTH_NAME_MAX.getValue(),
					GroupErrorCode.LENGTH_NAME_MAX.getMessage(), 
					apiPosition.getPosition(), group.getGroupName());
		}

		// 2. 推广组名称重复
		if (cproGroupMgr.hasRepeateName(group.getPlanId(), group.getGroupName())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);

			return new ApiError(GroupErrorCode.NAME_REPEAT.getValue(),
					GroupErrorCode.NAME_REPEAT.getMessage(), 
					apiPosition.getPosition(), group.getGroupName());
		}

		// 3. 推广组状态为生效或者搁置
		if (group.getGroupState() == null) {
			group.setGroupState(CproGroupConstant.GROUP_STATE_NORMAL);
		}
		if (group.getGroupState() != CproGroupConstant.GROUP_STATE_NORMAL
				&& group.getGroupState() != CproGroupConstant.GROUP_STATE_PAUSE) {
			apiPosition.addParam(GroupConstant.POSITION_STATUS);

			return new ApiError(GroupErrorCode.STATE_INVALID.getValue(),
					GroupErrorCode.STATE_INVALID.getMessage(), 
					apiPosition.getPosition(), null);
		}

		if (group.getGroupType() == null) {
			group.setGroupType(GroupConstant.ADD_GROUP_DEFAULT_GROUP_TYPE);
		}

		// 4. 点击价格[0.01元,999.99元]
		if (group.getGroupInfo().getPrice() == null
				|| !CproGroupConstant.isValidPriceValueRange(group
						.getGroupInfo().getPrice())) {
			apiPosition.addParam(GroupConstant.POSITION_PRICE);

			return new ApiError(GroupErrorCode.PRICE_INVALID.getValue(),
					GroupErrorCode.PRICE_INVALID.getMessage(), 
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupInfo().getPrice()));
		} else if (!isReasonablePrice(group)) {
			apiPosition.addParam(GroupConstant.POSITION_PRICE);

			return new ApiError(GroupErrorCode.PRICE_NOT_REASONABLE.getValue(),
					GroupErrorCode.PRICE_NOT_REASONABLE.getMessage(),
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupInfo().getPrice()));
		}

		// 5. 非删除状态的推广组个数达到上限：1000
		Long effectiveNumNow = cproGroupMgr.countEffectiveCproGroupByPlanId(group.getPlanId());
		if (effectiveNumNow >= CproGroupConstant.GROUP_ALL_MAX_NUM) {
			return new ApiError(GroupErrorCode.MAX_EFFECTIVE_NUMBER.getValue(),
					GroupErrorCode.MAX_EFFECTIVE_NUMBER.getMessage(),
					apiPosition.getPosition(), null);
		}

		/*
		 * //6. 非删除的推广组个数未达到上限：200 curCnt =
		 * cproGroupMgr.countEffectiveCproGroupByPlanId(group.getPlanId());
		 * if(curCnt>=CproGroupConstant.GROUP_EFFECTIVE_MAX_NUM){ return new
		 * ApiError(GroupErrorCode.MAX_EFFECTIVE_NUMBER.getValue(),
		 * GroupErrorCode.MAX_EFFECTIVE_NUMBER.getMessage(), groupPos, null); }
		 */

		// 7. 推广组类型不正确；
		// modified by genglei, @version cpweb-443
		// 非白名单用户无法提交选择了贴片展现类型的推广组
		if (!WhiteListCache.baiduFilm.has(group.getUserId())
				&& GroupTypeUtil.containsFilm(group.getGroupType())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);

			return new ApiError(GroupErrorCode.NO_FILM_PRIVELEGE.getValue(),
					GroupErrorCode.NO_FILM_PRIVELEGE.getMessage(), 
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}
		
		// grouptype限制
		if (!GroupTypeUtil.isValid(group.getGroupType())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);

			return new ApiError(
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}
		
		// excludeGender限制
		if(isGenderInfoInvalid(group.getGroupInfo().getGenderInfo())){
			apiPosition.addParam(GroupConstant.POSITION_GROUP_GENDERINFO);
			return new ApiError(
					GroupErrorCode.GROUP_GENDERINFO_WRONG.getValue(),
					GroupErrorCode.GROUP_GENDERINFO_WRONG.getMessage(),
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}

		return null;
	}

	private boolean isReasonablePrice(CproGroup group) {
		CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
		if (plan == null || group == null || group.getGroupInfo() == null) {
			return false;
		}
		int budget = plan.getBudget();

		// 合理的点击价格需小于等于每日预算
		// budget单位转换成：分
		return (group.getGroupInfo().getPrice() <= budget * 100);
	}

	/**
	 * 
	 * @param visitor
	 * @param group
	 * @param destCproGroup
	 * @param planId
	 * @param nullable
	 *            nullable=true表示为修改，空表示不修改
	 * @return
	 */
	private ApiError validateUpdateGroup(Visitor visitor, int index,
			CproGroup group, CproGroup destCproGroup, Integer planId,
			boolean nullable) {
		// ---------------------------推广组名称---------------------------------//

		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(GroupConstant.GROUP_INFO, index);
		if (!nullable || (nullable && group.getGroupName() != null)) {
			// 如果推广组名称为""
			if (group.getGroupName().length() == 0) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_EMPTY.getValue(),
						GroupErrorCode.NAME_EMPTY.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}
			if (StringUtils.validateHasSpecialChar(group.getGroupName())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_SPECIAL.getValue(),
						GroupErrorCode.NAME_SPECIAL.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}

			if (!StringUtils.validBeidouGbkStr(group.getGroupName(), true, 1, GroupConstant.LENGTH_GROUP_NAME)) {
				LogUtils.businessBatchInfo(visitor,
						"add CproGroup group name invalid", group.toString());
				
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				return new ApiError(GroupErrorCode.LENGTH_NAME_MAX.getValue(),
						GroupErrorCode.LENGTH_NAME_MAX.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}
		}

		// ---------------------------状态---------------------------------//
		if (!nullable || (nullable && group.getGroupState() != null)) {
			if (group.getGroupState() != CproGroupConstant.GROUP_STATE_NORMAL
					&& group.getGroupState() != CproGroupConstant.GROUP_STATE_PAUSE
					&& group.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE) {
				LogUtils.businessBatchInfo(visitor,
						"add CproGroup state invalid", group.toString());
				
				apiPosition.addParam(GroupConstant.POSITION_STATUS);
				return new ApiError(GroupErrorCode.STATE_INVALID.getValue(),
						GroupErrorCode.STATE_INVALID.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		// ---------------------------价格---------------------------------//
		if (!nullable || (nullable && group.getGroupInfo().getPrice() != null)) {
			if(group.getGroupInfo().getPrice() != GroupConstant.GROUP_PRICE_NOT_CHANGE_VALUE){
				if (!CproGroupConstant.isValidPriceValueRange(group.getGroupInfo().getPrice())) {
					LogUtils.businessBatchInfo(visitor,
							"add CproGroup price invalid", group.toString());
					
					apiPosition.addParam(GroupConstant.POSITION_PRICE);
					return new ApiError(GroupErrorCode.PRICE_INVALID.getValue(),
							GroupErrorCode.PRICE_INVALID.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (!isReasonablePrice(group)) {
					apiPosition.addParam(GroupConstant.POSITION_PRICE);
					
					return new ApiError(GroupErrorCode.PRICE_NOT_REASONABLE.getValue(), 
							GroupErrorCode.PRICE_NOT_REASONABLE.getMessage(), 
							apiPosition.getPosition(), 
							String.valueOf(group.getGroupInfo().getPrice()));
				}
			} // -1表示更新推广组的出价，这里做了特殊处理
		}

		if (!nullable || (nullable && group.getGroupName() != null && planId != null)) {
			if (cproGroupMgr.hasRepeateNameExcludeSelf(group.getPlanId(), 
					group.getGroupId(), group.getGroupName())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_REPEAT.getValue(),
						GroupErrorCode.NAME_REPEAT.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		// 推广组类型
		int postGroupType = group.getGroupType();
		int preGroupType = destCproGroup.getGroupType();
		if (!nullable || (nullable && group.getGroupType() != null 
				&& preGroupType != postGroupType && (preGroupType & postGroupType) != preGroupType)) {
			// 推广组类型发生变化，原groupType有bit位被置0
			apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
			
			return new ApiError(GroupErrorCode.TYPE_CANT_CHANGE.getValue(),
					GroupErrorCode.TYPE_CANT_CHANGE.getMessage(), 
					apiPosition.getPosition(), null);
		}
		
		// modified by genglei, @version cpweb-443
		// 非白名单用户无法为推广组选择贴片展现类型
		if (GroupTypeUtil.containsFilm(group.getGroupType())
				&& !WhiteListCache.baiduFilm.has(group.getUserId())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
			
			return new ApiError(GroupErrorCode.NO_FILM_MOD_PRIVELEGE.getValue(),
					GroupErrorCode.NO_FILM_MOD_PRIVELEGE.getMessage(),
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}
		// grouptype限制
		if (!GroupTypeUtil.isValid(group.getGroupType())) {
			apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
			
			return new ApiError(GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), 
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}

		// excludeGender限制
		if(isGenderInfoInvalid(group.getGroupInfo().getGenderInfo())){
			apiPosition.addParam(GroupConstant.POSITION_GROUP_GENDERINFO);
			return new ApiError(
					GroupErrorCode.GROUP_GENDERINFO_WRONG.getValue(),
					GroupErrorCode.GROUP_GENDERINFO_WRONG.getMessage(),
					apiPosition.getPosition(), 
					String.valueOf(group.getGroupType()));
		}
		
		return null;
	}
	
	/**
	 * 验证更新推广组
	 * @param visitor
	 * @param index
	 * @param group
	 * @param destCproGroup
	 * @param planId
	 * @return
	 */
	private ApiError validateUpdateAdditionalGroup(Visitor visitor, int index,
			CproGroup group, CproGroup destCproGroup, Integer planId) {
		
		// ---------------------------推广组名称---------------------------------//
		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(GroupConstant.GROUP_INFO, index);
		if (group.getGroupName() != null) {
			// 如果推广组名称为""
			if (group.getGroupName().length() == 0) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_EMPTY.getValue(),
						GroupErrorCode.NAME_EMPTY.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}
			if (StringUtils.validateHasSpecialChar(group.getGroupName())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_SPECIAL.getValue(),
						GroupErrorCode.NAME_SPECIAL.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}

			if (!StringUtils.validBeidouGbkStr(group.getGroupName(), true, 1, GroupConstant.LENGTH_GROUP_NAME)) {
				LogUtils.businessBatchInfo(visitor,
						"add CproGroup group name invalid", group.toString());
				
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				return new ApiError(GroupErrorCode.LENGTH_NAME_MAX.getValue(),
						GroupErrorCode.LENGTH_NAME_MAX.getMessage(), 
						apiPosition.getPosition(), group.getGroupName());
			}
		}
		if (group.getGroupName() != null && planId != null) {
			if (cproGroupMgr.hasRepeateNameExcludeSelf(group.getPlanId(), 
					group.getGroupId(), group.getGroupName())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPNAME);
				
				return new ApiError(GroupErrorCode.NAME_REPEAT.getValue(),
						GroupErrorCode.NAME_REPEAT.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		// ---------------------------状态---------------------------------//
		if (group.getGroupState() != null) {
			if (group.getGroupState() != CproGroupConstant.GROUP_STATE_NORMAL
					&& group.getGroupState() != CproGroupConstant.GROUP_STATE_PAUSE
					&& group.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE) {
				LogUtils.businessBatchInfo(visitor,
						"add CproGroup state invalid", group.toString());
				
				apiPosition.addParam(GroupConstant.POSITION_STATUS);
				return new ApiError(GroupErrorCode.STATE_INVALID.getValue(),
						GroupErrorCode.STATE_INVALID.getMessage(), 
						apiPosition.getPosition(), null);
			}
			
			if (!group.getGroupState().equals(destCproGroup.getGroupState())) {
				if (group.getGroupState() == CproGroupConstant.GROUP_STATE_PAUSE) { // 暂停推广组
					if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_NORMAL) {
						destCproGroup.setGroupState(group.getGroupState());
					} else {
						apiPosition.addParam(GroupConstant.POSITION_STATUS);
						return new ApiError(GroupErrorCode.GROUP_STATE_ERROR.getValue(),
								GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
								apiPosition.getPosition(), null);
					}
				} else if (group.getGroupState() == CproGroupConstant.GROUP_STATE_NORMAL) { // 恢复推广组
					if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE) {

						// 恢复推广组时，要判断是否超过单个计划下非删除推广组的最多个数
						Long effectiveNumNow = cproGroupMgr.countEffectiveCproGroupByPlanId(destCproGroup.getPlanId());
						if (effectiveNumNow >= CproGroupConstant.GROUP_ALL_MAX_NUM) {
							apiPosition.addParam(GroupConstant.POSITION_STATUS);
							return new ApiError(GroupErrorCode.GROUP_STATE_ERROR.getValue(),
									GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
									apiPosition.getPosition(), 
									GroupErrorCode.STATE_INVALID.getMessage());
						}
						destCproGroup.setGroupState(group.getGroupState());
					} else if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_PAUSE) {
						destCproGroup.setGroupState(group.getGroupState());
					} else {
						apiPosition.addParam(GroupConstant.POSITION_STATUS);
						return new ApiError(GroupErrorCode.GROUP_STATE_ERROR.getValue(),
								GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
								apiPosition.getPosition(), null);
					}
				} else if (group.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE) { // 删除推广组
					destCproGroup.setGroupState(group.getGroupState());
				} else {
					apiPosition.addParam(GroupConstant.POSITION_STATUS);
					return new ApiError(GroupErrorCode.GROUP_STATE_ERROR.getValue(),
							GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
							apiPosition.getPosition(), 
							GroupErrorCode.STATE_INVALID.getMessage());
				}
			}
		}

		// ---------------------------价格---------------------------------//
		if (group.getGroupInfo().getPrice() != null) {
			if(group.getGroupInfo().getPrice() != GroupConstant.GROUP_PRICE_NOT_CHANGE_VALUE){
				if (!CproGroupConstant.isValidPriceValueRange(group.getGroupInfo().getPrice())) {
					LogUtils.businessBatchInfo(visitor,
							"add CproGroup price invalid", group.toString());
					
					apiPosition.addParam(GroupConstant.POSITION_PRICE);
					return new ApiError(GroupErrorCode.PRICE_INVALID.getValue(),
							GroupErrorCode.PRICE_INVALID.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (!isReasonablePrice(group)) {
					apiPosition.addParam(GroupConstant.POSITION_PRICE);
					
					return new ApiError(GroupErrorCode.PRICE_NOT_REASONABLE.getValue(), 
							GroupErrorCode.PRICE_NOT_REASONABLE.getMessage(), 
							apiPosition.getPosition(), 
							String.valueOf(group.getGroupInfo().getPrice()));
				}
			} // -1表示更新推广组的出价，这里做了特殊处理
		}

		// ---------------------------类型---------------------------------//
		if (group.getGroupType() != null) {
			
			// grouptype限制
			if (!GroupTypeUtil.isValid(group.getGroupType())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
				
				return new ApiError(GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), 
						apiPosition.getPosition(), 
						String.valueOf(group.getGroupType()));
			}
			// 非白名单用户无法为推广组选择贴片展现类型
			if (GroupTypeUtil.containsFilm(group.getGroupType())
					&& !WhiteListCache.baiduFilm.has(group.getUserId())) {
				apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
				
				return new ApiError(GroupErrorCode.NO_FILM_MOD_PRIVELEGE.getValue(),
						GroupErrorCode.NO_FILM_MOD_PRIVELEGE.getMessage(),
						apiPosition.getPosition(), 
						String.valueOf(group.getGroupType()));
			}
			
			int preGroupType = destCproGroup.getGroupType();
			int postGroupType = group.getGroupType();
			int groupId = group.getGroupId();
			int userId = group.getUserId();
			int fixed = CproGroupConstant.GROUP_TYPE_FIXED,
				film = CproGroupConstant.GROUP_TYPE_FILM,
				flow = CproGroupConstant.GROUP_TYPE_FLOW,
				toBeCancelledGroupType = (~postGroupType) & preGroupType,
				invalidGroupType = 0;
			//如果postGroupType只包含贴片，那么就不能忽略300*250的尺寸；否则，忽略该尺寸
			boolean discardScale = true;
			if(postGroupType == film){
				discardScale = false;
			}
			if(GroupTypeUtil.containsFixed(toBeCancelledGroupType)){
				if(unitMgr.existsUnitForGroupIdAndGroupType(userId, groupId, fixed, discardScale)){
					invalidGroupType = invalidGroupType| fixed;
				}
			}
			if(GroupTypeUtil.containsFlow(toBeCancelledGroupType)){
				if(unitMgr.existsUnitForGroupIdAndGroupType(userId, groupId, flow, discardScale)){
					invalidGroupType = invalidGroupType| flow;
				}
			}
			if(GroupTypeUtil.containsFilm(toBeCancelledGroupType)){
				if(unitMgr.existsUnitForGroupIdAndGroupType(userId, groupId, film, discardScale)){
					invalidGroupType = invalidGroupType| film;
				}
			}
			if(invalidGroupType > 0){
				apiPosition.addParam(GroupConstant.POSITION_GROUPTYPE);
				
				return new ApiError(GroupErrorCode.GROUP_TYPE_CAN_NOT_UPDATE.getValue(), 
						GroupErrorCode.GROUP_TYPE_CAN_NOT_UPDATE.getMessage(), 
						apiPosition.getPosition(), 
						null);
			}
			
			// 如果前后类型相同则不需要更新
			if(preGroupType == postGroupType){
				group.setGroupType(null);
			}
		}
		
		// ---------------------------排除性别---------------------------------//
		if (group.getGroupInfo().getGenderInfo() != null) {
			if(isGenderInfoInvalid(group.getGroupInfo().getGenderInfo())){
				apiPosition.addParam(GroupConstant.POSITION_GROUP_GENDERINFO);
				return new ApiError(
						GroupErrorCode.GROUP_GENDERINFO_WRONG.getValue(),
						GroupErrorCode.GROUP_GENDERINFO_WRONG.getMessage(),
						apiPosition.getPosition(), 
						String.valueOf(group.getGroupType()));
			}
		}
		
		return null;
	}

	private boolean isGenderInfoInvalid(Integer genderInfo) {
		return !GenderInfoUtil.isValidGenderInfo(genderInfo);
	}

	public ApiResult<GroupType> addGroup(ApiResult<GroupType> result,
			int index, GroupType group, List<OptContent> optContents) {

		Integer planId = Integer.parseInt(String.valueOf(group.getCampaignId()));
		PaymentResult pay = result.getPayment();

		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);

        // bean映射
        group.setGroupId(0);
        Mapper mapper = BeanMapperProxy.getMapper();
        CproGroup destGroup = mapper.map(group, CproGroup.class);
        CproGroupInfo destInfo = destGroup.getGroupInfo();

		destGroup.setGroupId(null); // 必须有，表示为新增推广组
		destGroup.setPlanId(planId);
		destGroup.setUserId(bdUser.getUserid());
		destGroup.setAddUserId(visitor.getUserid());
		destGroup.setModUserId(visitor.getUserid());
		destGroup.setAddTime(new Date());
		destGroup.setModTime(new Date());
		
		/**
		 * 特殊处理逻辑，当type ^ 8 > 0的时候，认为是启用来高级组合设置
		 * @since beidou 3Plus
		 */
		if (APIGroupUtil.isAdvancedTargetType(group.getType())) {
			int allType = CproGroupConstant.GROUP_TYPE_FIXED | CproGroupConstant.GROUP_TYPE_FLOW | CproGroupConstant.GROUP_TYPE_FILM;
			group.setType(group.getType() & allType);
			destGroup.setGroupType(group.getType() & allType);
			destGroup.setTargetType(CproGroupConstant.GROUP_TARGET_TYPE_PACK);
		} else {
			destGroup.setTargetType(CproGroupConstant.GROUP_TARGET_TYPE_NONE);
		}
		
		destInfo.setUserId(bdUser.getUserid());
		destInfo.setIsAllRegion(CproGroupConstant.GROUP_ALLREGION);
		destInfo.setRegSum(UnionSiteCache.regCache.getFirstRegSum());
		destInfo.setIsAllSite(CproGroupConstant.GROUP_ALLSITE);
		destInfo.setSiteSum(0);
		destInfo.setCmpLevel(CproGroupConstant.CMP_LEVEL_NONE);
		destInfo.setGroup(destGroup);

		ApiError code = this.validateAddGroup(destGroup, index);

		if (code != null) {
			result = ApiResultBeanUtils.addApiError(result, code);
			return result;
		}
		
		// ---------------------------先提交推广组---------------------------------//
		CproGroup cproGroup = cproGroupMgr.addCproGroup(visitor, destGroup);

		if (cproGroup == null) {
			LogUtils.businessBatchInfo(visitor, "add CproGroup commit fail1",
					group.toString());

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO, index);

			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.GROUP_CTREATION_ERROR.getValue(),
					GroupErrorCode.GROUP_CTREATION_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		group.setGroupId(cproGroup.getGroupId());
		pay.increSuccess();
		ApiResultBeanUtils.addApiResult(result, group);
		
		//------- 保存历史操作记录 start ------
		try{
			OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_NEW;
			optContents.add(new OptContent(cproGroup.getUserId(),
					optype.getOpType(),
					optype.getOpLevel(), 
					cproGroup.getGroupId(), 
					null,
					optype.getTransformer().toDbString(cproGroup.getGroupName())));
		} catch(Exception e){
			log.error("failed to contruct opt history content. " + e.getMessage(), e);
		}
		//------- 保存历史操作记录 end ------
		
		return result;
	}

	public ApiResult<GroupType> updateGroup(ApiResult<GroupType> result,
			DataPrivilege user, GroupType[] groupTypes) {

		PaymentResult pay = result.getPayment();

		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int i = 0; i < groupTypes.length; i++) {
			GroupType groupType = groupTypes[i];

			if (groupType == null || groupType.getGroupId() <= 0
					|| groupType.getCampaignId() <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUP_INFO, i);

				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.GROUP_CTREATION_ERROR.getValue(),
						GroupErrorCode.GROUP_CTREATION_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			Integer planId = Integer.parseInt(String.valueOf(groupType.getCampaignId()));
			Integer groupId = Integer.parseInt(String.valueOf(groupType.getGroupId()));

			User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);

			Mapper mapper = BeanMapperProxy.getMapper();
			CproGroup group = mapper.map(groupType, CproGroup.class);

			group.setGroupId(groupId);
			group.setPlanId(planId);
			group.setUserId(bdUser.getUserid());
			group.setModUserId(visitor.getUserid());
			group.setModTime(new Date());

			CproGroupInfo groupInfo = new CproGroupInfo();
			groupInfo.setPrice(groupType.getPrice());
			groupInfo.setGenderInfo(groupType.getExcludeGender());
			group.setGroupInfo(groupInfo);

			CproGroup destCproGroup = cproGroupMgr.findWithInfoById(group.getGroupId());
			if (destCproGroup == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUP_INFO, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.NOT_FOUND.getValue(),
						GroupErrorCode.NOT_FOUND.getMessage(), 
						apiPosition.getPosition(),
						GroupErrorCode.NOT_FOUND.getMessage());
				continue;
			}
			CproGroup beforeCproGroup = new CproGroup();
			CproGroupInfo beforeCproGroupInfo = new CproGroupInfo();
			try{
				BeanUtils.copyProperties(beforeCproGroup, destCproGroup);
				BeanUtils.copyProperties(beforeCproGroupInfo, destCproGroup.getGroupInfo());
			} catch(Exception e){
				log.error("failed to copy cproplan. " + e.getMessage(), e);
			}
			
			if (group.getGroupType() == null) {
				// validate中使用了groupType校验白名单和单价验证必须使用该属性
				group.setGroupType(destCproGroup.getGroupType());
			}
			ApiError code = this.validateUpdateGroup(visitor, i, group,
					destCproGroup, planId, true);

			if (code != null) {
				result = ApiResultBeanUtils.addApiError(result, code);
				continue;
			}
			
			// 更新操作
			if (groupType.getStatus() == null) {
				// 状态不修改
				groupType.setStatus(destCproGroup.getGroupState());
			}
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO, i);
			if (groupType.getStatus() >= 0) {
				List<Integer> id = new ArrayList<Integer>(1);
				id.add(groupId);
				
				// 由于这里已经是Integer，因此比较不能再用==和!=，而需要更改为equals
				if (!groupType.getStatus().equals(destCproGroup.getGroupState())) {
					if (groupType.getStatus() == CproGroupConstant.GROUP_STATE_PAUSE) { // 暂停推广组
						if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_NORMAL) {
							destCproGroup.setGroupState(groupType.getStatus());
						} else {
							apiPosition.addParam(GroupConstant.POSITION_STATUS);
							
							result = ApiResultBeanUtils.addApiError(result,
									GroupErrorCode.GROUP_STATE_ERROR.getValue(),
									GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
									apiPosition.getPosition(), null);
							continue;
						}
					} else if (groupType.getStatus() == CproGroupConstant.GROUP_STATE_NORMAL) { // 恢复推广组
						if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE) {

							// 恢复推广组时，要判断是否超过单个计划下非删除推广组的最多个数
							Long effectiveNumNow = cproGroupMgr.countEffectiveCproGroupByPlanId(destCproGroup.getPlanId());
							if (effectiveNumNow >= CproGroupConstant.GROUP_ALL_MAX_NUM) {
								apiPosition.addParam(GroupConstant.POSITION_STATUS);
								
								result = ApiResultBeanUtils.addApiError(result,
										GroupErrorCode.GROUP_STATE_ERROR.getValue(),
										GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
										apiPosition.getPosition(), 
										GroupErrorCode.STATE_INVALID.getMessage());
								continue;
							}
							destCproGroup.setGroupState(groupType.getStatus());
						} else if (destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_PAUSE) {
							destCproGroup.setGroupState(groupType.getStatus());
						} else {
							apiPosition.addParam(GroupConstant.POSITION_STATUS);
							
							result = ApiResultBeanUtils.addApiError(result,
									GroupErrorCode.GROUP_STATE_ERROR.getValue(),
									GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
									apiPosition.getPosition(), null);
							continue;
						}
					} else if (groupType.getStatus() == CproGroupConstant.GROUP_STATE_DELETE) { // 删除推广组
						destCproGroup.setGroupState(groupType.getStatus());
					} else {
						apiPosition.addParam(GroupConstant.POSITION_STATUS);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupErrorCode.GROUP_STATE_ERROR.getValue(),
								GroupErrorCode.GROUP_STATE_ERROR.getMessage(),
								apiPosition.getPosition(), 
								GroupErrorCode.STATE_INVALID.getMessage());
						continue;
					}
				}
				
				// 更新推广组名称
				if (groupType.getGroupName() != null) {
					destCproGroup.setGroupName(groupType.getGroupName());
				}
				
				// 更新推广组出价
				if (groupType.getPrice() > 0) {
					destCproGroup.getGroupInfo().setPrice(groupType.getPrice());
				}
				
				// 更新展现类型
				if (groupType.getType() != null) {
					destCproGroup.setGroupType(groupType.getType());
				}
				
				// 更新排除性别
				// 翻译性别排除为性别正选，体验优化三期，by hejinggen
				//Integer convertedGenderInfo = GenderInfoUtil.convertGenderInfoToLatestStyle(groupType.getExcludeGender());//前端排除翻译为正选
				destCproGroup.getGroupInfo().setGenderInfo(groupType.getExcludeGender());
				
				// hexiufeng cpweb-696
				// 判断 normal or pause==>deleted,此时需要先设置为删除然后再修改其他字段数据
				// 删除时会自动添加plandelinfo
				if(beforeCproGroup.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE
						&& destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE){
					List<Integer> groupIdList = new LinkedList<Integer>();
					groupIdList.add(destCproGroup.getGroupId());
					cproGroupMgr.modStatetoDelete(visitor, groupIdList);
				}
				// 判断 deleted==>normal or pause,此时需要先恢复，如果恢复失败则报错
				if(beforeCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE
						&& destCproGroup.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE){
					List<Integer> groupIdList = new LinkedList<Integer>();
					groupIdList.add(destCproGroup.getGroupId());
					if(cproGroupMgr.findOutOfProtection(groupIdList).size() == 1){
						// 由于hibernate的自动提交问题，只要修改了处于持久化状态的对象，即使没有调用update方法，
						// 在数据库事务提交时也会被自动提交，这儿需要修改把group状态再修改回删除状态
						destCproGroup.setGroupState(CproGroupConstant.GROUP_STATE_DELETE);
						// 计划已经被删除，而且超出了恢复期，报错
						// 需要客户端来处理
						apiPosition.addParam(GroupConstant.POSITION_STATUS);
						result = ApiResultBeanUtils.addApiError(result,
								GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getValue(),
								GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getMessage(),
								apiPosition.getPosition(), 
								GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getMessage());
						continue;
					}
					cproGroupMgr.modStatetoResume(visitor, groupIdList);
				}
				
				// 修改推广组，提交
				CproGroup cproGroup = cproGroupMgr.modCproGroup(visitor, destCproGroup);

				if (cproGroup != null) {
					pay.increSuccess();
					ApiResultBeanUtils.addApiResult(result, groupType);
				} else {
					apiPosition.addParam(GroupConstant.POSITION_STATUS);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.GROUP_CTREATION_ERROR.getValue(),
							GroupErrorCode.GROUP_CTREATION_ERROR.getMessage(),
							apiPosition.getPosition(), 
							GroupErrorCode.STATE_INVALID.getMessage());
					continue;
				}
				
				//------- 保存历史操作记录 start ------
				try{
					if(!beforeCproGroup.getGroupName().equals(cproGroup.getGroupName())){
						OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_NAME;
						optContents.add(new OptContent(cproGroup.getUserId(),
								opType.getOpType(),
								opType.getOpLevel(), 
								Long.valueOf(cproGroup.getGroupId()), 
								opType.getTransformer().toDbString(beforeCproGroup.getGroupName()), 
								opType.getTransformer().toDbString(cproGroup.getGroupName())));
					}
					
					if(!beforeCproGroupInfo.getPrice().equals(cproGroup.getGroupInfo().getPrice())){
						OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_PRICE;
						optContents.add(new OptContent(cproGroup.getUserId(),
								opType.getOpType(),
								opType.getOpLevel(), 
								Long.valueOf(cproGroup.getGroupId()), 
								opType.getTransformer().toDbString(beforeCproGroupInfo.getPrice()), 
								opType.getTransformer().toDbString(cproGroup.getGroupInfo().getPrice())));
					}
					
					if(!beforeCproGroup.getGroupState().equals(cproGroup.getGroupState())){
						if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE){
							OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_DELETE;		
							optContents.add(new OptContent(cproGroup.getUserId(),
									optype.getOpType(),
									optype.getOpLevel(), 
									cproGroup.getGroupId(), 
									optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
									optype.getTransformer().toDbString(cproGroup.getGroupState())));
						}
						if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_PAUSE){
							OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_PAUSE;		
							optContents.add(new OptContent(cproGroup.getUserId(),
									optype.getOpType(),
									optype.getOpLevel(), 
									cproGroup.getGroupId(), 
									optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
									optype.getTransformer().toDbString(cproGroup.getGroupState())));
						}
						if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_NORMAL){
							OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_RESUME;		
							optContents.add(new OptContent(cproGroup.getUserId(),
									optype.getOpType(),
									optype.getOpLevel(), 
									cproGroup.getGroupId(), 
									optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
									optype.getTransformer().toDbString(cproGroup.getGroupState())));
						}
					}
					
					// 保存展现类型修改
					if(!Objects.equal(cproGroup.getGroupType(), beforeCproGroup.getGroupType())){
						OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_TYPE;		
						optContents.add(new OptContent(cproGroup.getUserId(),
								optype.getOpType(),
								optype.getOpLevel(), 
								cproGroup.getGroupId(), 
								optype.getTransformer().toDbString(beforeCproGroup.getGroupType()),
								optype.getTransformer().toDbString(cproGroup.getGroupType())));
					}
					
					if(!Objects.equal(cproGroup.getGroupInfo().getGenderInfo(), beforeCproGroup.getGroupInfo().getGenderInfo())){
						OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_GENDER_INFO;
						optContents.add(new OptContent(cproGroup.getUserId(),
								optype.getOpType(),
								optype.getOpLevel(), 
								cproGroup.getGroupId(), 
								optype.getTransformer().toDbString(beforeCproGroup.getGroupInfo().getGenderInfo()),
								optype.getTransformer().toDbString(cproGroup.getGroupInfo().getGenderInfo())));
					}
					
				} catch(Exception e){
				  log.error("failed to contruct opt history content. " + e.getMessage(), e);
				}
				//------- 保存历史操作记录 end ------

				
			} else {
				apiPosition.addParam(GroupConstant.POSITION_STATUS);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.GROUP_CTREATION_ERROR.getValue(),
						GroupErrorCode.GROUP_CTREATION_ERROR.getMessage(),
						apiPosition.getPosition(), 
						GroupErrorCode.STATE_INVALID.getMessage());
				continue;
			}
			
		}
		//记录历史操作
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理

		result.setPayment(pay);

		return result;
	}
	
	public ApiResult<AdditionalGroupType> updateAdditionalGroup(ApiResult<AdditionalGroupType> result, 
			DataPrivilege user, AdditionalGroupType[] groupTypes) {
		
		PaymentResult pay = result.getPayment();

		// 从当前线程session中获取调用者变量
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 准备保存的历史操作记录列表
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		// 遍历前端传递过来的推广组更新配置，逐个更新
		for (int i = 0; i < groupTypes.length; i++) {
			AdditionalGroupType groupType = groupTypes[i];
			
			// 验证前端传递的推广组更新配置中的groupId是否合法
			Integer groupId = Integer.parseInt(String.valueOf(groupType.getGroupId()));
			if (groupType == null || groupId <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUP_INFO, i);
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.GROUP_CTREATION_ERROR.getValue(),
						GroupErrorCode.GROUP_CTREATION_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			// 将前端传递过来的推广组更新配置映射为后端的CproGroup BO类
			Mapper mapper = BeanMapperProxy.getMapper();
			CproGroup group = mapper.map(groupType, CproGroup.class);
			group.setGroupId(groupId);
			group.setUserId(bdUser.getUserid());
			group.setModUserId(visitor.getUserid());
			group.setModTime(new Date());
			CproGroupInfo groupInfo = new CproGroupInfo();
			groupInfo.setPrice(groupType.getPrice());
			groupInfo.setGenderInfo(groupType.getExcludeGender());
			if (groupType.isAllRegion() != null) {
				groupInfo.setIsAllRegion(groupType.isAllRegion() == true ? CproGroupConstant.GROUP_ALLREGION : 0);
			}
			if (groupType.isAllSite() != null) {
				groupInfo.setIsAllSite(groupType.isAllSite() == true ? CproGroupConstant.GROUP_ALLSITE : 0);
			}
			group.setGroupInfo(groupInfo);
			
			//修改各种type仅作验证用
			group.setGroupType(groupType.getType());
			group.setTargetType(groupType.getTargetType());

			// 从数据库查找指定的推广组，如果不存在则报错继续；否则拷贝一个作为历史操作查询用的“修改前”对象
			CproGroup destCproGroup = cproGroupMgr.findWithInfoById(group.getGroupId());
			if (destCproGroup == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUP_INFO, i);
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.NOT_FOUND.getValue(),
						GroupErrorCode.NOT_FOUND.getMessage(), 
						apiPosition.getPosition(),
						GroupErrorCode.NOT_FOUND.getMessage());
				continue;
			}
			CproGroup beforeCproGroup = new CproGroup();
			CproGroupInfo beforeCproGroupInfo = new CproGroupInfo();
			try{
				BeanUtils.copyProperties(beforeCproGroup, destCproGroup);
				BeanUtils.copyProperties(beforeCproGroupInfo, destCproGroup.getGroupInfo());
				
				// Trick here：这里进行一次引用的重新赋值，保证后续历史操作拿到的before是最原始的
				beforeCproGroup.setGroupInfo(beforeCproGroupInfo);
				beforeCproGroupInfo.setGroup(beforeCproGroup);
			} catch(Exception e){
				log.error("failed to copy cproplan. " + e.getMessage(), e);
			}
			
			
			// 设置前端未传递的planId
			Integer planId = destCproGroup.getPlanId();
			group.setPlanId(planId);
			
			// 验证推广组更新参数
			ApiError code = this.validateUpdateAdditionalGroup(visitor, i, group, destCproGroup, planId);
			if (code != null) {
				result = ApiResultBeanUtils.addApiError(result, code);
				continue;
			}
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO, i);
			
			// 修改推广组状态
			if (groupType.getStatus() != null) {
				destCproGroup.setGroupState(groupType.getStatus());
			}
				
			// 更新推广组名称
			if (groupType.getGroupName() != null) {
				destCproGroup.setGroupName(groupType.getGroupName());
			}
			
			// 更新推广组出价
			if (groupType.getPrice() != null && groupType.getPrice() > 0) {
				destCproGroup.getGroupInfo().setPrice(groupType.getPrice());
			}
			
			// 更新展现类型
			if (groupType.getType() != null) {
				destCproGroup.setGroupType(groupType.getType());
			}
			
			// 更新排除性别
			if (groupType.getExcludeGender() != null) {
				// 翻译性别排除为性别正选，体验优化三期，by hejinggen
				//Integer convertedGenderInfo = GenderInfoUtil.convertGenderInfoToLatestStyle(groupType.getExcludeGender());//前端排除翻译为正选
				destCproGroup.getGroupInfo().setGenderInfo(groupType.getExcludeGender());
			}
			
			// 更新是否全地域投放
			if (groupType.isAllRegion() != null) {
				destCproGroup.getGroupInfo().setIsAllRegion(groupType.isAllRegion() == true ? CproGroupConstant.GROUP_ALLREGION : 0);
			}
			
			// 更新是否全网投放
			if (groupType.isAllSite() != null) {
				destCproGroup.getGroupInfo().setIsAllSite(groupType.isAllSite() == true ? CproGroupConstant.GROUP_ALLSITE : 0);
			}
			
			// 更新是否启用兴趣定向
			if (groupType.isItEnabled() != null && !TargettypeUtil.isAtRight(destCproGroup.getTargetType())) {
				Integer beforeTargetType = destCproGroup.getTargetType();
				if (groupType.isItEnabled() == true) {
					int afterTargetType = beforeTargetType | CproGroupConstant.GROUP_TARGET_TYPE_IT;
					destCproGroup.setTargetType(afterTargetType);
				} else {
					int afterTargetType = TargettypeUtil.clearIT(beforeTargetType);
					destCproGroup.setTargetType(afterTargetType);
				}
			}
			
			// 更新标准设置投放的定向方式
			if (groupType.getTargetType() != null) {
				
				Integer beforeTargetType = destCproGroup.getTargetType();
				
				if (!ApiTargetTypeUtil.isValidTargetType(groupType.getTargetType())) {
					apiPosition.addParam(GroupConstant.TARGETTYPE);
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (destCproGroup.getIsSmart() == CproGroupConstant.IS_SMART_TRUE && groupType.getTargetType() == GroupConstant.API_TARGET_TYPE_NONE) {
					apiPosition.addParam(GroupConstant.TARGETTYPE);
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.SMARTIDEA_GROUP_CANNOT_SET_TO_PT_TARGETTYPE.getValue(),
							GroupConfigErrorCode.SMARTIDEA_GROUP_CANNOT_SET_TO_PT_TARGETTYPE.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				// 高级组合投放禁止修改定向方式
				if (TargettypeUtil.isPack(beforeTargetType)) {
					apiPosition.addParam(GroupConstant.TARGETTYPE);
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TARGETTYPE_CANNOT_CHANGE_IF_SET_TO_PACK.getValue(),
							GroupConfigErrorCode.TARGETTYPE_CANNOT_CHANGE_IF_SET_TO_PACK.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (groupType.getTargetType() == GroupConstant.API_TARGET_TYPE_NONE) {
					int afterTargetType = TargettypeUtil.clearRT(beforeTargetType);
					afterTargetType = TargettypeUtil.clearVT(afterTargetType);
					afterTargetType = TargettypeUtil.clearCT(afterTargetType);
					afterTargetType = TargettypeUtil.clearQT(afterTargetType);
					afterTargetType = TargettypeUtil.clearHCT(afterTargetType);
					destCproGroup.setTargetType(afterTargetType);
				} else if (groupType.getTargetType() == GroupConstant.API_TARGET_TYPE_RT) {
					/*
					int afterTargetType = TargettypeUtil.clearVT(beforeTargetType);
					afterTargetType = TargettypeUtil.clearKT(afterTargetType);
					afterTargetType = afterTargetType | CproGroupConstant.GROUP_TARGET_TYPE_RT;
					destCproGroup.setTargetType(afterTargetType);
					*/
					
					/*
					 *  RT(RT2) has been deprecated since cpweb661 in 2013/10/28
					 */
					apiPosition.addParam(GroupConstant.TARGETTYPE);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.RT_DEPREATED_ERROR.getValue(),
							GroupConfigErrorCode.RT_DEPREATED_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				} else if (groupType.getTargetType() == GroupConstant.API_TARGET_TYPE_VT) {
					int afterTargetType = TargettypeUtil.clearRT(beforeTargetType);
					afterTargetType = TargettypeUtil.clearKT(afterTargetType);
					afterTargetType = afterTargetType | CproGroupConstant.GROUP_TARGET_TYPE_VT;
					destCproGroup.setTargetType(afterTargetType);
				} else if (groupType.getTargetType() == GroupConstant.API_TARGET_TYPE_KT) {
					int afterTargetType = TargettypeUtil.clearRT(beforeTargetType);
					afterTargetType = TargettypeUtil.clearVT(afterTargetType);
					afterTargetType = afterTargetType | CproGroupConstant.GROUP_TARGET_TYPE_CT | CproGroupConstant.GROUP_TARGET_TYPE_QT | CproGroupConstant.GROUP_TARGET_TYPE_HCT;
					destCproGroup.setTargetType(afterTargetType);
				} else {
					apiPosition.addParam(GroupConstant.POSITION_KT_TARGETTYPE);
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
			}
			
			// hexiufeng cpweb-696
			// 判断 normal or pause==>deleted,此时需要先设置为删除然后再修改其他字段数据
			// 删除时会自动添加plandelinfo
			if(beforeCproGroup.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE
					&& destCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE){
				List<Integer> groupIdList = new LinkedList<Integer>();
				groupIdList.add(destCproGroup.getGroupId());
				cproGroupMgr.modStatetoDelete(visitor, groupIdList);
			}
			// 判断 deleted==>normal or pause,此时需要先恢复，如果恢复失败则报错
			if(beforeCproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE
					&& destCproGroup.getGroupState() != CproGroupConstant.GROUP_STATE_DELETE){
				List<Integer> groupIdList = new LinkedList<Integer>();
				groupIdList.add(destCproGroup.getGroupId());
				if(cproGroupMgr.findOutOfProtection(groupIdList).size() == 1){
					// 计划已经被删除，而且超出了恢复期，报错
					// 需要客户端来处理
					apiPosition.addParam(GroupConstant.POSITION_STATUS);
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getValue(),
							GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getMessage(),
							apiPosition.getPosition(), 
							GroupErrorCode.GROUP_UPDATE_OUTOFPROTECTION.getMessage());
					continue;
				}
				cproGroupMgr.modStatetoResume(visitor, groupIdList);
			}
			
			// 修改推广组，提交到数据库
			destCproGroup.setModUserId(visitor.getUserid());
			destCproGroup.setModTime(new Date());
			
			
			CproGroup cproGroup = cproGroupMgr.modCproGroup(visitor, destCproGroup);
			if (cproGroup != null) {
				pay.increSuccess();
				AdditionalGroupType data = GroupBoMappingUtil.transGroupBo2AdditionalGroupType(cproGroup);
				ApiResultBeanUtils.addApiResult(result, data);
			} else {
				apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUP_INFO, i);
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.GROUP_UPDATE_ERROR.getValue(),
						GroupErrorCode.GROUP_UPDATE_ERROR.getMessage(),
						apiPosition.getPosition(), 
						GroupErrorCode.GROUP_UPDATE_ERROR.getMessage());
				continue;
			}
			
			//------- 保存历史操作记录 start ------
			try{
				// 保存推广组名称修改
				if(!beforeCproGroup.getGroupName().equals(cproGroup.getGroupName())){
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_NAME;
					optContents.add(new OptContent(cproGroup.getUserId(),
							opType.getOpType(),
							opType.getOpLevel(), 
							Long.valueOf(cproGroup.getGroupId()), 
							opType.getTransformer().toDbString(beforeCproGroup.getGroupName()), 
							opType.getTransformer().toDbString(cproGroup.getGroupName())));
				}
				
				// 保存出价修改
				if(!beforeCproGroupInfo.getPrice().equals(cproGroup.getGroupInfo().getPrice())){
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_PRICE;
					optContents.add(new OptContent(cproGroup.getUserId(),
							opType.getOpType(),
							opType.getOpLevel(), 
							Long.valueOf(cproGroup.getGroupId()), 
							opType.getTransformer().toDbString(beforeCproGroupInfo.getPrice()), 
							opType.getTransformer().toDbString(cproGroup.getGroupInfo().getPrice())));
				}
				
				// 保存推广组状态
				if(!beforeCproGroup.getGroupState().equals(cproGroup.getGroupState())){
					if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE){
						OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_DELETE;		
						optContents.add(new OptContent(cproGroup.getUserId(),
								optype.getOpType(),
								optype.getOpLevel(), 
								cproGroup.getGroupId(), 
								optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
								optype.getTransformer().toDbString(cproGroup.getGroupState())));
					}
					if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_PAUSE){
						OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_PAUSE;		
						optContents.add(new OptContent(cproGroup.getUserId(),
								optype.getOpType(),
								optype.getOpLevel(), 
								cproGroup.getGroupId(), 
								optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
								optype.getTransformer().toDbString(cproGroup.getGroupState())));
					}
					if(cproGroup.getGroupState() == CproGroupConstant.GROUP_STATE_NORMAL){
						OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_RESUME;		
						optContents.add(new OptContent(cproGroup.getUserId(),
								optype.getOpType(),
								optype.getOpLevel(), 
								cproGroup.getGroupId(), 
								optype.getTransformer().toDbString(beforeCproGroup.getGroupState()),
								optype.getTransformer().toDbString(cproGroup.getGroupState())));
					}
				}
				
				// 保存展现类型修改
				if(!Objects.equal(cproGroup.getGroupType(), beforeCproGroup.getGroupType())){
					OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_TYPE;		
					optContents.add(new OptContent(cproGroup.getUserId(),
							optype.getOpType(),
							optype.getOpLevel(), 
							cproGroup.getGroupId(), 
							optype.getTransformer().toDbString(beforeCproGroup.getGroupType()),
							optype.getTransformer().toDbString(cproGroup.getGroupType())));
				}
				
				// 保存排除性别修改
				if(!Objects.equal(cproGroup.getGroupInfo().getGenderInfo(), beforeCproGroup.getGroupInfo().getGenderInfo())){
					OpTypeVo optype = OptHistoryConstant.OPTYPE_GROUP_GENDER_INFO;
					optContents.add(new OptContent(cproGroup.getUserId(),
							optype.getOpType(),
							optype.getOpLevel(), 
							cproGroup.getGroupId(), 
							optype.getTransformer().toDbString(beforeCproGroup.getGroupInfo().getGenderInfo()),
							optype.getTransformer().toDbString(cproGroup.getGroupInfo().getGenderInfo())));
				}
				
				// 保存是否启用全地域
				if (!Objects.equal(cproGroup.getGroupInfo().getIsAllRegion(), beforeCproGroup.getGroupInfo().getIsAllRegion())) {
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_REGION_BATCH;
					OptContent content = new OptContent(group.getUserId(), 
							opType.getOpType(), 
							opType.getOpLevel(), 
							group.getGroupId(), 
							opType.getTransformer().toDbString(new GroupRegionOptVo(beforeCproGroup.getGroupInfo())),  
							opType.getTransformer().toDbString(new GroupRegionOptVo(cproGroup.getGroupInfo())));
					optContents.add(content);
				}
				
				// 保存是否启用全网投放
				if (cproGroup.getGroupInfo().getIsAllRegion() != beforeCproGroup.getGroupInfo().getIsAllRegion()) {
					OpTypeVo opTypeTrade = OptHistoryConstant.OPTYPE_GROUP_TRADE_EDIT;
					OptContent contentTrade = new OptContent(group.getUserId(), 
							opTypeTrade.getOpType(), 
							opTypeTrade.getOpLevel(), 
							group.getGroupId(), 
							opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(beforeCproGroup.getGroupInfo())),  
							opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(cproGroup.getGroupInfo())));
					if (!contentTrade.getPreContent().equals(contentTrade.getPostContent())) {
						optContents.add(contentTrade);
					}
					
					OpTypeVo opTypeSite = OptHistoryConstant.OPTYPE_GROUP_SITE_EDIT;
					OptContent contentSite = new OptContent(group.getUserId(), 
							opTypeSite.getOpType(), 
							opTypeSite.getOpLevel(), 
							group.getGroupId(), 
							opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(beforeCproGroup.getGroupInfo())),  
							opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(cproGroup.getGroupInfo())));
					if (!contentSite.getPreContent().equals(contentSite.getPostContent())) {
						optContents.add(contentSite);
					}
				}
				
				// 保存是否启用兴趣修改
				if ((TargettypeUtil.hasIT(cproGroup.getTargetType()) && !TargettypeUtil.hasIT(beforeCproGroup.getTargetType())) ||
						(!TargettypeUtil.hasIT(cproGroup.getTargetType()) && TargettypeUtil.hasIT(beforeCproGroup.getTargetType()))) {
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_IT_ENABLE;
					OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
							.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
									beforeCproGroup.getTargetType()), opType.getTransformer().toDbString(cproGroup.getTargetType()));
					OperationHistoryUtils.putOperationContent(content);
					optContents.addAll(OperationHistoryUtils.getOptContents());
				}
				
				// 保存是否定向方式修改
				if( (beforeCproGroup.getTargetType() | CproGroupConstant.GROUP_TARGET_TYPE_IT) != 
						(cproGroup.getTargetType() | CproGroupConstant.GROUP_TARGET_TYPE_IT)){
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_XT_CHANGE;
					OptContent content = new OptContent(group.getUserId(),
							opType.getOpType(), opType.getOpLevel(),
							group.getGroupId(),
							opType.getTransformer().toDbString(beforeCproGroup.getTargetType()),
							opType.getTransformer().toDbString(cproGroup.getTargetType()));
					optContents.add(content);
				}
				
			} catch(Exception e){
			  log.error("Failed to contruct opt history content. " + e.getMessage(), e);
			}
			//------- 保存历史操作记录 end ------
			
		}
		//记录历史操作
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理

		result.setPayment(pay);

		return result;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}
	
	
}
