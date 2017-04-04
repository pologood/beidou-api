package com.baidu.beidou.api.internal.audit.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ReauditUserInfo;
import com.baidu.beidou.api.internal.audit.vo.UserInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;

/**
 * ClassName: UserAuditService
 * Function: 北斗内部API，用户相关Service
 *
 * @author genglei
 * @version cpweb-567
 * @date Jun 23, 2013
 */
public interface UserAuditService {
	
	/**
	 * getUserInfo: 获取用户信息，包含userId、userName、userState等
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<UserInfo> getUserInfo(List<Integer> userIds);
	
	/**
	 * getReauditUserList: 获取重审用户列表信息
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<ReauditUserInfo> getReauditUserList(QueryBase query);
	
}
