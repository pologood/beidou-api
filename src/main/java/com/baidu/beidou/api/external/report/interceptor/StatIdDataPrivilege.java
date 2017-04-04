package com.baidu.beidou.api.external.report.interceptor;

import java.util.ArrayList;
import java.util.List;
import ognl.Ognl;
import ognl.OgnlException;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.user.service.UserMgr;

/**
 * 
 * ClassName: StatIdDataPrivilege  <br>
 * Function: 查询范围设置statId的权限验证拦截器 <br>
 * Note：但是这个拦截器只是获取来底层的excepion来做总的处理，具体的权限验证由后端到业务逻辑判断
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 7, 2012
 */
public class StatIdDataPrivilege extends AbstractDataPrivilege{
	
	private static final Log LOG = LogFactory.getLog(StatIdDataPrivilege.class);
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result;
		try {
			//处理
			result = invocation.proceed();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(null, GlobalErrorCode.SYSTEM_BUSY.getValue(), 
					GlobalErrorCode.SYSTEM_BUSY.getMessage(), PositionConstant.SYS,null);
		}
		return result;
	}



}
