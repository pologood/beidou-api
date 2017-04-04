package com.baidu.beidou.api.external;

import java.util.HashMap;
import java.util.Map;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;

/**
 * 
 * ClassName: DarwinApiHelper
 * Function: 单测用DR-API帮助类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 21, 2011
 */
public class DarwinApiHelper {
	
	public static final String APIOPTION_TOKENID = "tokenId";
	public static final String APIOPTION_TOKENTYPE = "tokenType";
	public static final String APIOPTION_REMOTEIP = "remoteIp";
	public static final String APIOPTION_LOGID = "logId";
	public static final String APIOPTION_FROMSERVER = "fromServer";
	
	public static final String DEFAULT_APIOPTION_TOKENID = "abcd1234";
	public static final String DEFAULT_APIOPTION_TOKENTYPE = "2";
	public static final String DEFAULT_APIOPTION_REMOTEIP = "10.0.0.1";
	public static final String DEFAULT_APIOPTION_LOGID = "5";
	public static final String DEFAULT_APIOPTION_FROMSERVER = "tc-dr-api00.tc";
	
	/**
	 * 返回DR-API传递过来的冗余信息，默认返回 <p>
	 * 其中options里面目前包括如下数据： <br>
	 * tokenId：API权限代码（token）的Id <br>
	 * tokenType：API权限代码（token）的类型（0 普通；1 SEM；2 EDITOR； 3 内部）<br>
	 * remoteIp：API请求来源IP <br>
	 * logId：API服务器日志序列号 <br>
	 * fromServer：API服务器名 <br>
	 *	挂接方可以利用这些信息来做审计、授权，以及反查DR-API日志。 <br>
	 */
	public static ApiOption getApiOptions(){
		ApiOption option = new ApiOption();
		Map<String, String> result = new HashMap<String, String>();
		result.put(APIOPTION_TOKENID, DEFAULT_APIOPTION_TOKENID);
		result.put(APIOPTION_TOKENTYPE, DEFAULT_APIOPTION_TOKENTYPE);
		result.put(APIOPTION_REMOTEIP, DEFAULT_APIOPTION_REMOTEIP);
		result.put(APIOPTION_LOGID, DEFAULT_APIOPTION_LOGID);
		result.put(APIOPTION_FROMSERVER, DEFAULT_APIOPTION_FROMSERVER);
		option.setOptions(result);
		return option;
	}
	
	public static BaseRequestOptions getBaseRequestOptions(){
		BaseRequestOptions options = new BaseRequestOptions();
		Map<String, String> result = new HashMap<String, String>();
		result.put(APIOPTION_TOKENID, DEFAULT_APIOPTION_TOKENID);
		result.put(APIOPTION_TOKENTYPE, DEFAULT_APIOPTION_TOKENTYPE);
		result.put(APIOPTION_REMOTEIP, DEFAULT_APIOPTION_REMOTEIP);
		result.put(APIOPTION_LOGID, DEFAULT_APIOPTION_LOGID);
		result.put(APIOPTION_FROMSERVER, DEFAULT_APIOPTION_FROMSERVER);
		options.setOptions(result);
		return options;
	}
	
	/**
	 * 返回DR-API传递过来的冗余信息，自定义返回 <p>
	 * 其中options里面目前包括如下数据： <br>
	 * tokenId：API权限代码（token）的Id <br>
	 * tokenType：API权限代码（token）的类型（0 普通；1 SEM；2 EDITOR； 3 内部）<br>
	 * remoteIp：API请求来源IP <br>
	 * logId：API服务器日志序列号 <br>
	 * fromServer：API服务器名 <br>
	 *	挂接方可以利用这些信息来做审计、授权，以及反查DR-API日志。 <br>
	 */
	public static ApiOption getApiOptions(String tokenId, String tokenType, String remoteIp, String logId, String fromServer){
		ApiOption option = new ApiOption();
		Map<String, String> result = new HashMap<String, String>();
		result.put(APIOPTION_TOKENID, tokenId);
		result.put(APIOPTION_TOKENTYPE, tokenType);
		result.put(APIOPTION_REMOTEIP, remoteIp);
		result.put(APIOPTION_LOGID, logId);
		result.put(APIOPTION_FROMSERVER, fromServer);
		option.setOptions(result);
		return option;
	}
	
	/**
	 * 返回DR-API传递过来的用户信息
	 */
	public static DataPrivilege getDataPrivilege(int dataUser, int opUser){
		DataPrivilege result = new DataPrivilege();
		result.setDataUser(dataUser);
		result.setOpUser(opUser);
		initThreadContext(null, dataUser, opUser);
		return result;
	}
	
	public static DataPrivilege getDataPrivilege(UserMgr userMgr, int dataUser, int opUser){
		DataPrivilege result = new DataPrivilege();
		result.setDataUser(dataUser);
		result.setOpUser(opUser);
		initThreadContext(userMgr, dataUser, opUser);
		return result;
	}
	
	public static BaseRequestUser getBaseRequestUser(int dataUser, int opUser){
		BaseRequestUser result = new BaseRequestUser();
		result.setDataUser(dataUser);
		result.setOpUser(opUser);
		initThreadContext(null, dataUser, opUser);
		return result;
	}
	
	public static BaseRequestUser getBaseRequestUser(UserMgr userMgr, int dataUser, int opUser){
		BaseRequestUser result = new BaseRequestUser();
		result.setDataUser(dataUser);
		result.setOpUser(opUser);
		initThreadContext(userMgr, dataUser, opUser);
		return result;
	}
	
	private static void initThreadContext(UserMgr userMgr, int dataUser, int opUser) {
		User datauser = null;
		if (userMgr == null) {
			datauser = new User();
		} else {
			datauser = userMgr.findUserBySFid(dataUser);
		}
		datauser.setUserid(dataUser);
		SessionHolder.getSession().put(ApiConstant.KEY_SESSION_USER, datauser);
		Visitor visitor = new Visitor();
		visitor.setUserid(opUser);
		SessionHolder.getSession().put(SessionHolder.VISITOR_KEY, visitor);
		SessionHolder.getSession().put(SessionHolder.USER_KEY, datauser);
		ThreadContext.putVisitor(visitor);
		SessionHolder.putUserId(dataUser);
		
	}
	
	
	/**
	 * setSession: 构造一个虚构的Visitor
	 * @version DarwinApiHelper
	 * @author genglei01
	 * @date 2011-12-23
	 */
	public static void setVisitorSession(int userId) {
		Visitor visitor = new Visitor();
		visitor.setUserid(userId);
		visitor.setIp("api");
		if (SessionHolder.getSession() == null) {
			ThreadContext.init();
		}
		SessionHolder.getSession().put(SessionHolder.VISITOR_KEY, visitor);
	}
	
	/**
	 * setUserSession: 构造一个虚构的User
	 * @version DarwinApiHelper
	 * @author genglei01
	 * @date 2012-1-9
	 */
	public static void setUserSession(int userId) {
		User user = new User();
		user.setUserid(userId);
		if (SessionHolder.getSession() == null) {
			ThreadContext.init();
		}
		SessionHolder.getSession().put(ApiConstant.KEY_SESSION_USER, user);
	}
}
