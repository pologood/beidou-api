package com.baidu.beidou.api.external.accountfile.error;

/**
 * 
 * ClassName: AccountFileErrorCode  <br>
 * Function: report前端错误返回码
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public enum AccountFileErrorCode {

	TOO_MANY_CAMPAIGNIDS(7000, "Too many campaign ids"), // PLANID个数超过限制
	PLAN_HAS_BEEN_MARK_AS_DELETE(7001, "The plan has been set to deleted state. could not generate info out of it."), // 如果plan已经标记为删除状态则不允许返回数据
	ACCOUNTFILE_FORMAT_WRONG(7002, "file format is invalid"),  // 账户数据文件格式错误
	FILEID_FORMAT_WRONG(7003, "The fileId is invalid"), // fileId格式错误
	FILEID_NOT_EXIST(7004, "The fileId is not exist"), // fileId不存在
	ACCOUNTFILE_NOT_EXIST(7005, "The file has not or failed to generate"), // fileId对应的文件尚未生成或者生成失败
	ACCOUNTFILE_FORBIDDEN_DUE_TO_LOADCONTROL(7006,"your request too frequently, hold on for a while"),
	;
	
	private int value = 0;
	private String message = null;

	private AccountFileErrorCode(int value, String message) {
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
