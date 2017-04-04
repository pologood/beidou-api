package com.baidu.beidou.api.external.cprogroup.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.baidu.beidou.api.external.util.constant.BeidouAPIType;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;

/**
 * ClassName: GroupDataPrivilege
 * Function: 推广组权限验证，即验证groupId指定的推广组是否属于该userId
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GroupDataPrivilege extends AbstractDataPrivilege {
	private static final Log LOG = LogFactory.getLog(GroupDataPrivilege.class);
	private CproGroupMgr cproGroupMgr;

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result;
		Object[] params = invocation.getArguments();

		Object user = (Object) params[0];
		int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(user);
		int type = typeAndUser[0];
		int datauser = typeAndUser[2];
		Object root = params[1];

		if (ognlExp != null) {
			List<Integer> groupIds = new ArrayList<Integer>();
			List<String> position = new ArrayList<String>();
			for (int index = 0; index < ognlExp.length; index++) {
				String key = ognlExp[index];
				try {
					Object values = Ognl.getValue(key, root);
					if (values instanceof Number) {
						Number groupId;
						groupId = (Number) values;
						if (groupId == null) {
							LOG.warn("groupId is null" + key + " in" + this.getClass().getName());
							continue;
						}
						groupIds.add(groupId.intValue());
						position.add(positionExp[index]);
					} else {
						String idStr = JSONUtil.serialize(values);
						values = JSONUtil.deserialize(idStr);
						if (values instanceof Object[]) {
							Number[] list = (Number[]) values;
							for (int i = 0; i < list.length; i++) {
								Number groupId = list[i];
								if (groupId == null) {
									LOG.warn("groupId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								groupIds.add(groupId.intValue());
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
								Number groupId = (Number) value;
								if (groupId == null) {
									LOG.warn("groupId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								groupIds.add(groupId.intValue());
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

			if (!groupIds.isEmpty()) {
				
				Map<Integer, Integer> groupId2UserIdMap = cproGroupMgr.findUserIdsByGroupIds(groupIds);
				
				if (type == BeidouAPIType.OLD) {
					// 有需要验证的推广组
					ApiResult<Object> apiResult = new ApiResult<Object>();
					PaymentResult pay = apiResult.getPayment();
					int invalidNum = 0;
					for (int index = 0; index < groupIds.size(); index++) {
						Integer groupId = groupIds.get(index);
						if (groupId2UserIdMap.get(groupId) == null || !groupId2UserIdMap.get(groupId).equals(datauser)) {
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
					// 有需要验证的推广组
					BaseResponse apiResult = new BaseResponse();
					BaseResponseOptions pay = new BaseResponseOptions();
					apiResult.setOptions(pay);
					int invalidNum = 0;
					for (int index = 0; index < groupIds.size(); index++) {
						Integer groupId = groupIds.get(index);
						if (groupId2UserIdMap.get(groupId) == null || !groupId2UserIdMap.get(groupId).equals(datauser)) {
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

		//try {
			// 处理
			result = invocation.proceed();
		//} catch (Exception e) {
		//	LOG.warn(e.getMessage(), e);
		//	result = ApiResultBeanUtils.addApiError(type, null,
		//			GlobalErrorCode.SYSTEM_BUSY.getValue(),
		//			GlobalErrorCode.SYSTEM_BUSY.getMessage(),
		//			PositionConstant.SYS, null);
		//}
		return result;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

}
