package com.baidu.beidou.api.external.cprogroup.error;

import com.baidu.beidou.api.external.util.vo.ApiResult;

/**
 * ClassName: GroupConfigException
 * Function: TODO ADD FUNCTION
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GroupConfigException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1785296738140918925L;
	
	private ApiResult<Object> result;

	public GroupConfigException(ApiResult<Object> result) {
		this.result = result;
	}

	public ApiResult<Object> getResult() {
		return result;
	}

	public void setResult(ApiResult<Object> result) {
		this.result = result;
	}

}