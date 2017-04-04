package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

public class AttachInfoUserResponseType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6700488557119171385L;
	private int userId;
	private int sign;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
}
