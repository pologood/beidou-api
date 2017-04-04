/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.vo.AddHolmesPeopleResult.java
 * 12:27:36 AM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.vo;

/**
 * 新建holmes人群返回接口，包含pid
 * 
 * @author Zhang Xu
 */
public class AddHolmesPeopleResult extends HolmesPeopleResult{

	private Long pid;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
	
}
