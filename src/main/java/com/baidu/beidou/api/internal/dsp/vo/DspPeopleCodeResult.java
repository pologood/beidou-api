package com.baidu.beidou.api.internal.dsp.vo;

/**
 * Dsp接口一站式代码返回类型
 * 
 * @author caichao
 */
public class DspPeopleCodeResult extends DspPeopleResult{
	
	private String code;
	private Long jsid;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getJsid() {
		return jsid;
	}
	public void setJsid(Long jsid) {
		this.jsid = jsid;
	}
	
	
}
