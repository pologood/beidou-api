package com.baidu.beidou.api.external.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.constant.BeidouAPIType;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseErrors;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
import com.google.common.collect.Maps;

/**
 * ClassName: DRAPIMountAPIBeanUtils
 * Function: API结果处理函数，提供给各个接口使用，该接口适用于“彩虹桥”模式
 *
 * @author zhangxu
 * @version 3.0 Plus
 */
public class DRAPIMountAPIBeanUtils {

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
	public static <T extends Serializable> BaseResponse<T> addApiError(BaseResponse<T> result, int code,
			String message, String position, String content) {
		BaseResponse<T> response = null;
		if (result == null) {
			response = new BaseResponse<T>();
		} else {
			response = result;
		}
		BaseResponseOptions options = response.getOptions();
		if(options == null){
			options = new BaseResponseOptions();
			response.setOptions(options);
			options.setTotal(1);
		}
		BaseResponseErrors error = new BaseResponseErrors();
		error.setCode(code);
		error.setMessage(message);
		error.setPosition(position);
		error.setContent(content);
		if (response.getErrors() == null) {
			response.setErrors(new ArrayList<BaseResponseErrors>());
		}
		response.getErrors().add(error);
		return response;
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
	public static <T extends Serializable> BaseResponse<T> addApiError(BaseResponse<T> response,
			BaseResponseErrors error) {
		BaseResponse<T> apiResponse = null;
		if (response == null) {
			apiResponse = new BaseResponse<T>();
		} else {
			apiResponse = response;
		}
		if (apiResponse.getErrors() == null) {
			apiResponse.setErrors(new ArrayList<BaseResponseErrors>());
		}
		if(apiResponse.getOptions() == null){
			apiResponse.setOptions(new BaseResponseOptions());
		}
		apiResponse.getErrors().add(error);
		return apiResponse;
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
	public static <T extends Serializable> BaseResponse<T> setApiResult(BaseResponse<T> result, T[] data) {
		BaseResponse<T> apiResponse = null;
		if (result == null) {
			apiResponse = new BaseResponse<T>();
		} else {
			apiResponse = result;
		}

		apiResponse.setData(data);
		return apiResponse;
	}
	
	public static <T extends Serializable> BaseResponse<T> validateUserAndParam(BaseResponse<T> response, BaseRequestUser user, ApiRequest param) {
		response = validateUser(response, user);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}
		response = validateParam(response, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}
		return response;
	}
	
	/**
	 * 返回操作管理员id和用户id
	 * @param user
	 * @return int[] 下标0为opuser，下标1为userid
	 */
	public static int[] getUser(BaseRequestUser user) {
		int[] result = new int[2];
		int userId = new Long(user.getDataUser()).intValue();
		int opUser = new Long(user.getOpUser()).intValue();
		result[0] = opUser;
		result[1] = userId;
		return result;
	}
	
	
	public static <T extends Serializable> BaseResponse<T> validateUser(BaseResponse<T> response, BaseRequestUser user) {
		if (response == null) {
			response = new BaseResponse<T>();
		}
		
		if (user == null) {
			response = addApiError(response,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), null, null);
			return response;
		}

		return response;
	}
	
	public static <T extends Serializable> BaseResponse<T> validateParam(BaseResponse<T> response, ApiRequest param) {
		if (response == null) {
			response = new BaseResponse<T>();
		}
		
		if (param == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			response = addApiError(response,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return response;
		}

		return response;
	}

    /**
     * 创建默认响应
     * 
     * @param total 请求参数总数
     * @return 默认响应
     */
    public static <T extends Serializable> BaseResponse<T> createResponse(int total) {
        BaseResponse<T> response = new BaseResponse<T>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(total);
        response.setOptions(option);
        return response;
    }

	/**
	 * 返回DR-API调用类型（旧或者新的“彩虹桥”模式），以及操作管理员id和用户id
	 * @param obj
	 * @return int[] 下标0为类型，下标1为opuser，下标2为userid，
	 */
	public static int[] getTypeUserInfo(Object obj) {
		int[] result = new int[3];
		int type;
		int opuser;
		int datauser;
		if (obj instanceof DataPrivilege) {
			type = BeidouAPIType.OLD;
			DataPrivilege user = (DataPrivilege) obj;
			opuser = user.getOpUser();
			datauser = user.getDataUser();
		} else {
			type = BeidouAPIType.NEW;
			BaseRequestUser user = (BaseRequestUser) obj;
			opuser = new Long(user.getOpUser()).intValue();
			datauser = new Long(user.getDataUser()).intValue();
		}
		result[0] = type;
		result[1] = opuser;
		result[2] = datauser;
		return result;
	}
	
    public static Map<String, String> getRequestOption(Object obj, String...keys) {
        Map<String, String> result = Maps.newHashMapWithExpectedSize(keys.length);

        Map<String, String> optionMap = null;
        if (obj instanceof ApiOption) {
            ApiOption option = (ApiOption) obj;
            optionMap = option.getOptions();
        } else {
            BaseRequestOptions option = (BaseRequestOptions) obj;
            optionMap = option.getOptions();
        }

        for (String key : keys) {
            String value = optionMap.get(key);
            if (value != null) {
                result.put(key, value);
            }
        }

        return result;
    }
	
}
