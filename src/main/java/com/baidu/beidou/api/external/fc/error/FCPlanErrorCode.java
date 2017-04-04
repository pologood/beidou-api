package com.baidu.beidou.api.external.fc.error;

/**
 * 
 * ClassName: FCPlanErrorCode  <br>
 * Function: FC推广计划错误代码
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
public enum FCPlanErrorCode {

	TOOMANY_NUM(801, "The batch size exceeds the limit"), // 批量条数过多
	PARAM_EMPTY(802, "The param is empty"), // 参数为空
	;

	private int value = 0;
	private String message = null;

	private FCPlanErrorCode(int value, String message) {
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
