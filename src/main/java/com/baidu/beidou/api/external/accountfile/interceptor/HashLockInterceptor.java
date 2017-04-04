package com.baidu.beidou.api.external.accountfile.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileIdRequest;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.interceptor.AbstractDataPrivilege;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

/**
 * 
 * ClassName: LoadControlInterceptor  <br>
 * Function: 作负载限制的拦截器，叫做throttle（节流阀）
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 16, 2012
 */
public class HashLockInterceptor extends AbstractDataPrivilege{
	
	private static final Log LOG = LogFactory.getLog(HashLockInterceptor.class);
	
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ApiResult<GetAccountFileIdResponse> result = null;
		try {
			String ret = "";
			if(ReportWebConstants.ENABLE_HASH_CACHE){
				result = new ApiResult<GetAccountFileIdResponse>();
				Object[] params = invocation.getArguments();
				DataPrivilege user = (DataPrivilege) params[0];
				GetAccountFileIdRequest request = (GetAccountFileIdRequest) params[1];
				int userId = user.getDataUser();
				String toDbPlanIds = "";
				long[] planIds = request.getAccountFileRequestType().getCampaignIds();
				int format = request.getAccountFileRequestType().getFormat();
				if(planIds != null && planIds.length > 0){
					Long[] objectPlanIds = ArrayUtils.toObject(planIds);
					toDbPlanIds = StringUtils.join(objectPlanIds, AccountFileWebConstants.ACCOUNTFILE_PLANID_SEPERATOR);
				} 
				String keyStr = Integer.toString(userId) + AccountFileWebConstants.ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR + toDbPlanIds + AccountFileWebConstants.ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR + Integer.toString(format);
				String keyMd5 = MD5.getMd5(keyStr);
				try{
					Object obj = BeidouCacheInstance.getInstance().memcacheRandomGet(keyMd5);
					if(obj == null || !(obj instanceof String)) {
						LOG.info("queryParam=[" + keyStr + "], md5=[" + keyMd5 + "] not locked");
					} else if(((String)obj).length() != AccountFileWebConstants.MD5_STRING_LENGTH) {
						LOG.error("queryParam=[" + keyStr + "] value length is not correct");
					} else {
						ret = (String)obj;
						GetAccountFileIdResponse response = new GetAccountFileIdResponse();
						response.setFileId(ret);
						result = ApiResultBeanUtils.addApiResult(result, response);
						PaymentResult payment = new PaymentResult();
						payment.setTotal(1);
						payment.setSuccess(1);
						result.setPayment(payment);
						LOG.info("queryParam=[" + keyStr + "], md5=[" + keyMd5 + "] has been locked with fileId=[" + ret + "]");
						return result;
					}
				} catch (Exception e){
					LOG.error("fail get hash lock for key=[" + keyStr + "]" + e.getMessage(),e); 
				} 
			}
			
			result = (ApiResult<GetAccountFileIdResponse>)(invocation.proceed());
			
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(null, GlobalErrorCode.SYSTEM_BUSY.getValue(), 
					GlobalErrorCode.SYSTEM_BUSY.getMessage(), PositionConstant.SYS,null);
		}
		return result;
	}


	

}

