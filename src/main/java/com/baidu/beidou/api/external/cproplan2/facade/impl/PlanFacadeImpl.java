package com.baidu.beidou.api.external.cproplan2.facade.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cproplan2.constant.PlanConstant;
import com.baidu.beidou.api.external.cproplan2.error.PlanCreationException;
import com.baidu.beidou.api.external.cproplan2.error.PlanErrorCode;
import com.baidu.beidou.api.external.cproplan2.facade.PlanFacade;
import com.baidu.beidou.api.external.cproplan2.service.ApiPlanService;
import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan2.vo.ScheduleType;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.CproPlanScheduleUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cproplan.util.CproPlanDeviceOSUtil;
import com.baidu.beidou.cproplan.vo.PlanEnddateOptVo;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserInfoMgr;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.BinaryUtils;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.StringUtils;
import com.google.common.base.Objects;

/**
 * ClassName: PlanFacadeImpl
 * Function: 推广计划层级添加和修改实现
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class PlanFacadeImpl implements PlanFacade {
	private CproPlanMgr planMgr;
	private ApiPlanService apiPlanService;
	private UserInfoMgr userInfoMgr;

	private final static Log LOG = LogFactory.getLog(PlanFacadeImpl.class);

	public ApiError updatePlan(final DataPrivilege user,
			final CampaignType plan, int index, List<OptContent> optContents) {
		if (plan == null) {
			return null;
		}
		CproPlan destPlan = planMgr
				.findCproPlanById((int) plan.getCampaignId());
		CproPlan beforePlan = new CproPlan();
		try{
			BeanUtils.copyProperties(beforePlan, destPlan);
		} catch(Exception e){
			LOG.error("failed to copy cproplan. " + e.getMessage(), e);
		}
		if (destPlan == null) {
			return null;
		}
		
		ApiError error = validateUpdatePlan(user.getDataUser(), plan, destPlan, index);
		if (error != null) {
			return error;
		}
		
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// hexiufeng cpweb-696
		// 判断 normal or pause==>deleted,此时需要先设置为删除然后再修改其他字段数据
		// 删除时会自动添加plandelinfo
		if(beforePlan.getPlanState() != CproPlanConstant.PLAN_STATE_DELETE
				&& destPlan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE){
			List<Integer> planIdList = new LinkedList<Integer>();
			planIdList.add(destPlan.getPlanId());
			planMgr.modStatetoDelete(visitor, planIdList);
		}
		// 判断 deleted==>normal or pause,此时需要先恢复，如果恢复失败则报错
		if(beforePlan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE
				&& destPlan.getPlanState() != CproPlanConstant.PLAN_STATE_DELETE){
			List<Integer> planIdList = new LinkedList<Integer>();
			planIdList.add(destPlan.getPlanId());
			if(1 == planMgr.findOutOfProtection(planIdList).size()){
				// 由于hibernate的自动提交问题，只要修改了处于持久化状态的对象，即使没有调用update方法，
				// 在数据库事务提交时也会被自动提交，这儿需要修改把计划状态再修改回删除状态
				destPlan.setPlanState(CproPlanConstant.PLAN_STATE_DELETE);
				// 计划已经被删除，而且超出了恢复期，报错
				// 需要客户端来处理
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
				return new ApiError(PlanErrorCode.PLAN_UPDATE_OUTOFPROTECTION.getValue(),
						PlanErrorCode.PLAN_UPDATE_OUTOFPROTECTION.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			planMgr.modStatetoResume(visitor, planIdList);
		}
		
		destPlan = planMgr.modCproPlan(visitor, destPlan);
		
		plan.setBudget(destPlan.getBudget());
		plan.setStatus(destPlan.getPlanState());
		plan.setWirelessBidRatio(destPlan.getWirelessBidRatio());
		
		//------- 保存历史操作记录 start ------
		try{
			if(!beforePlan.getPlanName().equals(destPlan.getPlanName())){
				OpTypeVo opType = OptHistoryConstant.OPTYPE_PLAN_NAME;
				optContents.add(new OptContent(destPlan.getUserId(),
						opType.getOpType(),
						opType.getOpLevel(), 
						Long.valueOf(destPlan.getPlanId()), 
						opType.getTransformer().toDbString(beforePlan.getPlanName()), 
						opType.getTransformer().toDbString(destPlan.getPlanName())));
			}
			
			if(!beforePlan.getBudget().equals(destPlan.getBudget())){
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_BUDGET;	
				optContents.add(new OptContent(destPlan.getUserId(),
						optype.getOpType(),
						optype.getOpLevel(), 
						destPlan.getPlanId(), 
						optype.getTransformer().toDbString(beforePlan.getBudget()),
						optype.getTransformer().toDbString(destPlan.getBudget())));
			}	
			
			String oldStartDate = new String(beforePlan.getStartDate());
			String newStartDate = new String(destPlan.getStartDate());
			if(DateUtils.getBetweenDate(DateUtils.strToDate(oldStartDate), DateUtils.strToDate(newStartDate))!= 0){//推广开始时间有调整
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_STARTTIME;
				optContents.add(new OptContent(destPlan.getUserId(),
						optype.getOpType(),
						optype.getOpLevel(),
						destPlan.getPlanId(),
						optype.getTransformer().toDbString(oldStartDate),
						optype.getTransformer().toDbString(newStartDate)));
			}
			
			Integer oldHasEndDate = beforePlan.getHasEndDate();
			Integer hasEndDate = destPlan.getHasEndDate();
			if(( (destPlan.getEndDate() != null) && (! destPlan.getEndDate().equals(beforePlan.getEndDate()))) || (!hasEndDate.equals(oldHasEndDate)) ){//推广结束时间有调整
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_ENDTIME;
				PlanEnddateOptVo oldObj = new PlanEnddateOptVo();
				oldObj.setEndDate(beforePlan.getEndDate());
				oldObj.setHasEndDate(oldHasEndDate);
				PlanEnddateOptVo newObj = new PlanEnddateOptVo();
				newObj.setEndDate(destPlan.getEndDate());
				newObj.setHasEndDate(destPlan.getHasEndDate());
				optContents.add(new OptContent(destPlan.getUserId(),
						optype.getOpType(),
						optype.getOpLevel(),
						destPlan.getPlanId(),
						optype.getTransformer().toDbString(oldObj),
						optype.getTransformer().toDbString(newObj)));
			}
			
			//投放日程
			String oldScheme = CproPlanScheduleUtils.formateScheme(beforePlan);
			String newScheme = CproPlanScheduleUtils.formateScheme(destPlan);
			if(!oldScheme.equals(newScheme)){//推广计划日程有调整
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_SCHEMA;
				optContents.add(new OptContent(destPlan.getUserId(),
						optype.getOpType(),
						optype.getOpLevel(),
						destPlan.getPlanId(),
						optype.getTransformer().toDbString(oldScheme),
						optype.getTransformer().toDbString(newScheme)));
			}
			
			if(!destPlan.getPlanState().equals(beforePlan.getPlanState())){
				if(destPlan.getPlanState() == CproPlanConstant.PLAN_STATE_PAUSE){
					OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_PAUSE;		
					optContents.add(new OptContent(destPlan.getUserId(),
							optype.getOpType(),
							optype.getOpLevel(), 
							destPlan.getPlanId(), 
							optype.getTransformer().toDbString(beforePlan.getPlanState()),
							optype.getTransformer().toDbString(destPlan.getPlanState())));
				}
				if(destPlan.getPlanState() == CproPlanConstant.PLAN_STATE_NORMAL){
					OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_RESUME;		
					optContents.add(new OptContent(destPlan.getUserId(),
							optype.getOpType(),
							optype.getOpLevel(), 
							destPlan.getPlanId(), 
							optype.getTransformer().toDbString(beforePlan.getPlanState()),
							optype.getTransformer().toDbString(destPlan.getPlanState())));
				}
				if(destPlan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE){
					OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_DELETE;		
					optContents.add(new OptContent(destPlan.getUserId(),
							optype.getOpType(),
							optype.getOpLevel(), 
							destPlan.getPlanId(), 
							optype.getTransformer().toDbString(beforePlan.getPlanState()),
							optype.getTransformer().toDbString(destPlan.getPlanState())));
				}
			}
			
			if (!Objects.equal(destPlan.getWirelessBidRatio() , beforePlan.getWirelessBidRatio())) {
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_WIRELESS_BID_RATIO;
				optContents.add(new OptContent(destPlan.getUserId(),
						optype.getOpType(),
						optype.getOpLevel(), 
						destPlan.getPlanId(), 
						optype.getTransformer().toDbString(beforePlan.getWirelessBidRatio()),
						optype.getTransformer().toDbString(destPlan.getWirelessBidRatio())));
			}
			
			if (destPlan.getDeviceOperaSystem() != beforePlan.getDeviceOperaSystem()) {
				// 获得到“待新增”和“待删除”设备和操作系统的列表
				List<String> toAddNameList = new ArrayList<String>();
				List<String> toDeleteNameList = new ArrayList<String>();
				
				CproPlanDeviceOSUtil.getDiffDeviceOsNames(toAddNameList, toDeleteNameList, 
						destPlan.getDeviceOperaSystem(), beforePlan.getDeviceOperaSystem());

				OpTypeVo opType = OptHistoryConstant.OPTYPE_PLAN_DEVICE_OS;
				OptContent content = new OptContent(destPlan.getUserId(), opType.getOpType(), opType.getOpLevel(), destPlan.getPlanId(), 
						opType.getTransformer().toDbString(toDeleteNameList), opType.getTransformer().toDbString(toAddNameList));
				optContents.add(content);
			}
			
		} catch(Exception e){
			LOG.error("failed to contruct opt history content. " + e.getMessage(), e);
		}
		//------- 保存历史操作记录 end ------
		
		return null;
	}

	public ApiResult<CampaignType> addPlan(ApiResult<CampaignType> result,
			DataPrivilege user, CampaignType plan, int index, List<OptContent> optContents) {
		if (user == null || plan == null || index < 0) {
			ApiError error = new ApiError(PlanErrorCode.PLAN_CREATE_FAILED.getValue(), 
					PlanErrorCode.PLAN_CREATE_FAILED.getMessage(),
					PositionConstant.SYS, null);
			ApiResultBeanUtils.addApiError(result, error);
			return result;
		}

		// 验证要新增的推广计划
		ApiError error = validateAddPlan(user.getDataUser(), plan, index);
		if (error != null) {
			// 推广计划验证不通过
			ApiResultBeanUtils.addApiError(result, error);
			return result;
		}

		// 设置新增的推广计划的信息
		CproPlan destPlan = BeanMapperProxy.getMapper().map(plan, CproPlan.class);

		destPlan.setPlanId(null); // 必须有，表示为新增的推广计划
		destPlan.setConsumeType(CproPlanConstant.PLAN_CONSUMETYPE_CPC);
		destPlan.setUserId(user.getDataUser());
		destPlan.setBudgetOver(0);
		destPlan.setHasEndDate(destPlan.getEndDate() == null ? 0 : 1);
		// 设置投放日程
		if (plan.getSchedule() == null || plan.getSchedule().length == 0) {
			// 全日程投放
			destPlan.setSundayScheme(PlanConstant.FULL_SCHEDULE); // '<<'优先级低于'-');
			destPlan.setMondayScheme(PlanConstant.FULL_SCHEDULE);
			destPlan.setTuesdayScheme(PlanConstant.FULL_SCHEDULE);
			destPlan.setWednesdayScheme(PlanConstant.FULL_SCHEDULE);
			destPlan.setThursdayScheme(PlanConstant.FULL_SCHEDULE);
			destPlan.setFridayScheme(PlanConstant.FULL_SCHEDULE);
			destPlan.setSaturdayScheme(PlanConstant.FULL_SCHEDULE);
		} else {
			int[] scheduleArray = unitSchedule(plan.getSchedule());
			destPlan.setMondayScheme(scheduleArray[0]);
			destPlan.setTuesdayScheme(scheduleArray[1]);
			destPlan.setWednesdayScheme(scheduleArray[2]);
			destPlan.setThursdayScheme(scheduleArray[3]);
			destPlan.setFridayScheme(scheduleArray[4]);
			destPlan.setSaturdayScheme(scheduleArray[5]);
			destPlan.setSundayScheme(scheduleArray[6]);
		}
		destPlan.setAddTime(new Date());
		destPlan.setModTime(new Date());
		destPlan.setAddUserId(user.getOpUser());
		destPlan.setModUserId(user.getOpUser());

		// 设置移动设备、操作系统值
		if (plan.getIsDeviceEnabled() == null) {
			plan.setIsDeviceEnabled(false);
		}
		if (plan.getIsOsEnabled() == null) {
			plan.setIsOsEnabled(false);
		}
		int isAllDevice = plan.getIsDeviceEnabled() == false ? PlanConstant.ALL_DEVICE : 0;
		Long deviceId = new Long(plan.getDevice());
		int isAllOs = plan.getIsOsEnabled() == false ? PlanConstant.ALL_OS : 0;
		List<Long> osList = new ArrayList<Long>();
		if (plan.getOs() != null) {
			for (Long osId : plan.getOs()) {
				osList.add(osId);
			}
		}
		long deviceOperaSystem = CproPlanDeviceOSUtil.getDeviceOSDBValue(isAllDevice, deviceId, isAllOs, osList);
		destPlan.setDeviceOperaSystem(deviceOperaSystem);
		
		// 添加推广计划
		try {
			result = apiPlanService.addPlan(result, destPlan, index);
			
			// 添加成功后，返回CampaignType，成功数+1
			plan.setCampaignId(destPlan.getPlanId());			
			ApiResultBeanUtils.addApiResult(result, plan);
			
			PaymentResult pay = result.getPayment();
			pay.increSuccess();
			
			//------- 保存历史操作记录 start ------
			try{
				OpTypeVo optype = OptHistoryConstant.OPTYPE_PLAN_NEW;
				User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
				optContents.add(new OptContent(bdUser.getUserid(),
						optype.getOpType(),
						optype.getOpLevel(), 
						plan.getCampaignId(), 
						null,
						optype.getTransformer().toDbString(plan.getCampaignName())));
			} catch(Exception e){
				LOG.error("failed to contruct opt history content. " + e.getMessage(), e);
			}
			//------- 保存历史操作记录 end ------
			
		} catch (PlanCreationException e) {
			LOG.warn("add cproplan fail for campaignType=[" + plan + "]");
		}
		return result;
	}

	public ApiError validateAddPlan(int userid, CampaignType plan, int index) {
		ApiError error = null;
		
		if (plan == null) {
			error = new ApiError(PlanErrorCode.PLAN_CREATE_FAILED.getValue(),
					PlanErrorCode.PLAN_CREATE_FAILED.getMessage(),
					PositionConstant.SYS, null);
			return error;
		}

		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
		
		if (org.apache.commons.lang.StringUtils.isEmpty(plan.getCampaignName())) {
			apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
			
			return new ApiError(PlanErrorCode.PLAN_NAME_EMPTY.getValue(),
					PlanErrorCode.PLAN_NAME_EMPTY.getMessage(), 
					apiPosition.getPosition(), null);
		} else {
			// 推广计划名称长度
			// ^[\w\-\u4e00-\u9fa5]+$
			if (StringUtils.validateHasSpecialChar(plan.getCampaignName())) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.PLAN_NAME_SPECIAL.getValue(),
						PlanErrorCode.PLAN_NAME_SPECIAL.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			if (plan.getCampaignName() == null
					|| !StringUtils.validBeidouGbkStr(plan.getCampaignName(),
							true, 1, PlanConstant.LENGTH_PLAN_NAME)) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.LENGTH_PLAN_NAME.getValue(),
						PlanErrorCode.LENGTH_PLAN_NAME.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			// 推广计划名称重复
			if (planMgr.hasRepeateName(userid, plan.getCampaignName())) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.REPEAT_PLAN_NAME.getValue(),
						PlanErrorCode.REPEAT_PLAN_NAME.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
		}

		// 10<预算<=10000
		error = validBudget(0, plan.getBudget(), index);
		if (error != null) {
			return error;
		}

		/*
		 * //4. 推广计划个数未达到上限：100 long curCount =
		 * planMgr.countCproPlanByUserId(userid);
		 * if(curCount>=CproPlanConstant.PLAN_ALL_MAX_NUM){ return new
		 * ApiError(PlanErrorCode.MAX_PLAN_NUM.getValue(),
		 * PlanErrorCode.MAX_PLAN_NUM.getMessage(), positionPre, null); }
		 */

		// 5. 非删除的推广计划个数未达到上限：30
		long curCount = planMgr.countEffectiveCproPlanByUserId(userid);
		if (curCount >= CproPlanConstant.PLAN_ALL_MAX_NUM) {
			return new ApiError(
					PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getValue(),
					PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getMessage(),
					apiPosition.getPosition(), null);
		}

		// 7. 推广计划状态为生效或者搁置
		if (plan.getStatus() == null) {
			plan.setStatus(CproPlanConstant.PLAN_STATE_NORMAL);
		}
		if (plan.getStatus() != CproPlanConstant.PLAN_STATE_NORMAL
				&& plan.getStatus() != CproPlanConstant.PLAN_STATE_PAUSE) {
			apiPosition.addParam(PlanConstant.POSITION_STATUS);
			
			return new ApiError(PlanErrorCode.WRONG_PLAN_STATUS.getValue(),
					PlanErrorCode.WRONG_PLAN_STATUS.getMessage(), 
					apiPosition.getPosition(), null);
		}

		// 11. 开始时间>=今日
		// 12. 结束时间>=开始时间
		Date currDate = DateUtils.getRoundedDayCurDate();
		if (plan.getStartDate() == null) {
			plan.setStartDate(currDate);
		} else {
			// 由于本身就是Date对象，所以不需要对其进行验证了
			if (currDate.after(plan.getStartDate())) {
				apiPosition.addParam(PlanConstant.POSITION_START_DATE);
				
				return new ApiError(PlanErrorCode.WRONG_PLAN_STARTDATE_BEFORE.getValue(), 
						PlanErrorCode.WRONG_PLAN_STARTDATE_BEFORE.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		// 如果设置的结束时间为19700101，则表示不设置结束时间。
		if (plan.getEndDate() != null
				&& plan.getStartDate().compareTo(plan.getEndDate()) > 0) {
			Date anotherDate = null;
			try {
				anotherDate = DateUtils.strToDate("19700101");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (plan.getEndDate().compareTo(anotherDate) == 0) {
				plan.setEndDate(null);
			} else {
				apiPosition.addParam(PlanConstant.POSITION_END_DATE);
				
				return new ApiError(PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE_START.getValue(), 
						PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE_START.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		// 根据开始时间设置状态，开始时间必须大于等于当前时间
		if (plan.getStatus() == CproPlanConstant.PLAN_STATE_NORMAL
				&& DateUtils.getRoundedDay(plan.getStartDate()).compareTo(currDate) > 0) {
			plan.setStatus(CproPlanConstant.PLAN_STATE_NOTBEGIN);
		}

		// 新建时如果Schedule不为空，则对其正确性进行验证
		ScheduleType[] schedule = plan.getSchedule();
		if (schedule != null) {
			// 如果输入为schedule=new ScheduleType[1]并且schedule[0].getWeekDay()==0，则表示全日程投放
			if (schedule.length == 1 && schedule[0] != null
					&& schedule[0].getWeekDay() == 0) {
				plan.setSchedule(new ScheduleType[0]);
				schedule = plan.getSchedule();
			}
			
			int result = validateSchedule(schedule);
			if (result > -1) {
				apiPosition.addParam(PlanConstant.POSITION_SCHEDULE, result);
				
				return new ApiError(PlanErrorCode.INVALID_SCHEDULE.getValue(),
						PlanErrorCode.INVALID_SCHEDULE.getMessage(),
						apiPosition.getPosition(), null);
			}
			
			int[] scheduleArray = unitSchedule(schedule);
			int total = 0;
			for (int i = 0; i < scheduleArray.length; i++) {
				total += scheduleArray[i];
			}
			if (total == 0) {
				apiPosition.addParam(PlanConstant.POSITION_SCHEDULE);
				
				return new ApiError(PlanErrorCode.INVALID_SCHEDULE.getValue(),
						PlanErrorCode.INVALID_SCHEDULE.getMessage(),
						apiPosition.getPosition(), null);
			}
		}
		
		// 验证推广类型以及移动设备出价
		if (plan.getWirelessBidRatio() == null) {
			plan.setWirelessBidRatio(CproPlanConstant.WIRELESSBIDRATIO_DEFAULT);
		}
		ApiError typeAndWirelessRatioError = validateTypeAndWirelessRatio(userid, plan.getType(), plan.getWirelessBidRatio(), index, true, true);
		if (typeAndWirelessRatioError != null) {
			return typeAndWirelessRatioError;
		}
		
		// 验证移动设备与操作系统
		ApiError deviceAndOsError = validateDeviceAndOs(userid, plan, index);
		if (deviceAndOsError != null) {
			return deviceAndOsError;
		}

		return null; // 表示验证通过
	}
	
	/**
	 * 
	 * validBudget: 验证预算是否合法
	 * 
	 * @param oldBudget 老的预算值，如果为新增创意，则为0
	 * @param budget 设置的新的预算值
	 * @param index 错误位置index
	 * @return null表示没有错误；
	 * @since
	 */
	private ApiError validBudget(int oldBudget, int budget, int index) {
		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
		apiPosition.addParam(PlanConstant.POSITION_BUDGET);
		
		if (oldBudget > 0 && oldBudget < CproPlanConstant.PLAN_BUDGET_MIN_VALUE) {
			if (!(budget >= CproPlanConstant.PLAN_BUDGET_OLD_MIN_VALUE && budget <= CproPlanConstant.PLAN_BUDGET_MAX_VALUE)) {

				// 如果预算越界，则返回错误提示: 对不起，当前推广组的每日预算需为大于1的整数，且不可超过10万
				return new ApiError(PlanErrorCode.MAX_PLAN_BUDGET.getValue(),
						PlanErrorCode.MAX_PLAN_BUDGET.getMessage(),
						apiPosition.getPosition(), null);
			}

			// 如果原来预算是大于等于10元的，则只允许其在[10,1w]的范围内修改
		} else {

			if (!(budget >= CproPlanConstant.PLAN_BUDGET_MIN_VALUE && budget <= CproPlanConstant.PLAN_BUDGET_MAX_VALUE)) {

				// 如果预算越界，则返回错误提示: 对不起，每日预算需为大于10的整数，且不可超过10万
				return new ApiError(PlanErrorCode.MAX_PLAN_BUDGET.getValue(),
						PlanErrorCode.MAX_PLAN_BUDGET.getMessage(),
						apiPosition.getPosition(), null);
			}
		}
		return null;
	}
	
	/**
	 * validateSchedule: 验证投放日程是否合法
	 * @version PlanFacadeImpl
	 * @author genglei01
	 * @date 2012-2-9
	 * @return index
	 * 		index=-1：正常；index>=0：表示在ScheduleType[]数组中位于index的日程不合法
	 */
	private int validateSchedule(ScheduleType[] scheduleArray) {
		if (scheduleArray == null || scheduleArray.length == 0) {
			return -1;
		}

		for (int index = 0; index < scheduleArray.length; index++) {
			ScheduleType sc = scheduleArray[index];
			if (sc == null) {
				return index;
			}
			// 对Schedule中的值进行合法性判断
			if (sc.getWeekDay() > 7 || sc.getWeekDay() <= 0
					|| sc.getStartTime() < 0 || sc.getStartTime() > 23
					|| sc.getEndTime() <= 0 || sc.getEndTime() > 24
					|| sc.getStartTime() > sc.getEndTime()) {
				return index;
			}
		}

		return -1;
	}
	
	/**
	 * 
	 * 验证推广类型以及移动设备出价
	 *
	 * @param userid
	 * @param promotionType
	 * @param wirelessBidRatio
	 * @param index
	 * @param isValidatePromotionType 是否验证更新类型
	 * @param isValidateWirelessBidRatio 是否验证更新移动设备出价比例
	 * @return ApiError
	 */
	private ApiError validateTypeAndWirelessRatio(int userid, int promotionType, int wirelessBidRatio, int index, boolean isValidatePromotionType, boolean isValidateWirelessBidRatio) {
		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
		
		// 验证推广计划类型
		if (isValidatePromotionType) {
			if (promotionType != CproPlanConstant.PROMOTIONTYPE_ALL && promotionType != CproPlanConstant.PROMOTIONTYPE_WIRELESS) {
				apiPosition.addParam(PlanConstant.POSITION_TYPE);
				
				return new ApiError(PlanErrorCode.INVALID_TYPE.getValue(),
						PlanErrorCode.INVALID_TYPE.getMessage(),
						apiPosition.getPosition(), null);
			}
		}
		
		// 需要检查移动设备出价比例
		if (isValidateWirelessBidRatio) {
			if (!CproPlanConstant.isWirelessBidRatioVaild(wirelessBidRatio)) {
				apiPosition.addParam(PlanConstant.POSITION_WIRELESSBIDRATIO);
				
				return new ApiError(PlanErrorCode.INVALID_WIRELESSRATIO.getValue(),
						PlanErrorCode.INVALID_WIRELESSRATIO.getMessage(),
						apiPosition.getPosition(), null);
			}
			
			if (promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS
					&& wirelessBidRatio != CproPlanConstant.WIRELESSBIDRATIO_DEFAULT) {
				apiPosition.addParam(PlanConstant.POSITION_WIRELESSBIDRATIO);
				
				return new ApiError(PlanErrorCode.INVALID_WIRELESSRATIO.getValue(),
						PlanErrorCode.INVALID_WIRELESSRATIO.getMessage(),
						apiPosition.getPosition(), null);
			}
		}
		
		return null;
	}
	
	private ApiError validateDeviceAndOs(int userid, CampaignType plan, int index) {
		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
		
		Boolean isDeviceEnabled = plan.getIsDeviceEnabled();
		Boolean isOsEnabled = plan.getIsOsEnabled();
		
		if (isDeviceEnabled == null) {
			isDeviceEnabled = false;
		}
		if (isOsEnabled == null) {
			isOsEnabled = false;
		}
				
		int isAllDevice = isDeviceEnabled == false ? PlanConstant.ALL_DEVICE : 0;
		Long deviceId = new Long(plan.getDevice());
		int isAllOs = isOsEnabled == false ? PlanConstant.ALL_OS : 0;
		List<Long> osList = CollectionsUtil.tranformLongArrayToLongList(plan.getOs());
		
		if (isDeviceEnabled == true && !CproPlanDeviceOSUtil.isDeviceIdsLegal(BinaryUtils.long2BinaryLongList(plan.getDevice()))) {
			apiPosition.addParam(PlanConstant.POSITION_DEVICE);
			return new ApiError(PlanErrorCode.INVALID_WIRELESS_DEVICE.getValue(),
					PlanErrorCode.INVALID_WIRELESS_DEVICE.getMessage(),
					apiPosition.getPosition(), null);
		}
		
		if (isOsEnabled == true && !CproPlanDeviceOSUtil.isOSIdsLegal(osList)) {
			apiPosition.addParam(PlanConstant.POSITION_OS);
			return new ApiError(PlanErrorCode.INVALID_WIRELESS_OS.getValue(),
					PlanErrorCode.INVALID_WIRELESS_OS.getMessage(),
					apiPosition.getPosition(), null);
		}
		
		if (!CproPlanDeviceOSUtil.isDeviceOSLegal(isAllDevice, deviceId, isAllOs, osList)) {
			apiPosition.addParam(PlanConstant.POSITION_OS);
			return new ApiError(PlanErrorCode.INVALID_WIRELESS_DEVICE_MAPPING.getValue(),
					PlanErrorCode.INVALID_WIRELESS_DEVICE_MAPPING.getMessage(),
					apiPosition.getPosition(), null);
		}
		
		return null;
	}

	private ApiError validateUpdatePlan(int userid, CampaignType plan,
			CproPlan dest, int index) {
		if (plan == null || dest == null) {
			return null;
		}

		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
		
		if (!StringUtils.isEmpty(plan.getCampaignName())) {
			// 推广计划名称包含特殊字符
			if (StringUtils.validateHasSpecialChar(plan.getCampaignName())) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.PLAN_NAME_SPECIAL.getValue(),
						PlanErrorCode.PLAN_NAME_SPECIAL.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			// 推广计划名称长度
			if (!StringUtils.validBeidouGbkStr(plan.getCampaignName(), true, 1,
					PlanConstant.LENGTH_PLAN_NAME)) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.LENGTH_PLAN_NAME.getValue(),
						PlanErrorCode.LENGTH_PLAN_NAME.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			// 推广计划名称重复
			if (planMgr.hasRepeateNameExcludeSelf(userid, (int) plan
					.getCampaignId(), plan.getCampaignName())) {
				apiPosition.addParam(PlanConstant.POSITION_CAMPAIGN_NAME);
				
				return new ApiError(PlanErrorCode.REPEAT_PLAN_NAME.getValue(),
						PlanErrorCode.REPEAT_PLAN_NAME.getMessage(),
						apiPosition.getPosition(), plan.getCampaignName());
			}
			dest.setPlanName(plan.getCampaignName());
		}

		if (plan.getBudget() > 0) {
			// 10<预算<=10000
			ApiError error = validBudget(dest.getBudget(), plan.getBudget(), index);
			if (error != null) {
				return error;
			}

			// 验证预算需高于所有的点击价格，added by zhuqian @beidou1.2.5
			int reasonableBudget = planMgr.checkReasonableBudget(plan
					.getCampaignId(), plan.getBudget(), false);
			if (reasonableBudget != plan.getBudget() * 100) {
				apiPosition.addParam(PlanConstant.POSITION_BUDGET);
				
				//一站式需要推广组最大点击价格，以便提示用户
				return new ApiError(PlanErrorCode.REASONABLE_PLAN_BUDGET.getValue(), 
						PlanErrorCode.REASONABLE_PLAN_BUDGET.getMessage() + " max price is [" + reasonableBudget + "]", 
						apiPosition.getPosition(), null);

			}

			// 重置预算结束标志位，重新上线,add by zp,version:1.2.0
			if (planMgr.isNeedSetBudgetOver(dest, plan.getBudget())) {
				dest.setBudgetOver(0);
			}

			dest.setBudget(plan.getBudget());
		}

		// 设置状态需要在修改开始结束时间之后,应该修改时间也会修改状态,以状态的修改为最后状态.
		// 为开始的推广计划无法修改状态
		if (plan.getStatus() != null && plan.getStatus() >= 0) {
			// 状态：0：生效；1: 搁置；2：删除；
			if (plan.getStatus() != CproPlanConstant.PLAN_STATE_NORMAL
					&& plan.getStatus() != CproPlanConstant.PLAN_STATE_PAUSE
					&& plan.getStatus() != CproPlanConstant.PLAN_STATE_DELETE) {
				apiPosition.addParam(PlanConstant.POSITION_STATUS);
				
				return new ApiError(PlanErrorCode.WRONG_PLAN_STATUS.getValue(),
						PlanErrorCode.WRONG_PLAN_STATUS.getMessage(),
						apiPosition.getPosition(), null);
			}

			// 不能暂停处于删除状态的推广计划
			if (plan.getStatus() == CproPlanConstant.PLAN_STATE_PAUSE
					&& dest.getPlanState().equals(CproPlanConstant.PLAN_STATE_DELETE)) {
				apiPosition.addParam(PlanConstant.POSITION_STATUS);
				
				return new ApiError(PlanErrorCode.WRONG_MODSTATE_DELETE_NORMAL.getValue(), 
						PlanErrorCode.WRONG_MODSTATE_DELETE_NORMAL.getMessage(), 
						apiPosition.getPosition(), null);
			}

			// 恢复推广计划时，要判断是否超过账户下非删除推广计划的最多个数
			if (plan.getStatus() == CproPlanConstant.PLAN_STATE_NORMAL
					&& dest.getPlanState().equals(
							CproPlanConstant.PLAN_STATE_DELETE)) {
				Long effectiveNumNow = planMgr.countEffectiveCproPlanByUserId(dest.getUserId());
				if (effectiveNumNow >= CproPlanConstant.PLAN_ALL_MAX_NUM) {
					apiPosition.addParam(PlanConstant.POSITION_STATUS);
					
					return new ApiError(PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getValue(), 
							PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getMessage(), 
							apiPosition.getPosition(), null);
				}
			}

			// 在删除改为生效或者搁置时，非删除的推广计划个数未达到上限：30
			/*
			 * if(dest.getPlanState()==CproPlanConstant.PLAN_STATE_DELETE &&
			 * plan.getStatus()!=CproPlanConstant.PLAN_STATE_DELETE){ long
			 * effCnt = planMgr.countEffectiveCproPlanByUserId(userid);
			 * if(effCnt>=CproPlanConstant.PLAN_EFFECTIVE_MAX_NUM){ return new
			 * ApiError(PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getValue(),
			 * PlanErrorCode.MAX_PLAN_EFFECTIVE_NUM.getMessage(),
			 * positionPre+PositionConstant.SPLIT+AdPlanConstant.POSITION_STATUS,
			 * null); } }
			 */
			dest.setPlanState(plan.getStatus());
		}

		String currDateStr = DateUtils.formatCurrrentDate();
		Date currDate = DateUtils.getRoundedDayCurDate();
		if (plan.getStartDate() != null) {
			// 本身就是Date对象，无需验证
			/**
			 * if(!DateUtils.validateDateString(plan.getStartDate())){ return
			 * new ApiError(PlanErrorCode.WRONG_PLAN_DATE_FORMAT.getValue(),
			 * PlanErrorCode.WRONG_PLAN_DATE_FORMAT.getMessage(),
			 * positionPre+PositionConstant.SPLIT+AdPlanConstant.POSITION_STARTDATE,
			 * null); }
			 */
			// 开始时间：已开始不允许修改，>=今日
			try {
				if (DateUtils.getBetweenDate(DateUtils.strToDate(dest.getStartDate()), DateUtils.strToDate(currDateStr)) >= 0
						&& DateUtils.getBetweenDate(plan.getStartDate(), DateUtils.strToDate(dest.getStartDate())) != 0 ) {
					apiPosition.addParam(PlanConstant.POSITION_START_DATE);
					return new ApiError(PlanErrorCode.WRONG_PLAN_STARTDATE_BEGIN.getValue(), 
							PlanErrorCode.WRONG_PLAN_STARTDATE_BEGIN.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (currDate.after(plan.getStartDate())) {
					apiPosition.addParam(PlanConstant.POSITION_START_DATE);
					
					return new ApiError(PlanErrorCode.WRONG_PLAN_STARTDATE_BEFORE.getValue(), 
							PlanErrorCode.WRONG_PLAN_STARTDATE_BEFORE.getMessage(), 
							apiPosition.getPosition(), null);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dest.setStartDate(DateUtils.formatDate(plan.getStartDate(), "yyyyMMdd"));
		}

		if ((plan.getEndDate() != null)) {
			// 结束时间为"",表示设置hasEndDate=0
			// 8. 结束时间：结束日期>=开始日期,结束日期>=今日

			if (DateUtils.formatDate(plan.getEndDate(), "yyyyMMdd").equals("19700101")) {
				// 设置结束时间为19700101
				dest.setEndDate(null);
				dest.setHasEndDate(0);
			} else {
				// 有结束时间
				if (DateUtils.formatDate(plan.getEndDate(), "yyyyMMdd")
						.compareTo(dest.getStartDate()) < 0) {
					apiPosition.addParam(PlanConstant.POSITION_END_DATE);
					
					return new ApiError(PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE_START.getValue(),
							PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE_START.getMessage(), 
							apiPosition.getPosition(), null);
				}
				if (DateUtils.formatDate(plan.getEndDate(), "yyyyMMdd")
						.compareTo(currDateStr) < 0) {
					apiPosition.addParam(PlanConstant.POSITION_END_DATE);
					
					return new ApiError(PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE.getValue(),
							PlanErrorCode.WRONG_PLAN_ENDDATE_BEFORE.getMessage(), 
							apiPosition.getPosition(), null);
				}
				dest.setEndDate(DateUtils.formatDate(plan.getEndDate(),
						"yyyyMMdd"));
				dest.setHasEndDate(1);
			}
			if (dest.getPlanState() == CproPlanConstant.PLAN_STATE_END) {
				dest.setPlanState(CproPlanConstant.PLAN_STATE_NORMAL);
			}
		}

		// 根据起始时间，结束时间，设置推广计划状态
		if (dest.getPlanState() == CproPlanConstant.PLAN_STATE_NORMAL
				|| dest.getPlanState() == CproPlanConstant.PLAN_STATE_NOTBEGIN
				|| dest.getPlanState() == CproPlanConstant.PLAN_STATE_END) {
			if (dest.getHasEndDate() != 0 && dest.getEndDate() != null
					&& currDateStr.compareTo(dest.getEndDate()) > 0) {
				dest.setPlanState(CproPlanConstant.PLAN_STATE_END);
			} else {
				if (dest.getStartDate().compareTo(currDateStr) > 0) {
					dest.setPlanState(CproPlanConstant.PLAN_STATE_NOTBEGIN);
				} else {
					dest.setPlanState(CproPlanConstant.PLAN_STATE_NORMAL);
				}
			}
		}

		// 设置投放日程,空数组表示全日程投放,null表示不修改
		ScheduleType[] schedule = plan.getSchedule();
		if (schedule != null) {
			// 如果输入为schedule=new ScheduleType[1]并且schedule[0].getWeekDay()==0，则表示全日程投放
			if (schedule.length == 0 || (schedule.length == 1 && schedule[0] != null
					&& schedule[0].getWeekDay() == 0)) {
				// 全日程投放
				dest.setSundayScheme(PlanConstant.FULL_SCHEDULE); // '<<'优先级低于'-');
				dest.setMondayScheme(PlanConstant.FULL_SCHEDULE);
				dest.setTuesdayScheme(PlanConstant.FULL_SCHEDULE);
				dest.setWednesdayScheme(PlanConstant.FULL_SCHEDULE);
				dest.setThursdayScheme(PlanConstant.FULL_SCHEDULE);
				dest.setFridayScheme(PlanConstant.FULL_SCHEDULE);
				dest.setSaturdayScheme(PlanConstant.FULL_SCHEDULE);
			} else {
				int result = validateSchedule(schedule);
				if (result > -1) {
					apiPosition.addParam(PlanConstant.POSITION_SCHEDULE, result);
					
					return new ApiError(PlanErrorCode.INVALID_SCHEDULE.getValue(),
							PlanErrorCode.INVALID_SCHEDULE.getMessage(),
							apiPosition.getPosition(), null);
				}
				
				int[] scheduleArray = unitSchedule(schedule);
				int total = 0;
				for (int i = 0; i < scheduleArray.length; i++) {
					total += scheduleArray[i];
				}
				if (total == 0) {
					apiPosition.addParam(PlanConstant.POSITION_SCHEDULE);
					
					return new ApiError(PlanErrorCode.INVALID_SCHEDULE.getValue(),
							PlanErrorCode.INVALID_SCHEDULE.getMessage(),
							apiPosition.getPosition(), null);
				}
				dest.setMondayScheme(scheduleArray[0]);
				dest.setTuesdayScheme(scheduleArray[1]);
				dest.setWednesdayScheme(scheduleArray[2]);
				dest.setThursdayScheme(scheduleArray[3]);
				dest.setFridayScheme(scheduleArray[4]);
				dest.setSaturdayScheme(scheduleArray[5]);
				dest.setSundayScheme(scheduleArray[6]);
			}
		}
		
		// 推广类型不可修改
		if (plan.getType() != null && !dest.getPromotionType().equals(plan.getType())) {
			apiPosition.addParam(PlanConstant.POSITION_TYPE);
			
			return new ApiError(PlanErrorCode.INVALID_TYPE.getValue(), 
					PlanErrorCode.INVALID_TYPE.getMessage(), 
					apiPosition.getPosition(), null);
		}

		// 验证推广类型以及移动设备出价
		if (plan.getWirelessBidRatio() != null) {
			boolean isValidatePromotionType = false;
			boolean isValidateWirelessBidRatio =
					(plan.getWirelessBidRatio() == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT
							 || plan.getWirelessBidRatio() == 0) ? false : true;
			if (isValidateWirelessBidRatio) {
				if (plan.getType() == null) {
					plan.setType(CproPlanConstant.PROMOTIONTYPE_ALL);
				}
				if (plan.getType() == CproPlanConstant.PROMOTIONTYPE_WIRELESS
						&& plan.getWirelessBidRatio() != CproPlanConstant.WIRELESSBIDRATIO_DEFAULT) {
					apiPosition.addParam(PlanConstant.POSITION_WIRELESSBIDRATIO);

					return new ApiError(PlanErrorCode.INVALID_WIRELESSRATIO.getValue(),
							PlanErrorCode.INVALID_WIRELESSRATIO.getMessage(),
							apiPosition.getPosition(), null);
				}
				dest.setWirelessBidRatio(plan.getWirelessBidRatio());
			}
			ApiError typeAndWirelessRatioError =
					validateTypeAndWirelessRatio(userid, plan.getType(), plan.getWirelessBidRatio(), index,
							isValidatePromotionType, isValidateWirelessBidRatio);
			if (typeAndWirelessRatioError != null) {
				return typeAndWirelessRatioError;
			}
		}
		
		// 验证移动设备与操作系统
		if (plan.getIsDeviceEnabled() != null && plan.getIsOsEnabled() != null) {
			ApiError deviceAndOsError = validateDeviceAndOs(userid, plan, index);
			if (deviceAndOsError != null) {
				return deviceAndOsError;
			} else {
				if (plan.getIsDeviceEnabled() == null) {
					plan.setIsDeviceEnabled(false);
				}
				if (plan.getIsOsEnabled() == null) {
					plan.setIsOsEnabled(false);
				}
				int isAllDevice = plan.getIsDeviceEnabled() == false ? PlanConstant.ALL_DEVICE : 0;
				Long deviceId = new Long(plan.getDevice());
				int isAllOs = plan.getIsOsEnabled() == false ? PlanConstant.ALL_OS : 0;
				List<Long> osList = CollectionsUtil.tranformLongArrayToLongList(plan.getOs());
				long deviceOperaSystem = CproPlanDeviceOSUtil.getDeviceOSDBValue(isAllDevice, deviceId, isAllOs, osList);
				dest.setDeviceOperaSystem(deviceOperaSystem);
			}
		}

		return null;
	}

	/**
	 * 返回投放日程设置
	 * 
	 * @param scheduleArray
	 * @return 长度为7的数组，从周日开始，每个元素对应该天的投放时间
	 */
	private int[] unitSchedule(ScheduleType[] scheduleArray) {
		if (scheduleArray == null) {
			return null;
		}
		// 对scheduleArray进行去重
		int[] result = new int[7];
		if (scheduleArray.length == 0) {
			// 表示全日程投放
			for (int i = 0; i < result.length; i++) {
				result[i] = PlanConstant.FULL_SCHEDULE; // '<<'优先级低于'-'
			}
		} else {
			Set<ScheduleType> uniqSch = new HashSet<ScheduleType>(7);
			for (ScheduleType sc : scheduleArray) {
				if (uniqSch.contains(sc)) {
					continue;
				}
				uniqSch.add(sc);
				// 对Schedule中的值进行合法性判断
				if (sc.getWeekDay() > 7 || sc.getWeekDay() <= 0
						|| sc.getStartTime() < 0 || sc.getStartTime() > 23
						|| sc.getEndTime() <= 0 || sc.getEndTime() > 24
						|| sc.getStartTime() > sc.getEndTime()) {
					continue;
				}
				for (int i = sc.getStartTime(); i < sc.getEndTime(); i++) {
					result[sc.getWeekDay() - 1] |= (1 << i);
				}
			}
		}
		return result;
	}
	
	public CproPlanMgr getPlanMgr() {
		return planMgr;
	}
	
	public void setPlanMgr(CproPlanMgr planMgr) {
		this.planMgr = planMgr;
	}
	
	public ApiPlanService getApiPlanService() {
		return apiPlanService;
	}
	
	public void setApiPlanService(ApiPlanService apiPlanService) {
		this.apiPlanService = apiPlanService;
	}

	public UserInfoMgr getUserInfoMgr() {
		return userInfoMgr;
	}

	public void setUserInfoMgr(UserInfoMgr userInfoMgr) {
		this.userInfoMgr = userInfoMgr;
	}
	
}
