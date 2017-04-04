package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

/**
 * @author kanghongwei
 * 
 *         kr“相关推荐”结果
 */

public class KrInfoVo4Related implements Serializable {


	/**F
	 * 
	 */
	private static final long serialVersionUID = -8623562650098056913L;

	/** atomid **/
	private Long wordid;

	/** 相关推荐字面 **/
	private String relatedWord;

	public KrInfoVo4Related() {
		super();
	}

	public KrInfoVo4Related(Long wordid, String relatedWord) {
		super();
		this.wordid = wordid;
		this.relatedWord = relatedWord;
	}



	public Long getWordid() {
		return wordid;
	}

	public void setWordid(Long wordid) {
		this.wordid = wordid;
	}

	public String getRelatedWord() {
		return relatedWord;
	}

	public void setRelatedWord(String relatedWord) {
		this.relatedWord = relatedWord;
	}

}
