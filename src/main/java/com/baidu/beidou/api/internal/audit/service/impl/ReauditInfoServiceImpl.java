package com.baidu.beidou.api.internal.audit.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.dao.UnitAuditDao;
import com.baidu.beidou.api.internal.audit.dao.UnitAuditDaoOnMultiAddb;
import com.baidu.beidou.api.internal.audit.service.ReauditInfoService;
import com.baidu.beidou.api.internal.audit.vo.ReauditUserInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.api.util.PagerUtils;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.dao.UserDao;
import com.baidu.beidou.util.ThreadContext;

public class ReauditInfoServiceImpl implements ReauditInfoService {
	
	private UnitAuditDao unitAuditDao;
	private UnitAuditDaoOnMultiAddb unitAuditDaoOnMultiAddb;
	private UserDao userDao;

	public int findReauditUserList(QueryBase query, List<ReauditUserInfo> result) {
		// 设置空的userId，防止报错
		ThreadContext.putUserId(1);
		List<Integer> userIds = unitAuditDao.findAllUserIds(query);
		Collections.sort(userIds);
		
		List<Integer> pageUserIds = PagerUtils.getPage(userIds, query.getPage(), query.getPageSize());
		if (CollectionUtils.isEmpty(pageUserIds)) {
			return 0;
		}
		
		// 获取用户名
		List<User> users = userDao.findUsersByIds(pageUserIds);
		if (CollectionUtils.isEmpty(users)) {
			return 0;
		}
		
		for (User user : users) {
			ReauditUserInfo info = new ReauditUserInfo();
			Integer userId = user.getUserid();
			info.setUserId(userId);
			info.setUserName(user.getUsername());
			info.setUserState(AuditConstant.USER_VIEW_STATE_NAME[user.getUstate()]);
			
			result.add(info);
		}
		
		return userIds.size();
	}
	
	public UnitAuditDao getUnitAuditDao() {
		return unitAuditDao;
	}

	public void setUnitAuditDao(UnitAuditDao unitAuditDao) {
		this.unitAuditDao = unitAuditDao;
	}

	public UnitAuditDaoOnMultiAddb getUnitAuditDaoOnMultiAddb() {
		return unitAuditDaoOnMultiAddb;
	}

	public void setUnitAuditDaoOnMultiAddb(
			UnitAuditDaoOnMultiAddb unitAuditDaoOnMultiAddb) {
		this.unitAuditDaoOnMultiAddb = unitAuditDaoOnMultiAddb;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
