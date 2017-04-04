package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.vo.request.GetReportIdRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiOption;
/**
 * 临时过滤 editor group层级报告或所有请求
 * 
 * @author caichao
 */
public class FilterGroupReportInterceptor implements MethodInterceptor{
	private static final Log LOG = LogFactory.getLog(FilterGroupReportInterceptor.class);
	
	//0:无效  1:有效过滤editor group层级数据  2:有效过滤editor所有请求
	private Integer editorSwitch;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		//如果开关没开，直接放行
		if (editorSwitch == 0 || editorSwitch == null) {
			return invocation.proceed();
		}
		
		LOG.info("access into the method now editor filter is open");
		
		String method = invocation.getMethod().getName();
		
		if ("getReportId".equals(method)) {
			Object[] params = invocation.getArguments();
			
			Object obj = (Object) params[0];
			int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(obj);
			int type = typeAndUser[0];
			int datauser = typeAndUser[2];
			
			if (isEditorRequest(params[2])) {
				Object result = filterReturn(editorSwitch,params[1],type,datauser);
				if (result == null) {
					return invocation.proceed();
				} else {
					return result;
				}
						
			} else {
				return invocation.proceed();
			}
			
		}
		return invocation.proceed();
	}
	
	/**
	 * 判断是否为editor 请求
	 * @param object
	 * @return
	 */
	private boolean isEditorRequest(Object object) {
		if (object instanceof ApiOption) {
			ApiOption option = (ApiOption)object;
			
			String tokenType = option.getOptions().get("tokenType");
			return "2".equals(tokenType);
		} else {
			return false;
		}
			
	}
	
	/**
	 * 过滤全部editor请求或者editor中group report请求
	 * @param editorSwitch
	 * @param obj
	 * @param type
	 * @param datauser
	 * @return
	 */
	private Object filterReturn(Integer editorSwitch,Object obj,int type,int datauser) {
		
		Object result = ApiResultBeanUtils.addApiError(type, null,
				GlobalErrorCode.SYSTEM_BUSY.getValue(),
				GlobalErrorCode.SYSTEM_BUSY.getMessage(),
				PositionConstant.SYS, null);
		//拦截editor全部请求
		if (editorSwitch == 2) {
			LOG.info("userid=[" + datauser + "] filter all editor request , return system error");
			return result;
		} else {
			if (obj instanceof GetReportIdRequest) {
				GetReportIdRequest request = (GetReportIdRequest)obj;
				if(3 == request.getReportRequestType().getReportType()) {
					LOG.info("userid=[" + datauser + "] filter  editor group report request , return system error");
					return result;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}


	public Integer getEditorSwitch() {
		return editorSwitch;
	}


	public void setEditorSwitch(Integer editorSwitch) {
		this.editorSwitch = editorSwitch;
	}
}
