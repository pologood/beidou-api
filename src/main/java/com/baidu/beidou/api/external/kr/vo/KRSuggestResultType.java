package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: KRSuggestResultType  <br>
 * Function: 关键词相关推荐返回类型
 *
 * @author zhangxu
 * @date Aug 15, 2012
 */
public class KRSuggestResultType implements Serializable{
	
	private static final long serialVersionUID = 17312712540539L;

	//关键词字面	
	private String word;
	
	//保留字段	
	private String reserved;	

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("word",word)
		.append("reserved", reserved)
        .toString();
	}
	

}
