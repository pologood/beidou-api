package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: FCKeyword  <br>
 * Function: 凤巢关键词
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class FCKeyword implements Serializable{
	
	private static final long serialVersionUID = 1135219042721L;

	private long unitId;
	
	private String[] keywords;
	
	public long getUnitId() {
		return unitId;
	}


	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}


	public String[] getKeywords() {
		return keywords;
	}


	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}


	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("unitId",unitId)
		.append("keywords", keywords)
        .toString();
	}
	
}
