package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: ExcludeKeywordVo  <br>
 * Function: 排除关键词数据
 *
 * @author zhangxu
 * @date Sep 19, 2012
 */
public class ExcludeKeywordVo implements AbstractVo{

	private Integer groupid;
	
	private Integer type;
	
	private Integer keywordPackId;
	
	private String keyword;
	
	public String[] toStringArray(){
		String[] str = new String[4];
		str[0] = String.valueOf(this.getGroupid());
		str[1] = String.valueOf(this.getType());
		str[2] = String.valueOf(this.getKeywordPackId() == null ? "" : this.getKeywordPackId());
		str[3] = String.valueOf(this.getKeyword() == null ? "" : this.getKeyword());
		return str;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getKeywordPackId() {
		return keywordPackId;
	}

	public void setKeywordPackId(Integer keywordPackId) {
		this.keywordPackId = keywordPackId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


}
