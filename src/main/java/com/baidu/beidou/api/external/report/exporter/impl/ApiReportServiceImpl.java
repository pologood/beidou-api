package com.baidu.beidou.api.external.report.exporter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.constant.ReportConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.error.ReportErrorCode;
import com.baidu.beidou.api.external.report.exporter.ApiReportService;
import com.baidu.beidou.api.external.report.facade.ApiReportFacade;
import com.baidu.beidou.api.external.report.facade.TransReportFacade;
import com.baidu.beidou.api.external.report.facade.impl.ApiReportFacadeImpl;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.validator.ApiReportValidator;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.ReportRequestType;
import com.baidu.beidou.api.external.report.vo.request.GetReportFileUrlRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportIdRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportStateRequest;
import com.baidu.beidou.api.external.report.vo.response.GetReportFileUrlResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportStateResponse;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;
import com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.AsyncReportService;
import com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.response.ApiError;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * 
 * ClassName: ApiReportServiceImpl  <br>
 * Function: 报告服务类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public class ApiReportServiceImpl implements ApiReportService{
	
	private static final Log log = LogFactory.getLog(ApiReportFacadeImpl.class);
	
	private ApiReportValidator apiReportValidator;
	
	private ApiReportFacade apiReportFacade;
	
	private ApiReportTaskMgr apiReportTaskMgr;
	
	private UserMgr userMgr;
	
	private TransReportFacade transReportFacade;
	
	@Resource
	private AsyncReportService asyncReportService;
	
	/**
	 * 
	 * 获取报告reportId <br>
	 * 
	 * 主要功能为： <br>
	 * 1. 验证请求包装类参数 <br>
	 * 2. 插入数据库新的任务 <br>
	 * 3. 插入新到任务到JMS中 <br>
	 * 4. 返回reportId <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportIdResponse> getReportId(DataPrivilege user,
			GetReportIdRequest getReportIdRequest, 
			ApiOption apiOption){
		ApiResult<GetReportIdResponse> result = new ApiResult<GetReportIdResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
        result = basicUserValidate(user, result);
        if (CollectionUtil.isNotEmpty(result.getErrors())) {
            return result;
        }

        ReportRequestType request = getReportIdRequest.getReportRequestType();

        com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.response.ApiResult<String> asyncResult =
                call(user, result, request, "", 0L);

        if (CollectionUtil.isNotEmpty(result.getErrors())) {
            return result;
        }

        if (asyncResult == null || CollectionUtil.isEmpty(asyncResult.getData())) {
            return result;
        }

        String reportId = asyncResult.getData().get(0);
        GetReportIdResponse response = new GetReportIdResponse();
        response.setReportId(reportId);
        result = ApiResultBeanUtils.addApiResult(result, response);
        payment.setSuccess(1);
		
		return result;
	}

	/**
	 * 用户id校验
	 * @param user
	 * @param result
	 * @return
	 * @author 蔡超
	 * @date 2015-8-25 上午12:52:49
	 */
    private ApiResult<GetReportIdResponse> basicUserValidate(DataPrivilege user, 
            ApiResult<GetReportIdResponse> result) {
        if (user == null) {
            result =
                    ApiResultBeanUtils.addApiError(result, UserErrorCode.NO_USER.getValue(),
                            UserErrorCode.NO_USER.getMessage(), null, null);
            return result;
        }

        User dataUser = userMgr.findUserBySFid(user.getDataUser());
        if (dataUser == null) {
            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNAUTHORIZATION.getValue(),
                            GlobalErrorCode.UNAUTHORIZATION.getMessage(), PositionConstant.USER, null);
            return result;
        }
        return result;
    }
    
    /**
     * 新旧两套并行
     * 
     * @param user
     * @param result
     * @param request
     * @param reportid
     * @param id
     * @author 蔡超
     * @date 2015-8-13 下午10:55:31
     */
    private com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.response.ApiResult<String> call(DataPrivilege user,
            ApiResult<GetReportIdResponse> result, ReportRequestType request, String reportid, long id) {
        try {
            com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.response.ApiResult<String> asyncResult = null;
            com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.request.ReportRequestType req =
                    getReportRequest(request, reportid, id);
            asyncResult = asyncReportService.getReportId(user.getDataUser(), req, user.getOpUser());

            List<ApiError> errors = asyncResult.getErrors();
            if (CollectionUtil.isNotEmpty(errors)) {
                for (ApiError error : errors) {
                    log.info(error);
                    ApiResultBeanUtils.addApiError(result, error.getCode(), 
                            error.getMessage(), error.getPosition(), null);
                }
            }

            log.info("get report id success " + asyncResult.getData().get(0));
            return asyncResult;
        } catch (Exception e) {
            log.error("async get report id fail", e);
        }

        return null;
    }

    /**
     * 构建请求
     * 
     * @param request
     * @param reportId 新旧两套并行使用同一个reportid 便于对比，新的复用旧的
     * @param id 同reportid
     * @return
     * @author 蔡超
     * @date 2015-8-13 下午10:55:50
     */
    private com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.request.ReportRequestType getReportRequest(
            ReportRequestType request, String reportId, long id) {
        com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.request.ReportRequestType req =
                new com.baidu.cpweb.soma.biz.asyncreport.produce.exporter.request.ReportRequestType();
        req.setTaskId(id);
        req.setReportId(reportId);
        req.setEndDate(request.getEndDate());
        req.setFormat(request.getFormat());
        req.setIdOnly(request.isIdOnly());
        req.setPerformanceData(request.getPerformanceData());
        req.setReportType(request.getReportType());
        req.setStartDate(request.getStartDate());
        req.setStatIds(request.getStatIds());
        req.setStatRange(request.getStatRange());
        return req;
    }
	
	/**
	 * 如果开启了哈希缓存，就尝试去memcache中找对应的queryparamMD5 key的值reportId，
	 * 如果找不到则返回空字符串
	 * 如果找到来返回32位字符长度的reportId
	 * @param task 任务
	 * @return String reportId
	 */
	protected String preGetHashCache(ApiReportTask task) {
		String ret = "";
		if(ReportWebConstants.ENABLE_HASH_CACHE){
			try{
				String queryParamStr = task.getQueryparam();
				int performanceData = task.getPerformancedata(); // performanceData与queryParam结合作为key
				int isZip = task.getIszip();
				String key = MD5.getMd5(queryParamStr + performanceData + isZip);
				Object obj = BeidouCacheInstance.getInstance().memcacheRandomGet(key);
				if(obj == null || !(obj instanceof String)) {
					log.info("queryparam MD5 key=[" + key + "] cannot be found in hash cache");
					return ret;
				}
				if(((String)obj).length() != ReportWebConstants.MD5_STRING_LENGTH) {
					log.error("queryparam MD5 key=[" + key + "] value length is not correct");
					return ret;
				}
				ret = (String)obj;
				task.setReportid(ret); //重新设置task的reportId
				log.info("queryparam MD5 key=[" + key + "] target hash cache reportId=[" + ret + "]");
			} catch (Exception e){
				log.error("fail get hash cache for task=[" + task + "]" + e.getMessage(),e); 
			} 
		}
		return ret;
	}
	

	/**
	 * 
	 * 查询reportId状态 <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportStateResponse> getReportState(DataPrivilege user,
			GetReportStateRequest request, 
			ApiOption apiOption){
		ApiResult<GetReportStateResponse> result = new ApiResult<GetReportStateResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		// 如果reportId参数不合法，直接报错
		String reportId = request.getReportId();
		if(StringUtils.isEmpty(reportId) || reportId.length() != ReportWebConstants.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_STATE.POSITION_REPORTID);
			result = ApiResultBeanUtils.addApiError(
					result,ReportErrorCode.WRONG_REPORTID.getValue(),
					ReportErrorCode.WRONG_REPORTID.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 如果reportId不存在，则报错
		ApiReportTask task = apiReportTaskMgr.findTasksByReportId(reportId);
		if(task == null){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_FILEURL.POSITION_REPORTID);
			result = ApiResultBeanUtils.addApiError(
					result,ReportErrorCode.GET_REPORTSTATE_NOTFOUND.getValue(),
					ReportErrorCode.GET_REPORTSTATE_NOTFOUND.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 查询出reportId对应的task状态，并且转换为前端映射到状态，返回状态码
		int status = task.getStatus();
		int frontStatus = ApiReportTaskConstant.REPORT_STATUS_FRONT_END_MAP.get(status);
		GetReportStateResponse response = new GetReportStateResponse();
		response.setIsGenerated(frontStatus);
		
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
		
	}
	
	/**
	 * 
	 * 获取报告reportId对应到下载url
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetReportIdResponse> 包含reportId
	 */
	public ApiResult<GetReportFileUrlResponse> getReportFileUrl(DataPrivilege user,
			GetReportFileUrlRequest request, 
			ApiOption apiOption) {
		ApiResult<GetReportFileUrlResponse> result = new ApiResult<GetReportFileUrlResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		// 如果reportId参数不合法，直接报错
		String reportId = request.getReportId();
		if(StringUtils.isEmpty(reportId) || reportId.length() != ReportWebConstants.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_FILEURL.POSITION_REPORTID);
			result = ApiResultBeanUtils.addApiError(
					result,ReportErrorCode.WRONG_REPORTID.getValue(),
					ReportErrorCode.WRONG_REPORTID.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 如果task不存在，直接报错
		ApiReportTask task = apiReportTaskMgr.findTasksByReportId(reportId);
		if(task == null){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_FILEURL.POSITION_REPORTID);
			result = ApiResultBeanUtils.addApiError(
					result,ReportErrorCode.GET_FILEURL_FAIL.getValue(),
					ReportErrorCode.GET_FILEURL_FAIL.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 查询出reportId对应的task状态，并且转换为前端映射到状态，如果状态为完成才返回url
		int status = task.getStatus();
		int frontStatus = ApiReportTaskConstant.REPORT_STATUS_FRONT_END_MAP.get(status);
		if(frontStatus != ApiReportTaskConstant.FRONT_TASK_STATUS_OK){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_FILEURL.POSITION_REPORTID);
			result = ApiResultBeanUtils.addApiError(
					result,ReportErrorCode.GET_FILEURL_FAIL.getValue(),
					ReportErrorCode.GET_FILEURL_FAIL.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		GetReportFileUrlResponse response = new GetReportFileUrlResponse();
		String fileurl = ReportWebConstants.DOWNLOAD_FILE_URL + task.getReportid();
		
		if(task.getIszip() == ReportWebConstants.FORMAT_ZIP){
			fileurl += ReportWebConstants.REPORT_ZIP_SUFFIX;
		} 
		
		response.setReportFilePath(fileurl);
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
	
	protected ApiReportQueryParameter getQueryParam(int userid,  
			Date startDate, 
			Date endDate, 
			int reportType,
			int statRange,
			long[] statIds,
			boolean idonly,
			int performancedata) throws RuntimeException{
		ApiReportQueryParameter request = new ApiReportQueryParameter();
		request.setUserid(userid);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		
		// 默认为账户报告
		if(statRange == 0){
			statRange = ReportWebConstants.REPORT_RANGE.ACCOUNT;
		}
		if(statRange != ReportWebConstants.REPORT_RANGE.ACCOUNT){
			if(statIds != null && statIds.length > 0){
				List<Integer> statIdListInt = new ArrayList<Integer>();
				List<Long> statIdListLong = new ArrayList<Long>();
				for(long statId:statIds){
					statIdListInt.add(Long.valueOf(statId).intValue());
				}
				for(long statId:statIds){
					statIdListLong.add(statId);
				}
				switch(statRange){
				case ReportWebConstants.REPORT_RANGE.PLAN:
					request.setPlanIds(statIdListInt);
					break;
				case ReportWebConstants.REPORT_RANGE.GROUP:
					request.setGroupIds(statIdListInt);
					break;
				case ReportWebConstants.REPORT_RANGE.UNIT:
					request.setUnitIds(statIdListLong);
					break;
				}
			}
		}
		
		request.setReportType(reportType);
		request.setStatRange(statRange);
		request.setIdOnly(idonly);
		
		
		if (isNeedHolmes(performancedata, userid, startDate, endDate)) {
			request.setNeedTransHolmes(true);
		}
		
		if (isNeedUv(performancedata)) {
			request.setNeedUv(true);
		}
		
		return request;
	}
	
	protected boolean isNeedHolmes(int performance, Integer userId, Date from, Date to){
		int[] isNeedHolmesBin = new int[]{0,1,2,3,4};  //低1到5位表示holmes数据
		for (int i : isNeedHolmesBin) {
			if (((performance >> i)&1) == 1) {
				try {
					// 判断是否需要查询holmes数据，如果没有启动，尽管传入来参数仍然查询不出
					boolean needToFetchTransData = transReportFacade.needToFetchTransData(userId, from, to, false);
					if (needToFetchTransData) {
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					log.error("Error to get needToFetchTransData from holmes " + e.getMessage());
				}
				// 访问holmes失败时返回false
				return false;
			}
		}
		return false;
	}
	
	protected boolean isNeedUv(int performance){
		int[] isNeedHolmesBin = new int[]{5,6,7,8,9};  //低6到10位表示uv数据
		for (int i : isNeedHolmesBin) {
			if (((performance >> i)&1) == 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 绩效指标转换
	 * @param performanceData 绩效指标数组
	 * @return int 绩效数据的十进制数字，需要后端模块转义为二进制，例如110101，从高位到低位代表展现、点击、消费、CTR、CPM、ACP
	 */
	protected int transferPerformanceData(String[] performanceData){
		// 绩效数据
		int performance = 0;
		if (performanceData == null || performanceData.length < 1) {
			performance += (1 << 15);
			performance += (1 << 14);
			performance += (1 << 13);
			performance += (1 << 12);
			performance += (1 << 11);
			performance += (1 << 10);
		} else {
			for (String data : performanceData) {
				if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRCH)) {
					if (((performance >> 15)&1)  != 1) {
						performance += (1 << 15);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CLICK)) {
					if (((performance >> 14)&1) != 1) {
						performance += (1 << 14);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_COST)) {
					if (((performance >> 13)&1) != 1) {
						performance += (1 << 13);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CTR)) {
					if (((performance >> 12)&1) != 1) {
						performance += (1 << 12);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CPM)) {
					if (((performance >> 11)&1) != 1) {
						performance += (1 << 11);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_ACP)) {
					if (((performance >> 10)&1) != 1) {
						performance += (1 << 10);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRCHUV)) {
					if (((performance >> 9)&1)  != 1) {
						performance += (1 << 9);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CLICKUV)) {
					if (((performance >> 8)&1) != 1) {
						performance += (1 << 8);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRSUR)) {
					if (((performance >> 7)&1) != 1) {
						performance += (1 << 7);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CUSUR)) {
					if (((performance >> 6)&1) != 1) {
						performance += (1 << 6);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_COCUR)) {
					if (((performance >> 5)&1) != 1) {
						performance += (1 << 5);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_ARRIVAL_RATE )) {
					if (((performance >> 4)&1) != 1) {
						performance += (1 << 4);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_HOP_RATE)) {
					if (((performance >> 3)&1) != 1) {
						performance += (1 << 3);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_RES_TIME)) {
					if (((performance >> 2)&1) != 1) {
						performance += (1 << 2);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_DIRECT_TRANS_CNT)) {
					if (((performance >> 1)&1) != 1) {
						performance += (1 << 1);
					}
				} else if (data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_INDIRECT_TRANS_CNT )) {
					if (((performance >> 0)&1) != 1) {
						performance += (1 << 0);
					}
				}
			}
		}
		return performance;
	}

	public ApiReportFacade getApiReportFacade() {
		return apiReportFacade;
	}

	public void setApiReportFacade(ApiReportFacade apiReportFacade) {
		this.apiReportFacade = apiReportFacade;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public ApiReportValidator getApiReportValidator() {
		return apiReportValidator;
	}

	public void setApiReportValidator(ApiReportValidator apiReportValidator) {
		this.apiReportValidator = apiReportValidator;
	}

	public ApiReportTaskMgr getApiReportTaskMgr() {
		return apiReportTaskMgr;
	}

	public void setApiReportTaskMgr(ApiReportTaskMgr apiReportTaskMgr) {
		this.apiReportTaskMgr = apiReportTaskMgr;
	}

	public TransReportFacade getTransReportFacade() {
		return transReportFacade;
	}

	public void setTransReportFacade(TransReportFacade transReportFacade) {
		this.transReportFacade = transReportFacade;
	}
	
}
