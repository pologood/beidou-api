package com.baidu.beidou.api.external.cproplan.exporter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.cproplan.constant.PlanConstant;
import com.baidu.beidou.api.external.cproplan.error.PlanErrorCode;
import com.baidu.beidou.api.external.cproplan.exporter.CampaignService;
import com.baidu.beidou.api.external.cproplan.facade.PlanFacade;
import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cproplan.vo.PlanOffTimeVo;
import com.baidu.beidou.stat.util.DateUtil;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.SessionHolder;

/**
 * ClassName: CampaignServiceImpl
 * Function: 推广计划设置，包含新建、修改及查询
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class CampaignServiceImpl implements CampaignService {

	private CproPlanMgr cproPlanMgr = null;
	private PlanFacade planFacade = null;
	
	// 推广组设置相关阀值限制
	private int addCampaignMax;
	private int getCampaignByCampaignIdMax;
	private int updateCampaignMax;

	public ApiResult<CampaignType> addCampaign(DataPrivilege user,
			AddCampaignRequest request, ApiOption apiOption) {
		/**
		 * 参数符合wsdl规范
		 */
		ApiResult<CampaignType> result = new ApiResult<CampaignType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null  || request.getCampaignTypes() == null
				|| request.getCampaignTypes().length < 1 ) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		int planNum = request.getCampaignTypes().length;
		if (planNum > this.addCampaignMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		// 设置总数
		PaymentResult payment = new PaymentResult();
		payment.setTotal(planNum);
		result.setPayment(payment);

		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < planNum; index++) {
			CampaignType campaignType = request.getCampaignTypes()[index];
			campaignType.setCampaignId(0);	// 表示该推广计划是新增的

			result = planFacade.addPlan(result, user, campaignType, index, optContents);
		}
		//记录历史操作
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;

	}

	public ApiResult<CampaignType> getCampaignByCampaignId(DataPrivilege user,
			GetCampaignByCampaignIdRequest request, ApiOption apiOption) {
		
		PaymentResult payment = new PaymentResult();
		ApiResult<CampaignType> result = new ApiResult<CampaignType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		if (request != null && request.getCampaignIds() != null) {
			if (request.getCampaignIds().length > getCampaignByCampaignIdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(PlanConstant.CAMPAIGN_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						PlanErrorCode.TOOMANY_NUM.getValue(),
						PlanErrorCode.TOOMANY_NUM.getMessage(), 
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

		payment.setTotal(request.getCampaignIds().length);

		List<CproPlan> cproPlanList = new ArrayList<CproPlan>();
		int idIndex = 0;
		for (Long planId : request.getCampaignIds()) {
			CproPlan cproPlan = cproPlanMgr.findCproPlanById(planId.intValue());
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_IDS, idIndex);
			
			if (cproPlan == null) {
				result = ApiResultBeanUtils.addApiError(result,
						PlanErrorCode.NO_PLAN.getValue(), 
						PlanErrorCode.NO_PLAN.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			if (!cproPlan.getUserId().equals(user.getDataUser())) {
				result = ApiResultBeanUtils.addApiError(result,
						PlanErrorCode.WRONG_USER.getValue(),
						PlanErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
			} else {
				cproPlanList.add(cproPlan);
			}
			idIndex++;
		}
		
		// 查询推广计划下线时间
		List<Integer> planIds = new ArrayList<Integer>();
		for(CproPlan cproPlan: cproPlanList){
			planIds.add(cproPlan.getPlanId());
		}
		List<PlanOffTimeVo> offtimePlans = cproPlanMgr.findCproPlanOfftime(planIds);
		Map<Integer, Date> offPlanMap = new HashMap<Integer, Date>();
		for(PlanOffTimeVo vo: offtimePlans){
			offPlanMap.put(vo.getPlanId(), vo.getOfftime());
		}
		Date today = new Date();

		Mapper mapper = BeanMapperProxy.getMapper();
		for (CproPlan cproPlan : cproPlanList) {
			CampaignType plan = mapper.map(cproPlan, CampaignType.class);
			// 处理日程
			List<ScheduleType> scheduleList = new ArrayList<ScheduleType>();

			decSchedual(scheduleList, cproPlan.getMondayScheme(), 0);
			decSchedual(scheduleList, cproPlan.getTuesdayScheme(), 1);
			decSchedual(scheduleList, cproPlan.getWednesdayScheme(), 2);
			decSchedual(scheduleList, cproPlan.getThursdayScheme(), 3);
			decSchedual(scheduleList, cproPlan.getFridayScheme(), 4);
			decSchedual(scheduleList, cproPlan.getSaturdayScheme(), 5);
			decSchedual(scheduleList, cproPlan.getSundayScheme(), 6);

			ScheduleType[] scheduleArray = new ScheduleType[scheduleList.size()];
			for (int i = 0; i < scheduleList.size(); i++) {
				scheduleArray[i] = scheduleList.get(i);
			}
			plan.setSchedule(scheduleArray);
			
			// 加入推广计划下线时间
			if(offPlanMap.containsKey(cproPlan.getPlanId())){
				if (DateUtil.isSameDay(today, offPlanMap.get(cproPlan.getPlanId()))) {
					plan.setStatus(CproPlanConstant.PLAN_VIEW_STATE_OFFLINE);
				}
			}

			result = ApiResultBeanUtils.addApiResult(result, plan);
			payment.increSuccess();
		}

		result.setPayment(payment);
		return result;
	}

	public ApiResult<CampaignType> getCampaign(DataPrivilege user,
			GetCampaignRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<CampaignType> result = new ApiResult<CampaignType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		// 查询的计划数量超出限制
		Long planCnt = cproPlanMgr.countCproPlanByUserId(user.getDataUser());
		if (planCnt != null && planCnt > PlanConstant.MAX_QUERY_PLAN_NUM) {
			result = ApiResultBeanUtils.addApiError(result,
					PlanErrorCode.TOOMANY_QUERY_PLAN.getValue(),
					PlanErrorCode.TOOMANY_QUERY_PLAN.getMessage(), null, null);
			return result;
		}

		List<CproPlan> cproPlanList = cproPlanMgr.findCproPlanByUserId(user
				.getDataUser());
		
		// 查询推广计划下线时间
		List<Integer> planIds = new ArrayList<Integer>();
		for(CproPlan cproPlan: cproPlanList){
			planIds.add(cproPlan.getPlanId());
		}
		List<PlanOffTimeVo> offtimePlans = cproPlanMgr.findCproPlanOfftime(planIds);
		Map<Integer, Date> offPlanMap = new HashMap<Integer, Date>();
		for(PlanOffTimeVo vo: offtimePlans){
			offPlanMap.put(vo.getPlanId(), vo.getOfftime());
		}
		Date today = new Date();

		Mapper mapper = BeanMapperProxy.getMapper();
		if (cproPlanList != null) {
			payment.setTotal(cproPlanList.size());
			for (CproPlan cproPlan : cproPlanList) {
				CampaignType plan = mapper.map(cproPlan, CampaignType.class);
				// 处理日程
				List<ScheduleType> scheduleList = new ArrayList<ScheduleType>();

				decSchedual(scheduleList, cproPlan.getMondayScheme(), 0);
				decSchedual(scheduleList, cproPlan.getTuesdayScheme(), 1);
				decSchedual(scheduleList, cproPlan.getWednesdayScheme(), 2);
				decSchedual(scheduleList, cproPlan.getThursdayScheme(), 3);
				decSchedual(scheduleList, cproPlan.getFridayScheme(), 4);
				decSchedual(scheduleList, cproPlan.getSaturdayScheme(), 5);
				decSchedual(scheduleList, cproPlan.getSundayScheme(), 6);

				ScheduleType[] scheduleArray = new ScheduleType[scheduleList
						.size()];
				for (int i = 0; i < scheduleList.size(); i++) {
					scheduleArray[i] = scheduleList.get(i);
				}
				plan.setSchedule(scheduleArray);
				
				// 加入推广计划下线时间
				if(offPlanMap.containsKey(cproPlan.getPlanId())){
					if (DateUtil.isSameDay(today, offPlanMap.get(cproPlan.getPlanId()))) {
						plan.setStatus(CproPlanConstant.PLAN_VIEW_STATE_OFFLINE);
					}
				}

				result = ApiResultBeanUtils.addApiResult(result, plan);
				payment.increSuccess();
			}
		}

		result.setPayment(payment);
		return result;
	}

	public ApiResult<Long> getCampaignId(DataPrivilege user,
			GetCampaignIdRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<Long> result = new ApiResult<Long>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		// 查询的计划数量超出限制
		Long planCnt = cproPlanMgr.countCproPlanByUserId(user.getDataUser());
		if (planCnt != null && planCnt > PlanConstant.MAX_QUERY_PLAN_NUM) {
			result = ApiResultBeanUtils.addApiError(result,
					PlanErrorCode.TOOMANY_QUERY_PLAN.getValue(),
					PlanErrorCode.TOOMANY_QUERY_PLAN.getMessage(), null, null);
			return result;
		}

		List<CproPlan> cproPlanList = cproPlanMgr.findCproPlanByUserId(user.getDataUser());

		if (cproPlanList != null) {
			payment.setTotal(cproPlanList.size());
			for (CproPlan cproPlan : cproPlanList) {
				result = ApiResultBeanUtils.addApiResult(result, 
						new Long(cproPlan.getPlanId()));
				payment.increSuccess();
			}
			
		}
		result.setPayment(payment);
		return result;
	}

	public ApiResult<CampaignType> updateCampaign(DataPrivilege user,
			UpdateCampaignRequest request, ApiOption apiOption) {
		/**
		 * 参数符合wsdl规范
		 */
		ApiResult<CampaignType> result = new ApiResult<CampaignType>();
		PaymentResult payment = new PaymentResult();
		result.setPayment(payment);

		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		
		if (request == null  || request.getCampaignTypes() == null || 
				request.getCampaignTypes().length < 1) {
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 批量个数超过100的限制
		int planNum = request.getCampaignTypes().length;
		if (planNum > updateCampaignMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES);
			
			result = ApiResultBeanUtils.addApiError(result,
					PlanErrorCode.TOOMANY_NUM.getValue(),
					PlanErrorCode.TOOMANY_NUM.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 设置总数
		payment.setTotal(planNum);

		ApiError error = null;
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < request.getCampaignTypes().length; index++) {
			CampaignType plan = request.getCampaignTypes()[index];
			if (plan == null) {
				continue;
			}
			
			error = planFacade.updatePlan(user, plan, index, optContents);
			if (error == null) {
				// 更新成功
				ApiResultBeanUtils.addApiResult(result, plan);
				payment.increSuccess();
			} else {
				ApiResultBeanUtils.addApiError(result, error);
			}

		}
		//记录历史操作
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		result.setPayment(payment);
		return result;
	}
	
	private void decSchedual(List<ScheduleType> scheduleList, Integer scheme,
			Integer dayofWeek) {
		if (scheme != null && scheme != 0) {
			int j = 0;
			ScheduleType schedule = null;
			for (int i = scheme; i > 0; i = i / 2) {
				if (i % 2 == 1) {
					if (schedule == null) {
						schedule = new ScheduleType();
						schedule.setWeekDay(dayofWeek + 1);
						schedule.setStartTime(j);
						scheduleList.add(schedule);
					}
					schedule.setEndTime(j + 1);
				} else {
					schedule = null;
				}
				j++;
			}
		}
	}

	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}
	
	public PlanFacade getPlanFacade() {
		return planFacade;
	}

	public void setPlanFacade(PlanFacade planFacade) {
		this.planFacade = planFacade;
	}

	public void setAddCampaignMax(int addCampaignMax) {
		this.addCampaignMax = addCampaignMax;
	}

	public void setGetCampaignByCampaignIdMax(int getCampaignByCampaignIdMax) {
		this.getCampaignByCampaignIdMax = getCampaignByCampaignIdMax;
	}

	public void setUpdateCampaignMax(int updateCampaignMax) {
		this.updateCampaignMax = updateCampaignMax;
	}
}
