package com.baidu.beidou.api.external.report.vo;


import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: KeywordStatViewItem  <br>
 * Function: 从doris查询出来的关键词数据VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class KeywordStatViewItem extends AbstractStatViewItem implements Serializable{
	
	private static final long serialVersionUID = 1129024170250647045L;
	
	private int userid;
	
	private String username;
	
	private int planid;
	
	private String planname;
	
	private int groupid;
	
	private String groupname;
	
	/** 字面 */
	private String keyword;
	
	/** atomId */
	private Long wordId;
	
	/**
	 * 所属受众组合id，没有则为0
	 */
	private long gpId = 0L;
	
	/**
	 * 受众组合名称
	 */
	private String packName;

	public KeywordStatViewItem(){
		super();
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("day", day)
			.append("userid", userid)
			.append("username", username)
			.append("planid", planid)
			.append("planname", planname)
			.append("groupid", groupid)
			.append("groupname", groupname)
			.append("gpId", gpId)
			.append("packName", packName)
			.append("keyword", keyword)
			.append("wordId", wordId)
	        .append("srchs", srchs)
	        .append("clks", clks)
	        .append("cost", cost)
	        .append("ctr", ctr)
	        .append("acp", acp)
	        .append("cpm", cpm)
	        .append("srchuv", srchuv)
	        .append("clkuv", clkuv)
	        .append("srsur", srsur)
	        .append("cusur", cusur)
	        .append("cocur", cocur)
	        .append("arrivalRate", arrivalRate)
	        .append("hopRate", hopRate)
	        .append("avgResTime", avgResTime)
	        .append("directTrans", directTrans)
	        .append("indirectTrans", indirectTrans)
	        .toString();
	}
	
	public long getGpId() {
		return gpId;
	}

	public void setGpId(long gpId) {
		this.gpId = gpId;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getPlanid() {
		return planid;
	}

	public void setPlanid(int planid) {
		this.planid = planid;
	}

	public String getPlanname() {
		return planname;
	}

	public void setPlanname(String planname) {
		this.planname = planname;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}
}
