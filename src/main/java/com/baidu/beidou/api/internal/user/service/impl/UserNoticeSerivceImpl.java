/**
 * @version 1.1.0
 * 2009-12-21 上午10:50:44
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.user.service.UserNoticeService;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.constant.USER_LEVEL;
import com.baidu.beidou.user.constant.UserConstant;
import com.baidu.beidou.user.service.UserMgr;

/**
 * @author zengyunfeng
 * 
 */
public class UserNoticeSerivceImpl implements UserNoticeService {
	private static final Log LOG = LogFactory
			.getLog(UserNoticeSerivceImpl.class);
	private UserMgr userMgr;
	private static final String USERNAME_KEY = "username";
	private static final String USERSTATUS_KEY = "ustatus";
	private static final String USERLEVEL_KEY = "ulevel";

	/**
	 * 新增外部客户通知<br>
	 * 注意需要是外部的广告主客户新增，即ulevelid为10101和10104的用户 useraccount中插入数据，如果已经存在，则更新。
	 * 
	 * @version 1.2.18
	 * @author zengyunfeng
	 * @param token
	 * @param userId
	 * @param userName
	 * @param shifenStatus
	 */
	private boolean addUser(long token, int userId, String userName,
			int shifenStatus) {
		User user = userMgr.findUserBySFid(userId);
		int sfstattransfer = UserConstant.SHIFEN_TRANSFER_STATE_NORMAL;
		if (shifenStatus == UserConstant.SHIFEN_STATE_REFUSE) {
			sfstattransfer = UserConstant.SHIFEN_TRANSFER_STATE_REFUSE;
		}
		if (user == null || user.getUstate() == UserConstant.USER_STATE_DELETED) {
			if (user == null) {
				user = new User();
			}
			// 插入新的外部用户
			// REPLACE INTO beidou.useraccount
			// (userid, username, password,ulevelid, ushifenstatid, utype,
			// ustate, balance, dummyaccount,sfstattransfer)
			// values(
			// NEW.userid, CONVERT(NEW.username USING
			// GBK), NEW.password, NEW.ulevelid, NEW.ustatid, 0, 0 ,0, 0,
			// newsfstattransfer);
			user.setUserid(userId);
			user.setUsername(userName);
			user.setUshifenstatid(shifenStatus);
			user.setUstate(0);
			user.setSfstattransfer(sfstattransfer);
			user.setHelpstatus(0);
			user.setBalancestat(0);
		} else {
			LOG.error("已经存在的用户重复进行插入");
			return true;
		}
		user = userMgr.saveOrUpdateUser(user);
		return true;
	}

	/**
	 * 用户shifen状态通知<br>
	 * 更新useraccount中的ushifenstatid字段， 并根据新的ushifenstatid值判断是否需要更新sfstattransfer
	 * (如果ushifenstatid=被拒绝时，则sfstattransfer=1；如果ushifenstatid=正常生效或者账面为0，则sfstattransfer=0；其他值不变化)。
	 * 更新审核员缓存。
	 * 
	 * @version 1.2.18
	 * @author zengyunfeng
	 * @param token
	 *            本次操作的token，用于去重
	 * @param userId
	 *            用户id
	 * @param oldShifenStatus
	 *            更新之前的shifen状态
	 * @param newShifenStatus
	 *            更新之后的shifen状态
	 */
	private boolean updateShifenState(long token, int userId,
			int newShifenStatus) {
		// 普通用户
		// 更新sf状态
		User user = userMgr.findUserBySFid(userId);
		if (user == null) {
			return false;
		}
		user.setUshifenstatid(newShifenStatus);
		if (newShifenStatus == UserConstant.SHIFEN_STATE_REFUSE) {
			user
					.setSfstattransfer(UserConstant.SHIFEN_TRANSFER_STATE_REFUSE);
		} else if (newShifenStatus == UserConstant.SHIFEN_STATE_NORMAL
				|| newShifenStatus == UserConstant.SHIFEN_STATE_ZERO) {
			user
					.setSfstattransfer(UserConstant.SHIFEN_TRANSFER_STATE_NORMAL);
		}
		//----------------------->mod by liangshimu,20100916
//		userMgr.saveOrUpdateUser(user);
		userMgr.updateUserSFStatus(user);
		//------------------------>end mod
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baidu.beidou.user.service.UserNoticeService#addUser(long, int)
	 */
	public boolean addUser(long token, int userId, Map<String, String> info) {
		
		if (info == null) {
			LOG.fatal("UC通知参数错误\ttoken="+token+"\tuserId="+userId+"\tinfo="+info);
			return false;
		}
		int ulevel = 0;
		int ustatus = 0;
		String levelStr = info.get(USERLEVEL_KEY);
		String statuStr = info.get(USERSTATUS_KEY);
		String userName = info.get(USERNAME_KEY);
		if(levelStr == null || !org.apache.commons.lang.math.NumberUtils.isDigits(levelStr)
				||statuStr == null || !org.apache.commons.lang.math.NumberUtils.isDigits(statuStr) || StringUtils.isEmpty(userName)){
			LOG.fatal("UC通知参数错误\ttoken="+token+"\tuserId="+userId+"\tinfo="+info);
			return false;
		}
		ulevel = Integer.parseInt(levelStr);
		ustatus = Integer.parseInt(statuStr);
		if (ulevel == USER_LEVEL.USER_LEVEL_CLIENT.getValue()
				|| ulevel == USER_LEVEL.USER_LEVEL_NORMAL
						.getValue()) {
			return this.addUser(token, userId, userName, ustatus);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baidu.beidou.user.service.UserNoticeService#updateShifenState(long,
	 *      int)
	 */
	public boolean updateShifenState(long token, int userId, Map<String, String> info) {
		if (info == null) {
			LOG.fatal("UC通知参数错误\ttoken="+token+"\tuserId="+userId+"\tinfo="+info);
			return false;
		}
		int ulevel = 0;
		int ustatus = 0;
		String levelStr = info.get(USERLEVEL_KEY);
		String statuStr = info.get(USERSTATUS_KEY);
		if(levelStr == null || !org.apache.commons.lang.math.NumberUtils.isDigits(levelStr)
				||statuStr == null || !org.apache.commons.lang.math.NumberUtils.isDigits(statuStr)){
			LOG.fatal("UC通知参数错误\ttoken="+token+"\tuserId="+userId+"\tinfo="+info);
			return false;
		}
		ulevel = Integer.parseInt(levelStr);
		ustatus = Integer.parseInt(statuStr);
		if (ulevel == USER_LEVEL.USER_LEVEL_CLIENT.getValue()
				|| ulevel == USER_LEVEL.USER_LEVEL_NORMAL
						.getValue()) {
			return this.updateShifenState(token, userId, ustatus);
		}
		return true;
		
	}

	/**
	 * @return the userMgr
	 */
	public UserMgr getUserMgr() {
		return userMgr;
	}

	/**
	 * @param userMgr
	 *            the userMgr to set
	 */
	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

}
