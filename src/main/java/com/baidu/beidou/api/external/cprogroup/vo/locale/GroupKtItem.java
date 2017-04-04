package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * 
 * ClassName: GroupKtItem  <br>
 * Function: 内部使用对象
 *
 * @author zhangxu
 * @date Jun 1, 2012
 */
public class GroupKtItem {
	
	private int index;	// 参数中所在索引
	
	private String keyword;	// 关键词字面
	
	private int pattern; //匹配模式
	
	public GroupKtItem(int index, String keyword, int pattern) {
		this.index = index;
		this.keyword = keyword;
		this.pattern = pattern;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	
	
}
