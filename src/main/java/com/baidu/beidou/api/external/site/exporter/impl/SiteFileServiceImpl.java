package com.baidu.beidou.api.external.site.exporter.impl;

import org.apache.commons.lang.StringUtils;

import com.baidu.beidou.api.external.site.constant.SiteFileConstant;
import com.baidu.beidou.api.external.site.error.SiteFileErrorCode;
import com.baidu.beidou.api.external.site.exporter.SiteFileService;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileIdRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileStateRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileUrlRequest;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileIdResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileStateResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileUrlResponse;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;

public class SiteFileServiceImpl implements SiteFileService {
	/**
	 * 
	 * 获取站点完整数据fileId
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileIdResponse> getSiteFileId(DataPrivilege user,
			GetSiteFileIdRequest request, 
			ApiOption apiOption) {
		ApiResult<GetSiteFileIdResponse> result = new ApiResult<GetSiteFileIdResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
//		User dataUser = userMgr.findUserBySFid(user.getDataUser());
//		if (dataUser == null) {
//			result = ApiResultBeanUtils.addApiError(result,
//					GlobalErrorCode.UNAUTHORIZATION.getValue(),
//					GlobalErrorCode.UNAUTHORIZATION.getMessage(),
//					PositionConstant.USER, 
//					null);
//			return result;
//		}
		
//		SiteFileRequestType type = request.getSiteFileRequestType();
		
		// 返回fileId
		GetSiteFileIdResponse response = new GetSiteFileIdResponse();
		response.setFileId(SiteFileConstant.SITE_FILE_ID);
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
	/**
	 * 
	 * 查询fileId状态 
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileStateResponse> getSiteFileState(DataPrivilege user,
			GetSiteFileStateRequest request, 
			ApiOption apiOption) {
		ApiResult<GetSiteFileStateResponse> result = new ApiResult<GetSiteFileStateResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		String fileId = request.getFileId();
		if(StringUtils.isEmpty(fileId) || fileId.length() != SiteFileConstant.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(SiteFileConstant.POSITION_GET_SITEFILE_STATE.POSITION_SITEFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,SiteFileErrorCode.FILEID_FORMAT_WRONG.getValue(),
					SiteFileErrorCode.FILEID_FORMAT_WRONG.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		if(!SiteFileConstant.SITE_FILE_ID.equals(fileId)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(SiteFileConstant.POSITION_GET_SITEFILE_STATE.POSITION_SITEFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,SiteFileErrorCode.FILEID_NOT_EXIST.getValue(),
					SiteFileErrorCode.FILEID_NOT_EXIST.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		GetSiteFileStateResponse response = new GetSiteFileStateResponse();
		if(SiteFileConstant.IS_GENERATE_GOING == true){
			response.setIsGenerated(SiteFileConstant.TASK_STATUS_DOING);
		} else {
			response.setIsGenerated(SiteFileConstant.TASK_STATUS_OK);
		}
		
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
	/**
	 * 
	 * 获取站点fileId对应的下载url
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileUrlResponse> getSiteFileUrl(DataPrivilege user,
			GetSiteFileUrlRequest request, 
			ApiOption apiOption) {
		ApiResult<GetSiteFileUrlResponse> result = new ApiResult<GetSiteFileUrlResponse>();
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		result.setPayment(payment);
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		String fileId = request.getFileId();
		if(StringUtils.isEmpty(fileId) || fileId.length() != SiteFileConstant.MD5_STRING_LENGTH){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(SiteFileConstant.POSITION_GET_SITEFILE_STATE.POSITION_SITEFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,SiteFileErrorCode.FILEID_FORMAT_WRONG.getValue(),
					SiteFileErrorCode.FILEID_FORMAT_WRONG.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		if(!SiteFileConstant.SITE_FILE_ID.equals(fileId)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(SiteFileConstant.POSITION_GET_SITEFILE_STATE.POSITION_SITEFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,SiteFileErrorCode.FILEID_NOT_EXIST.getValue(),
					SiteFileErrorCode.FILEID_NOT_EXIST.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		if(SiteFileConstant.IS_GENERATE_GOING == true){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(SiteFileConstant.POSITION_GET_SITEFILE_STATE.POSITION_SITEFILEID);
			result = ApiResultBeanUtils.addApiError(
					result,SiteFileErrorCode.FILE_UNABLE_TO_DOWNLOAD.getValue(),
					SiteFileErrorCode.FILE_UNABLE_TO_DOWNLOAD.getMessage(),
					apiPosition.getPosition(),
					null);
			return result;
		}
		
		GetSiteFileUrlResponse response = new GetSiteFileUrlResponse();
		String fileurl = SiteFileConstant.DOWNLOAD_FILE_URL;
		fileurl += (fileId + SiteFileConstant.SITEFILE_ZIP_SUFFIX);
		
		response.setFilePath(fileurl);
		response.setMd5(SiteFileConstant.SITE_FILE_MD5);
		result = ApiResultBeanUtils.addApiResult(result, response);
		payment.setSuccess(1);
		
		return result;
	}
	
}
