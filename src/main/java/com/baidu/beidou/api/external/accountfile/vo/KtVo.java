package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: KtVo  <br>
 * Function: 关键词数据 <br>
 * 
 * 属性包括：关键词ID,GroupId,CampaignId,关键词字面,展现资格
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 1, 2012
 */
public class KtVo implements AbstractVo{
	
	private long keywordid;
	
	private Integer planid;

	private Integer groupid;
	
	private String keyword;
	
	// 展现资格
	private int qualityDg;
	
	private int patternType;
	
	public String[] toStringArray(){
		String[] str = new String[6];
		str[0] = String.valueOf(this.getKeywordid());
		str[1] = String.valueOf(this.getGroupid());
		str[2] = String.valueOf(this.getPlanid());
		str[3] = String.valueOf(this.getKeyword());
		str[4] = String.valueOf(this.getQualityDg());
		str[5] = String.valueOf(this.getPatternType());
		return str;
	}

	public int getQualityDg() {
		return qualityDg;
	}

	public void setQualityDg(int qualityDg) {
		this.qualityDg = qualityDg;
	}

	public long getKeywordid() {
		return keywordid;
	}

	public void setKeywordid(long keywordid) {
		this.keywordid = keywordid;
	}

	public Integer getPlanid() {
		return planid;
	}

	public void setPlanid(Integer planid) {
		this.planid = planid;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPatternType() {
		return patternType;
	}

	public void setPatternType(int patternType) {
		this.patternType = patternType;
	}
	
	

}
