package com.baidu.beidou.api.external.kr.error;

/**
 * 
 * ClassName: KrErrorCode  <br>
 * Function: 关键词推荐错误代码
 *
 * @author zhangxu
 * @date Aug 21, 2012
 */
public enum KrErrorCode {
	
	REGION_NOT_FOUND(700, "The region does not exsit"), // 投放地域不存在
	KT_ALIVEDAYS_ERROR(701, "Invalid alivedays for KT"), // 非法的有效期值
	KT_TARGETTYPE_INTERNAL_ERROR(702, "Invalid KT Target type"), // 非法的关键词定向方式值
	FAILED_TO_GET_KR(703, "Failed to get recommanded keywords"), // 非法的关键词定向方式值
	SEED_WORD_INVALID(704, "Seed word is invalid"), // 非法的关键种子词
	;

	private int value = 0;
	private String message = null;

	private KrErrorCode(int value, String message) {
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
