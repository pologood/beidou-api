package com.baidu.beidou.api.internal.dsp.vo;

/**
 * 
 * ClassName: PeopleType  <br>
 * Function: 人群VO
 *
 * @author caichao
 * @version 2.0.0
 * @date June 17, 2014
 */
public class PeopleType {

	// 人群id	
	private long pid;
	
	// 人群名称	
	private String name;	
	
	// 人群有效期
	private int aliveDays;
	
	// 人群cookie数	
	private long cookieNum;
	
	//人群类型 0:基于标记 1:基于url 2:百度统计(网盟建) 3:百度统计受众平台建
	private int type;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}

	public long getCookieNum() {
		return cookieNum;
	}

	public void setCookieNum(long cookieNum) {
		this.cookieNum = cookieNum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
