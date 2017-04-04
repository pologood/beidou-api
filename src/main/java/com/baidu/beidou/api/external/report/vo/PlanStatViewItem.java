package com.baidu.beidou.api.external.report.vo;


import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.beidou.report.vo.StatInfo;

/**
 * 
 * ClassName: PlanStatViewItem  <br>
 * Function: 从doris查询出来的推广计划数据VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class PlanStatViewItem extends AbstractStatViewItem implements Serializable{
	
	private static final long serialVersionUID = 3021223077159647189L;

	public PlanStatViewItem(){
		super();
	}
	
	private int userid;
	
	private String username;
	
	private int planid;
	
	private String planname;

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

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("day", day)
		.append("userid", userid)
		.append("username", username)
		.append("planid", planid)
		.append("planname", planname)
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
