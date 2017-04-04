package com.baidu.beidou.api.external.kr.vo;

public class KRResponse implements java.io.Serializable {

	private static final long serialVersionUID = -2583892639499163386L;

	//推荐关键词信息列表
	RecommWordVo[] recommWordVos;

	//相关种子词推荐列表
	RelatedWordVo[] relatedWordVos;

	public RecommWordVo[] getRecommWordVos() {
		return recommWordVos;
	}

	public void setRecommWordVos(RecommWordVo[] recommWordVos) {
		this.recommWordVos = recommWordVos;
	}

	public RelatedWordVo[] getRelatedWordVos() {
		return relatedWordVos;
	}

	public void setRelatedWordVos(RelatedWordVo[] relatedWordVos) {
		this.relatedWordVos = relatedWordVos;
	}

}
