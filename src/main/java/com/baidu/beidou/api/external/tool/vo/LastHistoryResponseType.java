package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 最后一次历史操作记录时间类
 * 
 * @author caichao
 */
public class LastHistoryResponseType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1849890856300606634L;

	private int userId;
	
	private String time;
	
	private int reserve1;
	
	private String reserve2;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getReserve1() {
		return reserve1;
	}

	public void setReserve1(int reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("userId", userId)
		.append("time", time)
		.append("reserve1", reserve1)
		.append("reserve2", reserve2)
        .toString();
	}
	
}
