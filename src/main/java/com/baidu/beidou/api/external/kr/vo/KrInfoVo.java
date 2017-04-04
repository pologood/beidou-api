package com.baidu.beidou.api.external.kr.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class KrInfoVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/** atomid，前端不会使用，有可能为0(新词)，用于发现问题等**/
	private Long wordid;
	/** 字面**/
	private String kw;
	/** 竞争激烈程度 **/
	private Integer compdg;
	/** 平均展现量 **/
	private Long awgdscnt;
	/** 覆盖人数 **/
	private Long uv;
	
	/**平均展现量高级**/
	private Long advanceDscnt; //暂时不提供
	/** 覆盖人数高级 **/
	private Long advanceUv;//暂时不提供

	/**关键词词根**/
	private String kwRoot;

	/**推荐理由，1:淘金词**/
	private Long reasonId = 0L;
	
	private int reserved1;
	
	private String reserved2;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/****** getter and setter - start ******/
	public Long getWordid() {
		return wordid;
	}


	public void setWordid(Long wordid) {
		this.wordid = wordid;
	}


	public String getKw() {
		return kw;
	}


	public void setKw(String kw) {
		this.kw = kw;
	}


	public Integer getCompdg() {
		return compdg;
	}


	public void setCompdg(Integer compdg) {
		this.compdg = compdg;
	}


	public Long getAwgdscnt() {
		return awgdscnt;
	}


	public void setAwgdscnt(Long awgdscnt) {
		this.awgdscnt = awgdscnt;
	}


	public Long getUv() {
		return uv;
	}


	public void setUv(Long uv) {
		this.uv = uv;
	}


	public Long getAdvanceUv() {
		return advanceUv;
	}


	public void setAdvanceUv(Long advanceUv) {
		this.advanceUv = advanceUv;
	}
	
	public Long getAdvanceDscnt() {
		return advanceDscnt;
	}
	public void setAdvanceDscnt(Long advanceDscnt) {
		this.advanceDscnt = advanceDscnt;
	}


	public String getKwRoot() {
		return kwRoot;
	}


	public void setKwRoot(String kwRoot) {
		this.kwRoot = kwRoot;
	}


	public Long getReasonId() {
		return reasonId;
	}


	public void setReasonId(Long reasonId) {
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
	
	/****** getter and setter - end ******/


}
