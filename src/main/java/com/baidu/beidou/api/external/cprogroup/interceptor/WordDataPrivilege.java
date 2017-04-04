package com.baidu.beidou.api.external.cprogroup.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;

/**
 * ClassName: WordDataPrivilege
 * Function: ct关键词权限验证，即验证ctKeywordId指定的ct词是否属于该userId
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class WordDataPrivilege extends AbstractDataPrivilege {
	private static final Log LOG = LogFactory.getLog(WordDataPrivilege.class);
	private CproKeywordMgr cproKeywordMgr;
	private UserMgr userMgr;

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

		DataPrivilege user = (DataPrivilege) params[0];
		Object root = params[1];

		if (ognlExp != null) {
			List<Long> keywords = new ArrayList<Long>();
			List<String> position = new ArrayList<String>();
			for (int index = 0; index < ognlExp.length; index++) {
				String key = ognlExp[index];
				try {
					Object values = Ognl.getValue(key, root);
					if (values instanceof Number) {
						Number keywordId;
						keywordId = (Number) values;
						if (keywordId == null) {
							LOG.warn("keywordId is null" + key + " in"	+ this.getClass().getName());
							continue;
						}
						keywords.add(keywordId.longValue());
						position.add(positionExp[index]);
					} else {
						String idStr = JSONUtil.serialize(values);
						values = JSONUtil.deserialize(idStr);
						if (values instanceof Object[]) {
							Number[] list = (Number[]) values;
							for (int i = 0; i < list.length; i++) {
								Number keywordId = list[i];
								if (keywordId == null) {
									LOG.warn("keywordId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								keywords.add(keywordId.longValue());
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
								Number keywordId = (Number) value;
								if (keywordId == null) {
									LOG.warn("keywordId is null" + key + " in" + this.getClass().getName());
									continue;
								}
								keywords.add(keywordId.longValue());
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

			if (!keywords.isEmpty()) {
				// 有需要验证的推广主题词ID
				User sfUser = userMgr.findUserBySFid(user.getDataUser());
				if (sfUser == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);

					return ApiResultBeanUtils.addApiError(null,
							GlobalErrorCode.UNAUTHORIZATION.getValue(),
							GlobalErrorCode.UNAUTHORIZATION.getMessage(),
							apiPosition.getPosition(), null);
				}
				List<CproKeyword> cproKeywords = cproKeywordMgr.findByIds(keywords, sfUser.getUserid());
				if (CollectionUtils.isNotEmpty(cproKeywords)) {
					Map<Long, CproKeyword> keywordMap = new HashMap<Long, CproKeyword>(cproKeywords.size());
					for (CproKeyword keyword : cproKeywords) {
						keywordMap.put(keyword.getId(), keyword);
					}
					
					ApiResult<Object> apiResult = new ApiResult<Object>();
					PaymentResult pay = apiResult.getPayment();
					int invalidNum = 0;
					for (int index = 0; index < keywords.size(); index++) {
						Long id = keywords.get(index);
						CproKeyword keyword = keywordMap.get(id);
						if (keyword != null && user.getDataUser() != keyword.getUserId()) {
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

	public CproKeywordMgr getCproKeywordMgr() {
		return cproKeywordMgr;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

}
