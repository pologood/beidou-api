package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: ExcludeIpType
 * Function: 过滤IP信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class ExcludeIpType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long groupId;
	private String[] excludeIp;	// 过滤IP列表
	
	public long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public String[] getExcludeIp() {
		return excludeIp;
	}
	
	public void setExcludeIp(String[] ipFilter) {
		this.excludeIp = ipFilter;
	}
}
