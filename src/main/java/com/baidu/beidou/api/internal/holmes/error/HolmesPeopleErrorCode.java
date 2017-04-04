/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.error.HolmesPeopleErrorCode.java
 * 7:48:02 PM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.error;

/**
 * Holmes人群错误信息
 * 
 * @author Zhang Xu
 */
public enum HolmesPeopleErrorCode {

	PEOPLE_NAME_DUPLICATE(100, "People name duplicate"), // 人群名称重复
	PEOPLE_NAME_INVALID(101, "People name contains invalid character"), // 人群名称包含非法字符
	ALIVEDAYS_INVALID(102, "Alivedays is invalid"),  // 人群有效期非法
	PEOPLE_ID_NOT_FOUND(103, "The Holmes People with the id can not be found"), // Holmes人群ID在北斗不存在
	PEOPLE_NUM_EXCEED(104, "The people number has exceed the limit"), // 人群数量超过限制
	PEOPLE_ID_USERID_NOT_MATCH(105, "Holmes people id and userid not match"), // 人群所属用户非法
	;
	
	private int value = 0;
	private String message = null;

	private HolmesPeopleErrorCode(int value, String message) {
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
