package com.baidu.beidou.api.external.accountfile.exporter.impl;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileConstant;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.constant.ApiAccountFileTaskConstant;
import com.baidu.beidou.api.external.accountfile.error.AccountFileErrorCode;
import com.baidu.beidou.api.external.accountfile.exporter.AccountFileService;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileFacade;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.api.external.accountfile.util.AccountFileUtil;
import com.baidu.beidou.api.external.accountfile.vo.AccountFileRequestType;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileIdRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileStateRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileUrlRequest;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileStateResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileUrlResponse;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.HostnameConfig;
import com.baidu.beidou.api.external.util.UUIDGenerator;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: AccountFileServiceImpl  <br>
 * Function: 获取账户或者指定推广计划下的完整数据
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountFileServiceImpl implements AccountFileService{
	
	private UserMgr userMgr;
	
	private AccountFileFacade accountFileFacade;
	
	private ApiAccountFileTaskMgr apiAccountFileTaskMgr;
	
	/**
	 * 
	 * 获取账户或者指定推广计划下的完整数据fileId <br>
	 * 
	 * 主要功能为： <br>
	 * 1. 验证请求包装类参数 <br>
	 * 2. 插入数据库新的任务 <br>
	 * 3. 插入新到任务到JMS中 <br>
	 * 4. 返回fileId <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileIdResponse> 包含fileId
	 */
	public ApiResult<GetAccountFileIdResponse> getAccountFileId(DataPrivilege user,
			GetAccountFileIdRequest request, 
			ApiOption apiOption){
		ApiResult<GetAccountFileIdResponse> result = new ApiResult<GetAccountFileIdResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		User dataUser = userMgr.findUserBySFid(user.getDataUser());
		if (dataUser == null) {
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNAUTHORIZATION.getValue(),
					GlobalErrorCode.UNAUTHORIZATION.getMessage(),
					PositionConstant.USER, 
					null);
			return result;
		}
		
		// 验证报告格式参数，如果有错误直接返回
		AccountFileRequestType type = request.getAccountFileRequestType();
		int format = type.getFormat();
		if (format != AccountFileWebConstants.FORMAT_GZIP && format != AccountFileWebConstants.FORMAT_ZIP) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_ID.POSITION_FORMAT);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.ACCOUNTFILE_FORMAT_WRONG.getValue(),
					AccountFileErrorCode.ACCOUNTFILE_FORMAT_WRONG.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 准备ApiReportTask对象
		String toDbPlanIds = "";
		long[] planIds = type.getCampaignIds();
		if(planIds != null && planIds.length > 0){
			Long[] objectPlanIds = ArrayUtils.toObject(planIds);
			toDbPlanIds = StringUtils.join(objectPlanIds, AccountFileWebConstants.ACCOUNTFILE_PLANID_SEPERATOR);
		} 
		String hostname = HostnameConfig.getHOSTNAME();
		int userid = user.getDataUser();
		int opuser = user.getOpUser();
		String strVersion = request.getVersion();
		
		// 填充到task
		Date now = new Date();
		ApiAccountFileTask task = new ApiAccountFileTask();
		task.setUserid(userid);
		task.setPlanids(toDbPlanIds);
		task.setFormat(format);
		task.setOpuser(opuser);
		task.setMachineid(hostname);
		task.setFileid(UUIDGenerator.get(userid + toDbPlanIds + now));  // 生成fileId的策略
		task.setMd5(""); // md5先为空
		task.setStatus(ApiAccountFileTaskConstant.TASK_STATUS_NEW);
		task.setAddtime(now);
		task.setModtime(now);
        if (null != strVersion) {
            task.setVersion(Double.parseDouble(strVersion));
        }
		
		// 将task保存到数据库中
		ApiAccountFileTask savedTask = apiAccountFileTaskMgr.addAccountFileTask(task);
		
		// 向JMS中插入异步处理任务
		accountFileFacade.addTask(task);
		
		// 返回reportId
		GetAccountFileIdResponse response = new GetAccountFileIdResponse();
		response.setFileId(savedTask.getFileid());
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
	/**
	 * 
	 * 查询fileId状态 <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileStateResponse> 包含fileId对应数据文件生成状态
	 */
	public ApiResult<GetAccountFileStateResponse> getAccountFileState(DataPrivilege user,
			GetAccountFileStateRequest request, 
			ApiOption apiOption){
		ApiResult<GetAccountFileStateResponse> result = new ApiResult<GetAccountFileStateResponse>();
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
		String fileId = request.getFileId();
		if(StringUtils.isEmpty(fileId) || fileId.length() != AccountFileWebConstants.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_STATE.POSITION_ACCOUNTFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.FILEID_FORMAT_WRONG.getValue(),
					AccountFileErrorCode.FILEID_FORMAT_WRONG.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 如果fileId不存在，则报错
		List<ApiAccountFileTask> task = apiAccountFileTaskMgr.findByFileId(fileId);
		if(task == null || task.size() == 0 ){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_STATE.POSITION_ACCOUNTFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.FILEID_NOT_EXIST.getValue(),
					AccountFileErrorCode.FILEID_NOT_EXIST.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 查询出reportId对应的task状态，并且转换为前端映射到状态，返回状态码
		int status = task.get(0).getStatus();
		int frontStatus = ApiAccountFileTaskConstant.ACCOUNTFILE_STATUS_FRONT_END_MAP.get(status);
		GetAccountFileStateResponse response = new GetAccountFileStateResponse();
		response.setIsGenerated(frontStatus);
		
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
	/**
	 * 
	 * 获取报告fileId对应到下载url
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileUrlResponse> 包含fileId
	 */
	public ApiResult<GetAccountFileUrlResponse> getAccountFileUrl(DataPrivilege user,
			GetAccountFileUrlRequest request, 
			ApiOption apiOption){
		ApiResult<GetAccountFileUrlResponse> result = new ApiResult<GetAccountFileUrlResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		// 如果fileId参数不合法，直接报错
		String fileId = request.getFileId();
		if(StringUtils.isEmpty(fileId) || fileId.length() != AccountFileWebConstants.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_FILEURL.POSITION_ACCOUNTFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.FILEID_FORMAT_WRONG.getValue(),
					AccountFileErrorCode.FILEID_FORMAT_WRONG.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 如果fileId不存在，则报错
		List<ApiAccountFileTask> task = apiAccountFileTaskMgr.findByFileId(fileId);
		if(task == null || task.size() == 0 ){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_STATE.POSITION_ACCOUNTFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.FILEID_NOT_EXIST.getValue(),
					AccountFileErrorCode.FILEID_NOT_EXIST.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		// 查询出reportId对应的task状态，并且转换为前端映射到状态，返回状态码
		ApiAccountFileTask t = task.get(0);
		int status = t.getStatus();
		int frontStatus = ApiAccountFileTaskConstant.ACCOUNTFILE_STATUS_FRONT_END_MAP.get(status);
		if(frontStatus != ApiAccountFileTaskConstant.FRONT_TASK_STATUS_OK){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AccountFileConstant.POSITION_GET_ACCOUNTFILE_FILEURL.POSITION_ACCOUNTFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,AccountFileErrorCode.ACCOUNTFILE_NOT_EXIST.getValue(),
					AccountFileErrorCode.ACCOUNTFILE_NOT_EXIST.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		GetAccountFileUrlResponse response = new GetAccountFileUrlResponse();
		String fileurl = AccountFileWebConstants.DOWNLOAD_FILE_URL;
				
		if(t.getFormat() == AccountFileWebConstants.FORMAT_ZIP){
			fileurl += AccountFileUtil.getDownloadFilePath(t.getAddtime(), fileId, AccountFileWebConstants.ACCOUNTFILE_ZIP_SUFFIX);
		} else if(t.getFormat() == AccountFileWebConstants.FORMAT_GZIP){
			fileurl += AccountFileUtil.getDownloadFilePath(t.getAddtime(), fileId, AccountFileWebConstants.ACCOUNTFILE_GZIP_SUFFIX);
		}
		
		response.setFilePath(fileurl);
		response.setMd5(t.getMd5());
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public ApiAccountFileTaskMgr getApiAccountFileTaskMgr() {
		return apiAccountFileTaskMgr;
	}

	public void setApiAccountFileTaskMgr(ApiAccountFileTaskMgr apiAccountFileTaskMgr) {
		this.apiAccountFileTaskMgr = apiAccountFileTaskMgr;
	}

	public AccountFileFacade getAccountFileFacade() {
		return accountFileFacade;
	}

	public void setAccountFileFacade(AccountFileFacade accountFileFacade) {
		this.accountFileFacade = accountFileFacade;
	}
	

}
