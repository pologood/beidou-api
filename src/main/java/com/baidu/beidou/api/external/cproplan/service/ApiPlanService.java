package com.baidu.beidou.api.external.cproplan.service;

import com.baidu.beidou.api.external.cproplan.error.PlanCreationException;
import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.cproplan.bo.CproPlan;

/**
 * ClassName: ApiPlanService
 * Function: 增加推广计划
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public interface ApiPlanService {

	/**
	 * 
	 * @param plan
	 * @param group
	 * @param result
	 * @return
	 * @throws PlanCreationException
	 *             添加推广计划验证失败，推广计划需要回滚
	 */
	public ApiResult<CampaignType> addPlan(ApiResult<CampaignType> result,
			CproPlan plan, int index) throws PlanCreationException;
}
