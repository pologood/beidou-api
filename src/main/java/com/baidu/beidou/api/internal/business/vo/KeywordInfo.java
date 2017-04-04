package com.baidu.beidou.api.internal.business.vo;

public class KeywordInfo {

	private String literal; //关键词字面
	
	private int isDeleted; //删除状态为1，默认为0

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
