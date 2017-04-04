package com.baidu.beidou.api.external.util.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.constant.UserConstant;
import com.baidu.beidou.api.external.user.constant.RolePrivilegeConfig;
import com.baidu.beidou.cprogroup.util.ITUtils;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeidouCoreConstant;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.dao.datasource.DynamicDataSourceInterceptor;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;

/**
 * ClassName: PrivilegeInterceptor
 * Function: 用户权限验证
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class PrivilegeInterceptor implements MethodInterceptor {
	private static final Log LOG = LogFactory.getLog(PrivilegeInterceptor.class);
	private static final Log ACCESS_LOG = LogFactory.getLog("apiaccess");
	private UserMgr userMgr = null;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 第一个拦截器，初始化线程上下文
		ThreadContext.init();
		
		// 权限判断
		Object result;
		Object[] params = invocation.getArguments();
		StringBuilder accessLog = new StringBuilder();
		String interfaceName = invocation.getMethod().getDeclaringClass()
				.getName();
		accessLog.append(interfaceName).append('\t').append(
				invocation.getMethod().getName()).append('\t');
		if (params == null || params.length < 1
				|| !((params[0] instanceof DataPrivilege) || (params[0] instanceof BaseRequestUser))) {
			LOG.warn("UNEXPECTED_PARAMETER " + params);
			ACCESS_LOG.info(accessLog.append(405).toString()); // 405: 不允许的方法
			return ApiResultBeanUtils.addApiError(null,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					PositionConstant.NOPARAM, null);
		}
		Object obj = (Object) params[0];
		int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(obj);
		int type = typeAndUser[0];
		int opuser = typeAndUser[1];
		int datauser = typeAndUser[2];
		
		SessionHolder.putUserId(datauser);
		
        // fixbug：为了区分不同的操作记录类型
        this.setOptHistoryType(params);
		
		accessLog.append(JSONUtil.serialize(obj)).append("\t");
		// 角色权限，数据权限
		boolean hasPrivilege = userMgr.hasDataPrivilege(
				RolePrivilegeConfig.OPTYPE_AD, opuser, datauser);
		if (!hasPrivilege) {
			LOG.warn("UNAUTHORIZATION, opuser=" + opuser + ", datauser=" + datauser);
			ACCESS_LOG.info(accessLog.append(403).toString()); // 403: 禁止
			return ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(),
					PositionConstant.USER, null);
		}
		// 设置数据用户的信息
		User dataUser = userMgr.findUserBySFid(datauser);
		if (dataUser == null) {
			LOG.warn("UNAUTHORIZATION " + datauser);
			ACCESS_LOG.info(accessLog.append(403).toString()); // 403: 禁止
			return ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(),
					PositionConstant.USER, null);
		}
		SessionHolder.getSession().put(ApiConstant.KEY_SESSION_USER, dataUser);
		// 设置操作用户
		Visitor visitor = userMgr.getUserByUserId(opuser);
		
		if (visitor == null) {
			/** 支持MCC 2013/05/31
			LOG.warn("UNAUTHORIZATION " + opuser);
			ACCESS_LOG.info(accessLog.append(403).toString()); // 403: 禁止
			SessionHolder.getSession().clear();
			return ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(),
					PositionConstant.USER, null);
					*/
			visitor = new Visitor();
			visitor.setUserid(opuser);
			String[] roles = new String[]{UserConstant.USER_ROLE_MCC};
			Set<String> userRole = new HashSet<String>();
			CollectionUtils.addAll(userRole, roles);
			visitor.setRoles(userRole);
		}
		
		updateVisitor(visitor);
		ThreadContext.putVisitor(visitor);
		SessionHolder.getSession().put(SessionHolder.VISITOR_KEY, visitor);
		SessionHolder.getSession().put(SessionHolder.USER_KEY, dataUser);
		
		LOG.info(interfaceName + "#" + invocation.getMethod().getName() + ",OpUser=" + visitor.getUserid() + "|" + visitor.getUsername() + ",DataUser=" + dataUser.getUserid() + "|" + dataUser.getUsername());
		long start = System.currentTimeMillis();
		try {
			// 处理
			result = invocation.proceed();
			//ACCESS_LOG.info(accessLog.append(200).toString()); // 200:OK
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ACCESS_LOG.info(accessLog.append(500).toString()); // 500 内部服务器错误
			result = ApiResultBeanUtils.addApiError(type, null,
					GlobalErrorCode.SYSTEM_BUSY.getValue(),
					GlobalErrorCode.SYSTEM_BUSY.getMessage(),
					PositionConstant.SYS, null);
		} finally {
			try {
				LOG.info(ITUtils.connect(invocation.getMethod().getName(), " executed ", ThreadContext.getContext(DynamicDataSourceInterceptor.DB_ACCESS_TIMES), " times of query, and cost ", (System.currentTimeMillis() - start), " millis"));
			} catch (Exception e) {
				LOG.error("Error to print executed query evaluation log");
			}
			ThreadContext.clean();
        }
		return result;
	}

    /**
     * Function: setOptHistoryType，设置操作历史来源
     * 
     * @author genglei01
     * @param params params
     */
    private void setOptHistoryType(Object[] params) {
        Object obj = (Object) params[0];
        String ip = "";
        String tokenType = "";
        if (obj instanceof DataPrivilege) {
            ApiOption apioption = (ApiOption) params[2];
            ip = apioption.getOptions().get(ApiConstant.CLIENT_IP_FROM_APIOPTION);
            tokenType = apioption.getOptions().get(ApiConstant.CLIENT_TYPE_FROM_APIOPTION);
        } else {
            BaseRequestOptions apioption = (BaseRequestOptions) params[2];
            ip = apioption.getOptions().get(ApiConstant.CLIENT_IP_FROM_APIOPTION);
            tokenType = apioption.getOptions().get(ApiConstant.CLIENT_TYPE_FROM_APIOPTION);
        }

        if (StringUtils.isEmpty(ip)) {
            ip = "API"; // 如果ip为空就为API
        }

        int clientType = 0; // 默认访问来源就是api=0
        try {
            int tokenTypeInt = Integer.valueOf(tokenType);
            clientType = ApiConstant.DRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP.get(tokenTypeInt);
        } catch (Exception e) {
            LOG.warn("Error occurred converting DRApi to beidou client type ", e);
        }
        ThreadContext.putContext(BeidouCoreConstant.OPT_HISTORY_TYPE_KEY, clientType);
    }

	/**
	 * 设置ip为api
	 * 
	 * @param visitor
	 */
	private void updateVisitor(Visitor visitor) {
		visitor.setIp("api");
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
