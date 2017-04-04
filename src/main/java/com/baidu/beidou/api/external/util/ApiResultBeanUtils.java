package com.baidu.beidou.api.external.util;

import java.io.Serializable;
import java.util.ArrayList;

import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.constant.BeidouAPIType;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.api.external.util.vo.SuccessObject;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: ApiResultBeanUtils
 * Function: api结果处理函数，提供给各个接口使用
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class ApiResultBeanUtils {

	/**
	 * 添加错误信息
	 * 
	 * @param result
	 * @param code
	 * @param message
	 * @param position
	 * @param content
	 * @return
	 */
	public static <T> ApiResult<T> addApiError(ApiResult<T> result, int code,
			String message, String position, String content) {
		ApiResult<T> apiResult = null;
		if (result == null) {
			apiResult = new ApiResult<T>();
		} else {
			apiResult = result;
		}
		if(apiResult.getPayment() == null){
			apiResult.setPayment(new PaymentResult());
		}
		ApiError error = new ApiError();
		error.setCode(code);
		error.setMessage(message);
		error.setPosition(position);
		error.setContent(content);
		if (apiResult.getErrors() == null) {
			apiResult.setErrors(new ArrayList<ApiError>());
		}
		apiResult.getErrors().add(error);
		return apiResult;
	}
	
	/**
	 * 添加错误信息
	 * 
	 * @param type
	 * @param result
	 * @param code
	 * @param message
	 * @param position
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> Object addApiError(int type, Object result, int code,
			String message, String position, String content) {
		if (type == BeidouAPIType.OLD) {
			return addApiError((ApiResult<T>)result, code, message, position, content);
		} else {
			return DRAPIMountAPIBeanUtils.addApiError((BaseResponse<T>)result, code, message, position, content);
		}
	}


	/**
	 * 添加错误信息
	 * 
	 * @param result
	 * @param code
	 * @param message
	 * @param position
	 * @param content
	 * @return
	 */
	public static <T> ApiResult<T> addApiError(ApiResult<T> result,
			ApiError error) {
		ApiResult<T> apiResult = null;
		if (result == null) {
			apiResult = new ApiResult<T>();
		} else {
			apiResult = result;
		}
		if (apiResult.getErrors() == null) {
			apiResult.setErrors(new ArrayList<ApiError>());
		}
		if(apiResult.getPayment() == null){
			apiResult.setPayment(new PaymentResult());
		}
		apiResult.getErrors().add(error);
		return apiResult;
	}

	/**
	 * 添加数据到返回结果中
	 * 
	 * @param <T>
	 * @param result
	 *            返回结果，如果为null，将建立新的ApiResult<T>, 作为结果返回
	 * @param data
	 *            要添加的数据
	 * @return
	 */
	public static <T> ApiResult<T> addApiResult(ApiResult<T> result, T data) {
		ApiResult<T> apiResult = null;
		if (result == null) {
			apiResult = new ApiResult<T>();
		} else {
			apiResult = result;
		}

		if (apiResult.getData() == null) {
			apiResult.setData(new ArrayList<T>());
		}
		apiResult.getData().add(data);
		return apiResult;
	}
	
	/**
	 * setSuccessObject: 对于无返回结果的函数，需要设置一个SuccessObject返回
	 * @version 2.0.0
	 * @author genglei01
	 * @date 2011-12-23
	 */
	public static void setSuccessObject(ApiResult<Object> result) {
		SuccessObject data = SuccessObject.getInstance();
		ApiResultBeanUtils.addApiResult(result, data);
	}
	
	public static <T> ApiResult<T> validateUser(ApiResult<T> result, DataPrivilege user) {
		if (result == null) {
			result = new ApiResult<T>();
		}
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), null, null);
			return result;
		}

		return result;
	}
	
	public static <T> ApiResult<T> validateParam(ApiResult<T> result, ApiRequest request) {
		if (result == null) {
			result = new ApiResult<T>();
		}
		
		if (request == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			result = addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		return result;
	}
	
	public static <T> ApiResult<T> validateUserAndParam(ApiResult<T> result, DataPrivilege user, ApiRequest request) {
		result = validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}
		result = validateParam(result, request);
		if (result.hasErrors()) {
			return result;
		}
		return result;
	}
}
