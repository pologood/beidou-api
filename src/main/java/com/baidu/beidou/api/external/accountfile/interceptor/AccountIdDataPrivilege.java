package com.baidu.beidou.api.external.accountfile.interceptor;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.accountfile.constant.AccountFileConstant;
import com.baidu.beidou.api.external.accountfile.error.AccountFileErrorCode;
import com.baidu.beidou.api.external.cproplan.interceptor.PlanIdDataPrivilege;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;

/**
 * 
 * ClassName: AccountIdDataPrivilege  <br>
 * Function: 传入planid的权限验证器
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountIdDataPrivilege  extends AbstractDataPrivilege {

	private static final Log LOG = LogFactory.getLog(PlanIdDataPrivilege.class);
	private CproPlanMgr planMgr = null;

	@SuppressWarnings("rawtypes")
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result;
		Object[] params = invocation.getArguments();
		if (params == null || params.length < 2
				|| !(params[0] instanceof DataPrivilege)) {
			LOG.warn("UNEXPECTED_PARAMETER " + params);
			return ApiResultBeanUtils.addApiError(null,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					PositionConstant.NOPARAM, null);
		}

		DataPrivilege user = (DataPrivilege) params[0];
		Object root = params[1];

		if (ognlExp != null) {
			List<Integer> planIds = new ArrayList<Integer>();
			List<String> position = new ArrayList<String>();
			for (int index = 0; index < ognlExp.length; index++) {
				String key = ognlExp[index];
				try {
					Object values = Ognl.getValue(key, root);
					if (values instanceof Number) {
						Number planId;
						planId = (Number) values;
						planIds.add(planId.intValue());
						position.add(positionExp[index]);
					} else {
						String idStr = JSONUtil.serialize(values);
						values = JSONUtil.deserialize(idStr);
						if (values instanceof Object[]) {
							Number[] list = (Number[]) values;
							for (int i = 0; i < list.length; i++) {
								Number planId = list[i];
								if (planId == null) {
									LOG.warn("planid is null" + key + " in" + this.getClass().getName());
									continue;
								}
								planIds.add(planId.intValue());
								if (idExp[index] == null) {
									position.add(positionExp[index] + "[" + i + "]");
								} else {
									position.add(positionExp[index] + "[" + i + "]." + idExp[index]);
								}
							}
						} else if (values instanceof List) {
							List list = (List) values;
							for (int i = 0; i < list.size(); i++) {
								Object value = list.get(i);
								Number planId = (Number) value;
								if (planId == null) {
									LOG.warn("planid is null" + key + " in" + this.getClass().getName());
									continue;
								}
								planIds.add(planId.intValue());
								if (idExp[index] == null) {
									position.add(positionExp[index] + "[" + i + "]");
								} else {
									position.add(positionExp[index] + "[" + i + "]." + idExp[index]);
								}
							}
						}

					}
				} catch (OgnlException e) {
				}
			}

			if (!planIds.isEmpty()) {
				// 有需要验证的推广计划ID
				ApiResult<Object> apiResult = new ApiResult<Object>();
				PaymentResult pay = apiResult.getPayment();
				pay.setTotal(1);
				if(planIds.size() > AccountFileWebConstants.MAX_PLANIDS){
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_ID.POSITION_CAMPAIGNIDS);
					result = ApiResultBeanUtils.addApiError(
							apiResult,AccountFileErrorCode.TOO_MANY_CAMPAIGNIDS.getValue(),
							AccountFileErrorCode.TOO_MANY_CAMPAIGNIDS.getMessage(),
							apiPosition.getPosition(),
							null);
					return result;
				}
				
				// 有需要验证的推广计划ID
				pay.setTotal(planIds.size());
				
				for (int index = 0; index < planIds.size(); index++) {
					List<CproPlan> cproPlan = planMgr.findCproPlanByPlanIds(planIds.subList(index, index + 1));
					if (cproPlan == null || cproPlan.isEmpty()
							|| cproPlan.get(0).getUserId() != user.getDataUser()) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(position.get(index));
						
						apiResult = ApiResultBeanUtils.addApiError(apiResult,
								GlobalErrorCode.UNAUTHORIZATION.getValue(),
								GlobalErrorCode.UNAUTHORIZATION.getMessage(),
								apiPosition.getPosition(), null);
					} else if (cproPlan.get(0).getPlanState() == CproPlanConstant.PLAN_STATE_DELETE) {
						// 如果计划已删除，则不允许输出数据
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(position.get(index));
						
						apiResult = ApiResultBeanUtils.addApiError(apiResult,
								AccountFileErrorCode.PLAN_HAS_BEEN_MARK_AS_DELETE.getValue(),
								AccountFileErrorCode.PLAN_HAS_BEEN_MARK_AS_DELETE.getMessage(),
								apiPosition.getPosition(), null);
					}
				}
				
				if (apiResult.hasErrors()) {
					pay.setSuccess(0);
					return apiResult;
				}
			}
		}

		try {
			// 处理
			result = invocation.proceed();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.SYS, null);
		}
		return result;
	}

	/**
	 * @return the planMgr
	 */
	public CproPlanMgr getPlanMgr() {
		return planMgr;
	}

	/**
	 * @param planMgr
	 *            the planMgr to set
	 */
	public void setPlanMgr(CproPlanMgr planMgr) {
		this.planMgr = planMgr;
	}
	
}
