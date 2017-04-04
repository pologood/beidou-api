package com.baidu.beidou.api.external.cprogroup.facade.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.facade.GroupExcludePeopleFacade;
import com.baidu.beidou.api.external.cprogroup.util.APIGroupUtil;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.IndexMapper;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.exception.ExcludeException;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;
import com.baidu.beidou.cprogroup.vo.VtPeopleExcludeMapperVo;
import com.baidu.beidou.cprogroup.vo.VtPeopleVo;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.OptContentProviderUtil;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 
 * ClassName: GroupExcludePeopleFacade  <br>
 * Function: 由于历史原因，排除人群的代码在北斗web中，因此这里存在一个特殊的facade
 *
 * @author zhangxu
 * @date Sep 4, 2012
 */
public class GroupExcludePeopleFacadeImpl implements GroupExcludePeopleFacade {
	
	private static final Log log = LogFactory.getLog(GroupExcludePeopleFacadeImpl.class);

	private VtPeopleMgr vtPeopleMgr;
	
	private CproGroupVTMgr cproGroupVTMgr;
	
	private CproGroupMgr cproGroupMgr;
	
	/**
	 * 保存推广组排除人群设置
	 * @param response
	 * @param groupId
	 * @param excludePids 排除人群id列表
	 * @param indexMapper excludePids在实际前端传递过来的数组里的下标索引，需要提前构造好
	 * @param userId
	 * @param opUser
	 * @param checkLimit 是否验证总数超过限制
	 * @param exitWhenValidationFail 遇到验证错误时是否退出
	 * @param isOverride 是否覆盖原配置信息
	 * @param contents 
	 */
	public void addOrSetExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			IndexMapper indexMapper,
			int userId,
			int opUser,
			boolean checkLimit,
			boolean exitWhenValidationFail,
			boolean isOverride,
			List<OptContent> contents) {
		
		List<Integer> groupIds = APIGroupUtil.trans2GroupIdList(groupId);
		
		// 查询出已关联的人群列表和该用户的所有人群设置
		List<CproGroupVTVo> vtRelationList = cproGroupVTMgr.findVTRelationByGroupNotSpecifyTargettype(groupId, userId);
		List<CproGroupVTVo> vtPeopleList = vtPeopleMgr.getAllVtPeople(userId);
		Set<Long> vtRelationIdSet = APIGroupUtil.transVtVo2LongSet(vtRelationList);
		Set<Long> vtPeopleIdSet = APIGroupUtil.transVtVo2LongSet(vtPeopleList);
		
		// 验证排除数量上限
		if (checkLimit) {
			int peopleExcludeCount = cproGroupVTMgr.countPeopleExclude(userId, groupIds, null);
			boolean isPeopleExcCountExceed = (peopleExcludeCount + excludePids.size()) > CproGroupConstant.GROUP_VT_EXCLUDE_CROWD_MAX_NUM ? true : false;
			if (isPeopleExcCountExceed) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getValue(),
						GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
				return;
			}
		}
		
		// 查询已存在的排除人群
		List<VtPeopleExcludeMapperVo> existExcludePeopleList = cproGroupVTMgr.findPeopleExcludeByCondition(userId, groupIds, null);
		Set<Long> existExcludePidSet = APIGroupUtil.transExcludeVtVo2LongSet(existExcludePeopleList);
		
		Set<Long> toAddPidSet = new HashSet<Long>(); //待添加集合
		Set<Long> noNeedToAddPidSet = new HashSet<Long>(); //不需要动已添加集合
		for (int index = 0; index < excludePids.size(); index++) {
			long pid = excludePids.get(index);
			
			// 对于覆盖操作有错误直接退出
			if (CollectionUtils.isNotEmpty(response.getErrors()) && exitWhenValidationFail) {
				return;
			}
			
			// 是否是重复的人群id
			if (toAddPidSet.contains(pid)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS, index, indexMapper);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_PEOPLE_DUP.getValue(),
						GroupConfigErrorCode.EXCLUDE_PEOPLE_DUP.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 验证人群是否是存在
			if (!vtPeopleIdSet.contains(pid)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS, index, indexMapper);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_PEOPLE_NOT_EXIST.getValue(),
						GroupConfigErrorCode.EXCLUDE_PEOPLE_NOT_EXIST.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 验证人群是否已关联
			if (vtRelationIdSet.contains(pid)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS, index, indexMapper);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_PEOPLE_RELATED.getValue(),
						GroupConfigErrorCode.EXCLUDE_PEOPLE_RELATED.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 不存在时才加入待添加集合
			if (!existExcludePidSet.contains(pid)) {
				toAddPidSet.add(pid);
			} else {
				noNeedToAddPidSet.add(pid);
			}
		}
		
		// 对于覆盖操作有错误直接退出
		if (CollectionUtils.isNotEmpty(response.getErrors()) && exitWhenValidationFail) {
			return;
		}
		
		// 根据推广组ID取出PlanId
		Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
		if (planId == null) {
			throw new RuntimeException("Cannot get planId from groupId=" + groupId);
		}
		
		// 构造待添加人群，并保存
		List<Long> toAddPeopleIds = new ArrayList<Long>(toAddPidSet);
		StringBuffer logMsg = new StringBuffer();
		logMsg.append("## Save exclude people, groupId=");
		logMsg.append(groupId);
		logMsg.append(", toAddNum=");
		logMsg.append(toAddPeopleIds.size());
		
		try {
			cproGroupVTMgr.addPeopleExclude(userId, opUser, planId, groupId, toAddPeopleIds);
		} catch (ExcludeException e) {
			log.error("Internal error " + e.getMessage(), e);
		}
		
		// 根据关联排除人群与不需要添加的人群做余集来求出待删除的人群集合，并保存
		List<Long> toDelPeopleIds = null;
		if (isOverride) {
			existExcludePidSet.removeAll(noNeedToAddPidSet);
			toDelPeopleIds = new ArrayList<Long>(existExcludePidSet);
			cproGroupVTMgr.delByPids(userId, toDelPeopleIds);
			logMsg.append(", toDelNum=");
			logMsg.append(toDelPeopleIds.size());
		}
		
		log.info(logMsg);
		
		//添加历史操作记录
		this.addVtPeopleHistory(userId, groupId, toAddPeopleIds, toDelPeopleIds, contents);
		
	}

	/**
	 * 删除推广组排除人群设置
	 * @param response
	 * @param groupId
	 * @param excludePids 排除人群id列表
	 * @param indexMapper excludePids在实际前端传递过来的数组里的下标索引，需要提前构造好
	 * @param userId
	 * @param opUser
	 * @param contents
	 */
	public void deleteExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			IndexMapper indexMapper,
			int userId,
			int opUser,
			List<OptContent> contents) {
		
		List<Integer> groupIds = APIGroupUtil.trans2GroupIdList(groupId);
		
		// 查询出已排除的人群
		List<VtPeopleExcludeMapperVo> voList = cproGroupVTMgr.findPeopleExcludeByCondition(userId, groupIds, null);
		
		// 构造已排除人群ID
		Set<Long> existExcludePeopleIdSet = APIGroupUtil.transExcludeVtVo2LongSet(voList);
		
		Set<Long> toDelPidSet = new HashSet<Long>(); //待添加集合
		for (int index = 0; index < excludePids.size(); index++) {
			long pid = excludePids.get(index);
			
			// 是否是重复的人群id
			if (toDelPidSet.contains(pid)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS, index, indexMapper);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_PEOPLE_DUP.getValue(),
						GroupConfigErrorCode.EXCLUDE_PEOPLE_DUP.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 验证人群是否是存在与已排除列表中
			if (!existExcludePeopleIdSet.contains(pid)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS, index, indexMapper);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.EXCLUDE_PEOPLE_NOT_EXIST.getValue(),
						GroupConfigErrorCode.EXCLUDE_PEOPLE_NOT_EXIST.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			toDelPidSet.add(pid);
		}
		
		// 根据推广组ID取出PlanId
		Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
		if (planId == null) {
			throw new RuntimeException("Cannot get planId from groupId=" + groupId);
		}
		
		// 构造待删除人群，并保存
		List<Long> toDelPeopleIds = new ArrayList<Long>(toDelPidSet);
		StringBuffer logMsg = new StringBuffer();
		logMsg.append("## Delete exclude people, groupId=");
		logMsg.append(groupId);
		logMsg.append(", toDelNum=");
		logMsg.append(toDelPeopleIds.size());
		
		cproGroupVTMgr.delByPidsAndGroupId(userId, toDelPeopleIds, groupId);
		
		log.info(logMsg);
		
		//添加历史操作记录
		this.addVtPeopleHistory(userId, groupId, null, toDelPeopleIds, contents);
		
	}
	
	private void addVtPeopleHistory(Integer userId, Integer groupId, List<Long> toAddPeopleIds, List<Long> toDelPeopleIds, List<OptContent> contents) {
		List<VtPeopleVo> vtPeople4Add = CollectionUtils.isEmpty(toAddPeopleIds) ? new ArrayList<VtPeopleVo>() : vtPeopleMgr.findVtPeopleList(userId, toAddPeopleIds);
		List<VtPeopleVo> vtPeople4Del = CollectionUtils.isEmpty(toDelPeopleIds) ? new ArrayList<VtPeopleVo>() : vtPeopleMgr.findVtPeopleList(userId, toDelPeopleIds);
		List<String> addBeforePeople = OptContentProviderUtil.transList(vtPeople4Add);
		List<String> delAfterPeople = OptContentProviderUtil.transList(vtPeople4Del);
		OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_EXCLUDE_PEOPLE;
		OptContent content = new OptContent(
				userId,
				opType.getOpType(), 
				opType.getOpLevel(),
				groupId,
				opType.getTransformer().toDbString(addBeforePeople),
				opType.getTransformer().toDbString(delAfterPeople));
		contents.add(content);
	}
	
	public VtPeopleMgr getVtPeopleMgr() {
		return vtPeopleMgr;
	}

	public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
		this.vtPeopleMgr = vtPeopleMgr;
	}

	public CproGroupVTMgr getCproGroupVTMgr() {
		return cproGroupVTMgr;
	}

	public void setCproGroupVTMgr(CproGroupVTMgr cproGroupVTMgr) {
		this.cproGroupVTMgr = cproGroupVTMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}
	
}
