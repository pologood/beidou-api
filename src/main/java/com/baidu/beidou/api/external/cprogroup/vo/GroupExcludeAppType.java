package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 排除应用类
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-6-7 上午11:44:29
 */
public class GroupExcludeAppType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long groupId;
	
	private List<Long> excludeApp;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<Long> getExcludeApp() {
		return excludeApp;
	}

	public void setExcludeApp(List<Long> excludeApp) {
		this.excludeApp = excludeApp;
	}
}
