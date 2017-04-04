package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ExcludeKeywordType  <br>
 * Function: 排除关键词类
 *
 * @author zhangxu
 * @date Aug 28, 2012
 */
public class ExcludeKeywordType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long groupId;
	
	private List<KeywordType> excludeKeywords;
	
	private List<Integer> excludeKeywordPackIds;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public List<KeywordType> getExcludeKeywords() {
		return excludeKeywords;
	}

	public void setExcludeKeywords(List<KeywordType> excludeKeywords) {
		this.excludeKeywords = excludeKeywords;
	}

	public List<Integer> getExcludeKeywordPackIds() {
		return excludeKeywordPackIds;
	}

	public void setExcludeKeywordPackIds(List<Integer> excludeKeywordPackIds) {
		this.excludeKeywordPackIds = excludeKeywordPackIds;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("excludeKeywords", excludeKeywords)
		.append("excludeKeywordPackIds", excludeKeywordPackIds)
        .toString();
	}
	
}
