package com.baidu.beidou.api.external.report.vo;


import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * ClassName: DeviceStatViewItem  <br>
 * Function: 从doris查询出来的设备推广计划数据VO
 *
 * @author caichao
 * @version 2.0.0
 * @since cpweb670
 * @date 11 20, 2013
 */
public class DeviceStatViewItem extends AbstractStatViewItem implements Serializable{
	
	private static final long serialVersionUID = 3021223077159647189L;

	public DeviceStatViewItem(){
		super();
	}
	
	private int userid;
	
	private String username;
	
	private int planid;
	
	private String planname;
	
	private int deviceid;

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
	
	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("day", day)
		.append("userid", userid)
		.append("username", username)
		.append("planid", planid)
		.append("planname", planname)
		.append("deviceid",deviceid)
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
