package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;

/**
 * 
 * ClassName: KeywordPackType  <br>
 * Function: 关键词组合配置类
 *
 * @author zhangxu
 * @date Aug 25, 2012
 */
public class KeywordPackType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;
	
	private boolean isOptimized;

	private List<KeywordType> keywords = new ArrayList<KeywordType>();
	
	private int targetType;
	
	private int aliveDays;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KeywordType> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordType> keywords) {
		this.keywords = keywords;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}
		
	public boolean isOptimized() {
		return isOptimized;
	}

	public void setOptimized(boolean isOptimized) {
		this.isOptimized = isOptimized;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id",id)
		.append("name", name)
		.append("isOptimized", isOptimized)
		.append("keywords", keywords)
		.append("targetType", targetType)
		.append("aliveDays", aliveDays)
        .toString();
	}
	
}
