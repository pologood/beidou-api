package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: WordType
 * Function: 关键词信息，通过wordId从atom中获得
 *
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-28
 */
public class WordType implements Serializable {
	
	private static final long serialVersionUID = -7252063284508891490L;
	
	// 关键词字面
	private String keyword;
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("keyword",keyword)
        .toString();
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
