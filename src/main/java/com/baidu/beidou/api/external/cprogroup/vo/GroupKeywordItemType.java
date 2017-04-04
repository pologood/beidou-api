package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: GroupKeywordItemType  <br>
 * Function: 关键词信息
 *
 * @author zhangxu
 * @date May 31, 2012
 */
public class GroupKeywordItemType implements Serializable {
	
	private static final long serialVersionUID = 5578052329842167502L;
	
	private long groupId;	// 推广组id
	
	private String keyword;	// 关键词字面
	
	private int pattern; // 关键词匹配模式

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}


}
