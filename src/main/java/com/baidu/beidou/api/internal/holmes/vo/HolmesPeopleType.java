/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType.java
 * 7:21:24 PM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.vo;

/**
 * Holmes在北斗新建的人群类型
 * 
 * @author Zhang Xu
 */
public class HolmesPeopleType {
	
	/** Holmes人群ID */
	private Long holmesPid;

	private String name;

	private Integer alivedays;
	
	/** Holmes网站ID */
	private Integer holmesSiteid;

	public Long getHolmesPid() {
		return holmesPid;
	}

	public void setHolmesPid(Long holmesPid) {
		this.holmesPid = holmesPid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAlivedays() {
		return alivedays;
	}

	public void setAlivedays(Integer alivedays) {
		this.alivedays = alivedays;
	}

	public Integer getHolmesSiteid() {
		return holmesSiteid;
	}

	public void setHolmesSiteid(Integer holmesSiteid) {
		this.holmesSiteid = holmesSiteid;
	}

}
