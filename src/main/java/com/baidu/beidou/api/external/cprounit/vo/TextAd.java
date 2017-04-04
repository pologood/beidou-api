package com.baidu.beidou.api.external.cprounit.vo;

import java.io.Serializable;

public class TextAd extends Ad implements Serializable {

	private static final long serialVersionUID = 1L;
	private String descprition1; // 描述1
	private String descprition2; // 描述2

	/**
	 * @return the descprition1
	 */
	public String getDescprition1() {
		return descprition1;
	}

	/**
	 * @param descprition1
	 *            the descprition1 to set
	 */
	public void setDescprition1(String descprition1) {
		this.descprition1 = descprition1;
	}

	/**
	 * @return the descpriton2
	 */
	public String getDescprition2() {
		return descprition2;
	}

	/**
	 * @param descpriton2
	 *            the descpriton2 to set
	 */
	public void setDescprition2(String descpriton2) {
		this.descprition2 = descpriton2;
	}

}
