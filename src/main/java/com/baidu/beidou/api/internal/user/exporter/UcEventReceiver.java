/**
 * @version 1.1.0
 * 2009-12-15 下午09:28:12
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.exporter;

import java.util.Map;

/**
 * @author zengyunfeng
 * 
 */
public interface UcEventReceiver {

	public static int EVENT_ADD_USER = 1;
	public static int EVENT_MOD_SFSTATUS = 2;
	
	/**
	 * @param token
	 *            本次操作token序列号
	 * @param eventType
	 *            事件类型:    1: 添加用户事件;  2: 用户的shifen状态通知; 
	 * @param userId
	 *            该事件的用户id
	 * @param info
	 *            事件元数据, 如用户uid
	 * @return
	 */
	public boolean notify(long token, int eventType, int userId,
			Map<String, String> info);

}
