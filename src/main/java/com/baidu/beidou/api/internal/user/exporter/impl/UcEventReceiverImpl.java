/**
 * @version 1.1.0
 * 2009-12-21 上午10:53:14
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.exporter.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.user.exporter.UcEventReceiver;
import com.baidu.beidou.api.internal.user.service.UserNoticeService;

/**
 * @author zengyunfeng
 *
 */
public class UcEventReceiverImpl implements UcEventReceiver {
	private static final Log LOG = LogFactory.getLog(UcEventReceiverImpl.class);
	private UserNoticeService userNoticeService ;
	
	/* (non-Javadoc)
	 * @see com.baidu.beidou.user.exporter.UcEventReceiver#notify(long, int, int, java.util.Map)
	 */
	public boolean notify(long token, int eventType, int userId,
			Map<String, String> info) {
		boolean result = false;
		switch (eventType) {
		case EVENT_ADD_USER:
			result = userNoticeService.addUser(token, userId, info);
			break;
		case EVENT_MOD_SFSTATUS:
			result = userNoticeService.updateShifenState(token, userId, info);
			break;

		default:
			LOG.error("未知的事件类型eventType="+eventType+"\ttoken="+token+"\tuserId="+userId+"\tinfo="+info);
			break;
		}
		return result;
	}
	
	/**
	 * @return the userNoticeService
	 */
	public UserNoticeService getUserNoticeService() {
		return userNoticeService;
	}
	/**
	 * @param userNoticeService the userNoticeService to set
	 */
	public void setUserNoticeService(UserNoticeService userNoticeService) {
		this.userNoticeService = userNoticeService;
	}

}
