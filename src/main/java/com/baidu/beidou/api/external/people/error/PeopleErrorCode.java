package com.baidu.beidou.api.external.people.error;

/**
 * 
 * ClassName: PeopleErrorCode  <br>
 * Function: 人群接口错误代码
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Feb 3, 2012
 */
public enum PeopleErrorCode {

	PEOPLE_LIST_TOO_LONG(9000, "The people id list is too long"), 
	;

	private int value = 0;
	private String message = null;

	private PeopleErrorCode(int value, String message) {
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
