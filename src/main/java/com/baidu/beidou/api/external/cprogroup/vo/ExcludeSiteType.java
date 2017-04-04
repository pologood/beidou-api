package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: ExcludeSiteType
 * Function: 过滤网站信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class ExcludeSiteType implements Serializable{
	private static final long serialVersionUID = 1L;
	private long groupId;
	private String[] excludeSite;	// 过滤网站列表
	
	public long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public String[] getExcludeSite() {
		return excludeSite;
	}
	
	public void setExcludeSite(String[] siteFilter) {
		this.excludeSite = siteFilter;
	}

}
