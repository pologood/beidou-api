package com.baidu.beidou.api.external.site.error;

public enum SiteFileErrorCode {

	FILEID_FORMAT_WRONG(7503, "The fileId is invalid"), // fileId格式错误
	FILEID_NOT_EXIST(7504, "The fileId is not exist"), // fileId不存在
	FILE_UNABLE_TO_DOWNLOAD(7505, "The file has not or failed to generate"), // fileId对应的文件尚未生成或者生成失败
	;

	private int value = 0;
	private String message = null;

	private SiteFileErrorCode(int value, String message) {
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
