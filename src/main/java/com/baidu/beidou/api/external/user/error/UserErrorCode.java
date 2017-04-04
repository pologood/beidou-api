package com.baidu.beidou.api.external.user.error;

/**
 * ClassName: UserErrorCode
 * Function: 用户层errorcode
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public enum UserErrorCode {
	NO_USER(100, "The user does not exist"); // 用户不存在

	private int value = 0;
	private String message = null;

	private UserErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}
}
