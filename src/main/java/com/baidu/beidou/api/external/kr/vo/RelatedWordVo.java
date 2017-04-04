package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author qianlei
 * kr“相关推荐”结果
 */

public class RelatedWordVo implements Serializable {

	private static final long serialVersionUID = 2895961308370284741L;

	//相关推荐字面
	private String relatedWord;

	//预留字段1
	private int reserved1;

	//预留字段2
	private String reserved2;

	public RelatedWordVo() {
		super();
	}

	public RelatedWordVo(String relatedWord) {
		super();
		this.relatedWord = relatedWord;
	}

	public void setRelatedWord(String relatedWord) {
		this.relatedWord = relatedWord;
	}

	public int getReserved1() {
		return reserved1;
	}

	public void setReserved1(int reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("relatedWord", relatedWord).toString();
	}

}
