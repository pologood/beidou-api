package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: KRResultType  <br>
 * Function: 关键词推荐返回类型
 *
 * @author zhangxu
 * @date Aug 15, 2012
 */
public class KRResultType implements Serializable{
	
	private static final long serialVersionUID = 17322712840599L;

	//关键词字面	
	private String word;
	
	//平均展现量	
	private String avgShowCnt;	
	
	//覆盖人数	
	private int	uv;

	//竞争激烈程度
	private int cmpDegree;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getAvgShowCnt() {
		return avgShowCnt;
	}

	public void setAvgShowCnt(String avgShowCnt) {
		this.avgShowCnt = avgShowCnt;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getCmpDegree() {
		return cmpDegree;
	}

	public void setCmpDegree(int cmpDegree) {
		this.cmpDegree = cmpDegree;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("word",word)
		.append("avgShowCnt", avgShowCnt)
		.append("uv", uv)
        .append("cmpDegree", cmpDegree)
        .toString();
	}
	

}
