package com.baidu.beidou.api.external.report.vo;


import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.beidou.report.vo.StatInfo;

/**
 * 
 * ClassName: GenderStatViewItem  <br>
 * Function: 从doris查询出来的性别数据VO
 *
 * @author zhangxu
 * @date Sep 25, 2012
 */
public class GenderStatViewItem extends AbstractStatViewItem implements Serializable{
	
	private static final long serialVersionUID = 1001029050130638071L;

	public GenderStatViewItem(){
		super();
	}
	
	private int userid;
	
	private String username;
	
	private int planid;
	
	private String planname;
	
	private int groupid;
	
	private String groupname;
	
	private int genderid;
	
	private String gendername;
	
	/**
	 * 是否已删除（合并数据使用）
	 */
	private boolean isDeleted = false;

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

	public int getGenderid() {
		return genderid;
	}

	public void setGenderid(int genderid) {
		this.genderid = genderid;
	}

	public String getGendername() {
		return gendername;
	}

	public void setGendername(String gendername) {
		this.gendername = gendername;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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
		.append("genderid", genderid)
		.append("gendername", gendername)
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

}
