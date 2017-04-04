package com.baidu.beidou.api.external.fc.interceptor;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.fc.vo.FCCampaignType;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: FCDataPrivilege  <br>
 * Function: 验证campaignIds,campaignId,unitId的权限拦截器
 * Note：暂时未启用，由于业务逻辑不复杂，查两次FC-API从性能考虑可以节约一次不必要的权限查询
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb398
 * @date Jan 10, 2012
 */
public class FCDataPrivilege extends AbstractDataPrivilege {

	private static final Log LOG = LogFactory.getLog(FCDataPrivilege.class);

	@SuppressWarnings("rawtypes")
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result;
		Object[] params = invocation.getArguments();
		if (params == null || params.length < 2 || !(params[0] instanceof DataPrivilege)) {
			LOG.warn("UNEXPECTED_PARAMETER " + params);
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			
			return ApiResultBeanUtils.addApiError(null,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
		}

		Object root = params[1];

		if (ognlExp != null) {
			List<Long> peopleIds = new ArrayList<Long>();
			List<String> position = new ArrayList<String>();
			for (int index = 0; index < ognlExp.length; index++) {
				String key = ognlExp[index];
				try {
					Object values = Ognl.getValue(key, root);
					if (values instanceof Number) {
						Number peopleId;
						peopleId = (Number) values;
						peopleIds.add(peopleId.longValue());
						position.add(positionExp[index]);
					} else {
						String idStr = JSONUtil.serialize(values);
						values = JSONUtil.deserialize(idStr);
						if (values instanceof Object[]) {
							Number[] list = (Number[]) values;
							for (int i = 0; i < list.length; i++) {
								Number peopleId = list[i];
								if (peopleId == null) {
									LOG.warn("peopleId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								peopleIds.add(peopleId.longValue());
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
								Number peopleId = (Number) value;
								if (peopleId == null) {
									LOG.warn("peopleId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								peopleIds.add(peopleId.longValue());
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

			ApiResult<FCCampaignType> tmpResult = new ApiResult<FCCampaignType>();
//			if (!peopleIds.isEmpty()) {
//				for (int index = 0; index < peopleIds.size(); index++) {
//					VtPeople vtPeople = vtPeopleMgr.findVtPeople(peopleIds.get(index), user.getDataUser());
//					if(vtPeople == null){
//						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
//						apiPosition.addParam(position.get(index));
//
//						ApiResultBeanUtils.addApiError(tmpResult,
//								GlobalErrorCode.UNAUTHORIZATION.getValue(),
//								GlobalErrorCode.UNAUTHORIZATION.getMessage(), 
//								apiPosition.getPosition(), null);
//					}
//				}
//			}
			if(tmpResult.getErrors() != null && !tmpResult.getErrors().isEmpty()){
				return tmpResult;
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

	
	
}
