package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RecommWordVo implements Serializable {

	private static final long serialVersionUID = -6900986138990020161L;

	//关键词字面	
	private String word;

	//平均展现量	
	private long avgShowCnt;

	//覆盖人数	
	private long uv;

	//竞争激烈程度
	private int cmpDegree;

	//关键词词根
	private String kwRoot;

	//推荐理由，1:淘金词，默认0
	private long reasonId = 0;

	//预留字段1
	private int reserved1;

	//预留字段2
	private String reserved2;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getAvgShowCnt() {
		return avgShowCnt;
	}

	public void setAvgShowCnt(long avgShowCnt) {
		this.avgShowCnt = avgShowCnt;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public int getCmpDegree() {
		return cmpDegree;
	}

	public void setCmpDegree(int cmpDegree) {
		this.cmpDegree = cmpDegree;
	}

	public String getKwRoot() {
		return kwRoot;
	}

	public void setKwRoot(String kwRoot) {
		this.kwRoot = kwRoot;
	}

	public long getReasonId() {
		return reasonId;
	}

	public void setReasonId(long reasonId) {
		this.reasonId = reasonId;
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
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("word", word).append("avgShowCnt", avgShowCnt).append("uv", uv).append("cmpDegree", cmpDegree).append("kwRoot", kwRoot).append("reasonId", reasonId).toString();
	}
}
