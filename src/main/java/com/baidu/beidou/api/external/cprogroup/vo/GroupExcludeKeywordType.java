package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: GroupExcludeKeywordType  <br>
 * Function: 推广组排除关键词类
 *
 * @author zhangxu
 * @date Aug 28, 2012
 */
public class GroupExcludeKeywordType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long groupId;
	
	private int type; // 1：关键词, 2：关键词组合

	private KeywordType excludeKeyword;
	
	private int excludeKeywordPackId;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public KeywordType getExcludeKeyword() {
		return excludeKeyword;
	}

	public void setExcludeKeyword(KeywordType excludeKeyword) {
		this.excludeKeyword = excludeKeyword;
	}

	public int getExcludeKeywordPackId() {
		return excludeKeywordPackId;
	}

	public void setExcludeKeywordPackId(int excludeKeywordPackId) {
		this.excludeKeywordPackId = excludeKeywordPackId;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("type",type)
		.append("excludeKeyword", excludeKeyword)
		.append("excludeKeywordPackId", excludeKeywordPackId)
        .toString();
	}
	
}
