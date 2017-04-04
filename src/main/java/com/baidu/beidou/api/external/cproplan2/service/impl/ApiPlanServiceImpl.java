package com.baidu.beidou.api.external.cproplan2.service.impl;

import com.baidu.beidou.api.external.cproplan2.constant.PlanConstant;
import com.baidu.beidou.api.external.cproplan2.error.PlanCreationException;
import com.baidu.beidou.api.external.cproplan2.error.PlanErrorCode;
import com.baidu.beidou.api.external.cproplan2.service.ApiPlanService;
import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.util.SessionHolder;

/**
 * ClassName: ApiPlanServiceImpl
 * Function: 增加推广计划
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class ApiPlanServiceImpl implements ApiPlanService {

	private CproPlanMgr planMgr = null;

	public ApiResult<CampaignType> addPlan(ApiResult<CampaignType> result, 
			CproPlan plan, int index) throws PlanCreationException {
		if (result == null) {
			return null;
		}
		
		if (plan == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PlanConstant.CAMPAIGN_TYPES, index);
			
			ApiError error = new ApiError(PlanErrorCode.PLAN_CREATE_FAILED.getValue(), 
					PlanErrorCode.PLAN_CREATE_FAILED.getMessage(),
					apiPosition.getPosition(), null);
			ApiResultBeanUtils.addApiError(result, error);
		} else {
			// 添加推广计划
			Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
			CproPlan cproPlan = planMgr.addCproPlan(visitor, plan);
			plan.setPlanId(cproPlan.getPlanId());
		}
		
		return result;
	}
	
	public CproPlanMgr getPlanMgr() {
		return planMgr;
	}
	
	public void setPlanMgr(CproPlanMgr planMgr) {
		this.planMgr = planMgr;
	}
}
