package com.baidu.beidou.api.internal.audit.service;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.UserInfo;

public interface UserInfoService {
	
	/**
	 * getUserInfo: 获取用户信息
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 21, 2013
	 */
	public List<UserInfo> getUserInfo(List<Integer> userIds);

}
