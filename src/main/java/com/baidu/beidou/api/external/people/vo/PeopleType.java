package com.baidu.beidou.api.external.people.vo;

/**
 * 
 * ClassName: PeopleType  <br>
 * Function: 人群VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 10, 2012
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
	

}
