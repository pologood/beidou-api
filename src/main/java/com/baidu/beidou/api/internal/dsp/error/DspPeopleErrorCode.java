package com.baidu.beidou.api.internal.dsp.error;

/**
 * Dsp人群错误信息
 * 
 * @author caichao
 */
public enum DspPeopleErrorCode {
	USERID_INVALID(1000,"the userid is not found in wangmeng"),
	SAVE_DB_FAIL(1001,"save code into db fail"),
	RETURN_PID_INVALID(1002,"return pid is invalid"),
	TYPE_INVALID(1003,"the type is invalid");
	
	private int value;
	private String message;
	
	private DspPeopleErrorCode(int value,String message){
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
