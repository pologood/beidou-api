package com.baidu.beidou.api.external.interest.error;

/**
 * 
 * ClassName: PeopleErrorCode  <br>
 * Function: 兴趣接口错误代码
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public enum InterestErrorCode {

	INTEREST_ID_LIST_TOO_LONG(9100, "Interest id list too long"), 
	;

	private int value = 0;
	private String message = null;

	private InterestErrorCode(int value, String message) {
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
