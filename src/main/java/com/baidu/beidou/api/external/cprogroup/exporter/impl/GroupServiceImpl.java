package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupService;
import com.baidu.beidou.api.external.cprogroup.service.ApiGroupService;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByGroupIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupIdByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAdditionalGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateGroupRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.util.SessionHolder;

/**
 * ClassName: GroupServiceImpl
 * Function: 推广组基本设置，包括：新增及修改推广组，获取推广组信息等
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GroupServiceImpl implements GroupService {
	
	private static final Log LOG = LogFactory.getLog(GroupServiceImpl.class);

	private CproGroupMgr cproGroupMgr;

	private ApiGroupService apiGroupService;
	
	// 推广组设置相关阀值限制
	private int addGroupMax;
	private int getGroupByGroupIdMax;
	private int updateGroupMax;
	

	public ApiResult<GroupType> addGroup(DataPrivilege user,
			AddGroupRequest request, ApiOption apiOption) {

		ApiResult<GroupType> result = new ApiResult<GroupType>();

		// ---------------------------参数验证---------------------------------//
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getGroupTypes() == null
				|| request.getGroupTypes().length < 1) {	
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 推广组个数验证
		int groupSum = request.getGroupTypes().length;
		if (groupSum > addGroupMax) {	
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_ADD_GROUP.getValue(),
					GroupErrorCode.TOOMANY_ADD_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		PaymentResult pay = new PaymentResult();
		pay.setTotal(groupSum);
		result.setPayment(pay);
		
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < groupSum; index++) {
			GroupType group = request.getGroupTypes()[index];
			result = apiGroupService.addGroup(result, index, group, optContents);
		}
		//记录历史操作
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}

	public ApiResult<GroupType> getGroupByGroupId(DataPrivilege user,
			GetGroupByGroupIdRequest request, ApiOption apiOption) {
		ApiResult<GroupType> result = new ApiResult<GroupType>();
		PaymentResult pay = new PaymentResult();

		// ---------------------------参数验证---------------------------------//
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getGroupIds() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUPIDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request.getGroupIds().length > getGroupByGroupIdMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUPIDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_QUERY_GROUP.getValue(),
					GroupErrorCode.TOOMANY_QUERY_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		long[] groupIdArray = request.getGroupIds();
		pay.setTotal(groupIdArray.length);
		for (int i = 0; i < groupIdArray.length; i++) {
			if (groupIdArray[i] <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.GROUPIDS, i);
				
				result = ApiResultBeanUtils.addApiError(result, 
						GroupErrorCode.PARAMETER_ERROR.getValue(), 
						GroupErrorCode.PARAMETER_ERROR.getMessage(), 
						apiPosition.getPosition(),
						GroupErrorCode.PARAMETER_ERROR.getMessage());
			} else {
				Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
				CproGroup g = cproGroupMgr.findWithInfoById(groupId);
				if (g == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.GROUPIDS, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(),
							GroupErrorCode.NOT_FOUND.getMessage());
				} else {
					GroupType groupType = new GroupType();
					groupType.setCampaignId(g.getPlanId());
					groupType.setGroupId(g.getGroupId());
					groupType.setGroupName(g.getGroupName());
					groupType.setPrice(g.getGroupInfo().getPrice());
					groupType.setStatus(g.getGroupState());
					groupType.setType(g.getGroupType());
					groupType.setExcludeGender(g.getGroupInfo().getGenderInfo());
					result = ApiResultBeanUtils.addApiResult(result, groupType);
					pay.increSuccess();
				}
			}
		}

		result.setPayment(pay);

		return result;
	}

	public ApiResult<GroupType> getGroupByCampaignId(DataPrivilege user,
			GetGroupByCampaignIdRequest request, ApiOption apiOption) {
		ApiResult<GroupType> result = new ApiResult<GroupType>();
		PaymentResult pay = new PaymentResult();

		// ---------------------------参数验证---------------------------------//
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCampaignId() <= 0) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PLANID);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		Integer id = Integer.parseInt(String.valueOf(request.getCampaignId()));

		// 查询的推广组数量超出限制
		Long groupCnt = cproGroupMgr.countCproGroupByPlanId(id);
		if (groupCnt != null && groupCnt > GroupConstant.MAX_QUERY_GROUP_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_QUERY_GROUP.getValue(),
					GroupErrorCode.TOOMANY_QUERY_GROUP.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		List<CproGroup> groups = this.cproGroupMgr.findWithInfoByPlanId(id, -1);

		if (groups != null) {
			pay.setTotal(groups.size());
			for (CproGroup g : groups) {
				
				//临时过滤掉at左推广组 add by caichao
				if (g.getTargetType() == 256) {
					pay.increSuccess();
					continue;
				}
				GroupType groupType = new GroupType();
				groupType.setCampaignId(id);
				groupType.setGroupId(g.getGroupId());
				groupType.setGroupName(g.getGroupName());
				groupType.setPrice(g.getGroupInfo().getPrice());
				groupType.setStatus(g.getGroupState());
				groupType.setType(g.getGroupType());
				groupType.setExcludeGender(g.getGroupInfo().getGenderInfo());
				result = ApiResultBeanUtils.addApiResult(result, groupType);
				pay.increSuccess();
			}
		}
		result.setPayment(pay);

		return result;
	}

	public ApiResult<Long> getGroupIdByCampaignId(DataPrivilege user,
			GetGroupIdByCampaignIdRequest request, ApiOption apiOption) {
		ApiResult<Long> result = new ApiResult<Long>();
		PaymentResult pay = new PaymentResult();

		// ---------------------------参数验证---------------------------------//
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getCampaignId() <= 0) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PLANID);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		Integer id = Integer.parseInt(String.valueOf(request.getCampaignId()));

		// 查询的推广组数量超出限制
		Long groupCnt = cproGroupMgr.countCproGroupByPlanId(id);
		if (groupCnt != null && groupCnt > GroupConstant.MAX_QUERY_GROUP_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_QUERY_GROUP.getValue(),
					GroupErrorCode.TOOMANY_QUERY_GROUP.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		List<CproGroup> groups = this.cproGroupMgr.findCproGroupByPlanId(id);

		if (groups != null) {
			pay.setTotal(groups.size());
			for (CproGroup g : groups) {
				//临时过滤掉at左推广组 add by caichao
				if (g.getTargetType() == 256) {
					pay.increSuccess();
					continue;
				}
				result = ApiResultBeanUtils.addApiResult(result, 
						Long.valueOf(g.getGroupId()));
				pay.increSuccess();
			}
		}

		result.setPayment(pay);

		return result;
	}

	public ApiResult<GroupType> updateGroup(DataPrivilege user,
			UpdateGroupRequest request, ApiOption apiOption) {

		ApiResult<GroupType> result = new ApiResult<GroupType>();

		// ---------------------------参数验证---------------------------------//
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getGroupTypes() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request.getGroupTypes().length > updateGroupMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_MOD_GROUP.getValue(),
					GroupErrorCode.TOOMANY_MOD_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		GroupType[] groupTypes = request.getGroupTypes();
		for (int i = 0; i < groupTypes.length; i++) {
			Long groupId = groupTypes[i].getGroupId();
			CproGroup g = cproGroupMgr.findWithInfoById(groupId.intValue());
			groupTypes[i].setCampaignId(g.getPlanId());
		}
		
		PaymentResult pay = new PaymentResult();
		pay.setTotal(groupTypes.length);
		result.setPayment(pay);
		
		result = apiGroupService.updateGroup(result, user, groupTypes);

		return result;
	}

	public ApiResult<AdditionalGroupType> updateAdditionalGroup(DataPrivilege user, 
			UpdateAdditionalGroupRequest request, ApiOption apiOption) {
		
		ApiResult<AdditionalGroupType> result = new ApiResult<AdditionalGroupType>();
		
		result = ApiResultBeanUtils.validateUserAndParam(result, user, request);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null || request.getGroupTypes() == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (request.getGroupTypes().length > updateGroupMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.GROUP_INFO);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupErrorCode.TOOMANY_MOD_GROUP.getValue(),
					GroupErrorCode.TOOMANY_MOD_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		AdditionalGroupType[] groupTypes = request.getGroupTypes();
		
		PaymentResult pay = new PaymentResult();
		pay.setTotal(groupTypes.length);
		result.setPayment(pay);
		
		result = apiGroupService.updateAdditionalGroup(result, user, groupTypes);

		return result;

	}
	
	public void setApiGroupService(ApiGroupService apiGroupService) {
		this.apiGroupService = apiGroupService;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public void setAddGroupMax(int addGroupMax) {
		this.addGroupMax = addGroupMax;
	}

	public void setGetGroupByGroupIdMax(int getGroupByGroupIdMax) {
		this.getGroupByGroupIdMax = getGroupByGroupIdMax;
	}

	public void setUpdateGroupMax(int updateGroupMax) {
		this.updateGroupMax = updateGroupMax;
	}
}
