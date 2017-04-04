package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: KeywordType
 * Function: 关键词信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class KeywordType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//关键词字面
	private String keyword;	
	
	//展现资格
	private int qualification;
	
	//匹配模式，PP或者IM
	private int pattern;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getQualification() {
		return qualification;
	}

	public void setQualification(int qualification) {
		this.qualification = qualification;
	}

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("keyword",keyword)
		.append("pattern", pattern)
		.append("qualification", qualification)
        .toString();
	}
	
	public final static List<String> extractKeywords(Collection<KeywordType> keywords){
		if(keywords == null)
			return new ArrayList<String>(0);
		
		List<String> keywordList = new ArrayList<String>(keywords.size());
		for(KeywordType keyword : keywords){
			keywordList.add(keyword.getKeyword());
		}
		return keywordList;
	}

}
