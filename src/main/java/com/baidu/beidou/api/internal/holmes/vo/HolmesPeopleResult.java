/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleResult.java
 * 7:39:19 PM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.vo;

/**
 * Holmes人群接口返回类型
 * 
 * @author Zhang Xu
 */
public class HolmesPeopleResult {

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
