package com.baidu.beidou.api.internal.dsp.vo;

/**
 * Dsp人群接口返回类型
 * 
 * @author caichao
 */
public class DspPeopleResult {
	
	private int status;
	private String errorMsg;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
