/**
 * 2009-12-14 下午08:29:44
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.service;

import java.util.Map;

/**
 * 接受用户变化的通知接口
 * 所有的请求需要记录日志
 * 如果调用接口发生异常，需要发送邮件报警
 * @author zengyunfeng
 * 
 */
public interface UserNoticeService {
	
	
	/**
	 * 用户shifen状态通知<br>
	 * TODO:
	 * 更新useraccount中的ushifenstatid字段，
	 * 并根据新的ushifenstatid值判断是否需要更新sfstattransfer
	 * (如果ushifenstatid=被拒绝时，则sfstattransfer=1；如果ushifenstatid=正常生效或者账面为0，则sfstattransfer=0；其他值不变化)。
	 * 更新审核员缓存。
	 * @version 1.2.18
	 * @author zengyunfeng
	 * @param token 本次操作的token，用于去重
	 * @param userId 用户id
	 * @param info 包含用户状态，ulevel
	 */
	public boolean updateShifenState(long token, int userId, Map<String, String> info);
	
	/**
	 * 新增外部客户通知<br>
	 * 注意需要是外部的广告主客户新增，即ulevelid为10101和10104的用户
	 * TODO:
	 * useraccount中插入数据，如果已经存在，则更新。
	 * @version 1.2.18
	 * @author zengyunfeng
	 * @param token
	 * @param userId
	 * @param info 包含用户名，用户状态，ulevel
	 */
	public boolean addUser(long token, int userId, Map<String, String> info);
	
}
