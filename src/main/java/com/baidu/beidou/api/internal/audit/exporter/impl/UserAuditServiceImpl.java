package com.baidu.beidou.api.internal.audit.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.UserAuditService;
import com.baidu.beidou.api.internal.audit.service.UserInfoService;
import com.baidu.beidou.api.internal.audit.service.ReauditInfoService;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ReauditUserInfo;
import com.baidu.beidou.api.internal.audit.vo.UserInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.util.StringUtils;

public class UserAuditServiceImpl implements UserAuditService {

	private UserInfoService userInfoService;
	
	private ReauditInfoService reauditInfoService;
	
	public AuditResult<UserInfo> getUserInfo(List<Integer> userIds) {
		AuditResult<UserInfo> result = new AuditResult<UserInfo>();
		if (CollectionUtils.isEmpty(userIds)) {
			return result;
		}
		
		List<UserInfo> list = userInfoService.getUserInfo(userIds);
		result.addResults(list);
		
		return result;
	}
	
	public AuditResult<ReauditUserInfo> getReauditUserList(QueryBase query) {
		AuditResult<ReauditUserInfo> result = new AuditResult<ReauditUserInfo>();
		if (query == null) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if (query.getQueryType() != QueryConstant.QueryUserReaudit.allUser
				&& query.getQueryType() != QueryConstant.QueryUserReaudit.userName
				&& query.getQueryType() != QueryConstant.QueryUserReaudit.userId) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		List<ReauditUserInfo> list = new ArrayList<ReauditUserInfo>();
		int totalNum = reauditInfoService.findReauditUserList(query, list);
		result.addResults(list);
		
		int totalPage = (totalNum - 1) / query.getPageSize() + 1;
		result.setTotalNum(totalNum);
		result.setTotalPage(totalPage);
		
		return result;
	}

	public UserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public ReauditInfoService getReauditInfoService() {
		return reauditInfoService;
	}

	public void setReauditInfoService(ReauditInfoService reauditInfoService) {
		this.reauditInfoService = reauditInfoService;
	}
}
