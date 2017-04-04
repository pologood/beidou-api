package com.baidu.beidou.api.internal.audit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.audit.service.UserInfoService;
import com.baidu.beidou.api.internal.audit.vo.UserInfo;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.dao.UserDao;

public class UserInfoServiceImpl implements UserInfoService {

	private UserDao userDao;
	
	public List<UserInfo> getUserInfo(List<Integer> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return null;
		}
		
		List<User> users = userDao.findUsersByIds(userIds);
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		
		List<UserInfo> result = new ArrayList<UserInfo>();
		for (User user : users) {
			UserInfo info = new UserInfo();
			info.setUserId(user.getUserid());
			info.setUserName(user.getUsername());
			info.setUserStatus(user.getUstate());
			result.add(info);
		}
		
		return result;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
