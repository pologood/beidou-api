package com.baidu.beidou.api.external.util.error;

import java.io.Serializable;
import java.util.List;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: GlobalErrorCode
 * Function: 全局errorcode
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public enum GlobalErrorCode {
	SYSTEM_BUSY(0, "System error"), //系统错误
	UNEXPECTED_PARAMETER(1, "Bad parameters"), //参数错误
	UNAUTHORIZATION(2, "Have no authorization"), //没有权限
	TOO_FREQUENTLY(3, "Access too frequent"); //访问过于频繁

	private int value = 0;
	private String message = null;

	private GlobalErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

    /**
     * 返回Error响应
     * 
     * @param response 响应对象
     * @param param 参数类型
     * @param pos 位置
     * @param content 错误信息
     * @return 包含错误信息的响应对象
     */
    public <T extends Serializable> BaseResponse<T> getErrorResponse(BaseResponse<T> response, String param,
            Integer pos, String content) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        if (pos != null) {
            apiPosition.addParam(param, pos);
        } else {
            apiPosition.addParam(param);
        }
        response = DRAPIMountAPIBeanUtils.addApiError(response, value, message, apiPosition.getPosition(), content);
        return response;
    }
    
    /**
     * 返回Error响应
     * 
     * @param result 响应对象
     * @param params 错误参数
     * @param content 错误信息
     * @return 包含错误信息的响应对象
     */
    public ApiResult<Object> getErrorResponse(ApiResult<Object> result, List<ErrorParam> params, String content) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        for (ErrorParam param : params) {
            if (param.getPosition() != null) {
                apiPosition.addParam(param.getParamName(), param.getPosition().intValue());
            } else {
                apiPosition.addParam(param.getParamName());
            }
        }

        result = ApiResultBeanUtils.addApiError(result, value, message, apiPosition.getPosition(), content);
        return result;
    }
}
