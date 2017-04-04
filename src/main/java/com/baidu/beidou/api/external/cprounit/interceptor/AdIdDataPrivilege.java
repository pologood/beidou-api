package com.baidu.beidou.api.external.cprounit.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.BeidouAPIType;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;

/**
 * ClassName: AdIdDataPrivilege Function: unit层级拦截器
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public class AdIdDataPrivilege extends AbstractDataPrivilege {
	private static final Log LOG = LogFactory.getLog(AdIdDataPrivilege.class);
	private CproUnitMgr unitMgr = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result;
		Object[] params = invocation.getArguments();

		Object user = (Object) params[0];
		int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(user);
		int type = typeAndUser[0];
		int datauser = typeAndUser[2];
		Object root = params[1];

		if (ognlExp != null) {
			List<Long> adIds = new ArrayList<Long>();
			List<String> position = new ArrayList<String>();
			for (int index = 0; index < ognlExp.length; index++) {
				String key = ognlExp[index];
				try {
					Object values = Ognl.getValue(key, root);
					if (values instanceof Number) {
						Number adId;
						adId = (Number) values;
						adIds.add(adId.longValue());
						position.add(positionExp[index]);
					} else {
						String idStr = JSONUtil.serialize(values);
						values = JSONUtil.deserialize(idStr);
						if (values instanceof Object[]) {
							Number[] list = (Number[]) values;
							for (int i = 0; i < list.length; i++) {
								Number adId = list[i];
								if (adId == null) {
									LOG.warn("adId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								adIds.add(adId.longValue());
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
								Number adId = (Number) value;
								if (adId == null) {
									LOG.warn("adId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								adIds.add(adId.longValue());
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

			if (!adIds.isEmpty()) {
				
				User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
				Map<Long, Integer> unitId2UserIdMap = unitMgr.findUserIdsByUnitId(adIds, bdUser.getUserid());
				
				if (type == BeidouAPIType.OLD) {
					// 有需要验证的推广创意ID
					ApiResult<Object> apiResult = new ApiResult<Object>();
					PaymentResult pay = apiResult.getPayment();
					int invalidNum = 0;
					for (int index = 0; index < adIds.size(); index++) {
						Long unitId = adIds.get(index);
						if (unitId2UserIdMap.get(unitId) == null || !unitId2UserIdMap.get(unitId).equals(datauser)) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(position.get(index));
							
							apiResult = ApiResultBeanUtils.addApiError(apiResult,
									GlobalErrorCode.UNAUTHORIZATION.getValue(),
									GlobalErrorCode.UNAUTHORIZATION.getMessage(),
									apiPosition.getPosition(), null);
							invalidNum++;
						}
					}
					
					if (apiResult.hasErrors()) {
						pay.setSuccess(0);
						pay.setTotal(invalidNum);
						return apiResult;
					}
				} else {
					// 有需要验证的推广创意ID
					BaseResponse apiResult = new BaseResponse();
					BaseResponseOptions pay = new BaseResponseOptions();
					apiResult.setOptions(pay);
					int invalidNum = 0;
					for (int index = 0; index < adIds.size(); index++) {
						Long unitId = adIds.get(index);
						if (unitId2UserIdMap.get(unitId) == null || !unitId2UserIdMap.get(unitId).equals(datauser)) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(position.get(index));
							
							apiResult = DRAPIMountAPIBeanUtils.addApiError(apiResult,
									GlobalErrorCode.UNAUTHORIZATION.getValue(),
									GlobalErrorCode.UNAUTHORIZATION.getMessage(),
									apiPosition.getPosition(), null);
							invalidNum++;
						}
					}
					
					if (apiResult.getErrors() != null && apiResult.getErrors().size() != 0) {
						pay.setSuccess(0);
						pay.setTotal(invalidNum);
						return apiResult;
					}
				}
			}
		}

		try {
			// 处理
			result = invocation.proceed();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.SYS, null);
		}
		return result;
	}

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

}
