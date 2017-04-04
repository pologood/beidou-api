package com.baidu.beidou.api.external.cprogroup.vo;

import java.util.List;

/**
 * 
 * ClassName: KtItemType  <br>
 * Function: 关键词信息
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class KtItemType {

	private static final long serialVersionUID = 1L;
	
	//关键词定向方式
	private int targetType = -1;
	
	//有效期
	private int aliveDays = -1;
	
	//KT词
	private KeywordType[] ktWordList;

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}

	public KeywordType[] getKtWordList() {
		return ktWordList;
	}

	public void setKtWordList(KeywordType[] ktWordList) {
		this.ktWordList = ktWordList;
	}

	
	
}
