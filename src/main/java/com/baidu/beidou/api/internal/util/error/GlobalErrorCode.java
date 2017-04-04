package com.baidu.beidou.api.internal.util.error;

public enum GlobalErrorCode {
	SUCCESS(0, "成功"), 
	SYSTEM_BUSY(-1, "系统错误"), 
	UNEXPECTED_PARAMETER(-2, "参数错误"), 
	UNAUTHORIZATION(-3, "没有权限");

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
}