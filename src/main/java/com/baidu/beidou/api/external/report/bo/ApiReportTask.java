package com.baidu.beidou.api.external.report.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ApiReport <br>
 * Function: API报告表BO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportTask implements Serializable{
	
	private static final long serialVersionUID = 4127910230853782121L;	

	private long id; 
	
	private String queryparam;
	
	private int status;
	
	private int userid;
	
	private String reportid;
	
	private String machineid;
	
	private int performancedata;
	
	private int iszip;
	
	private int opuser;
	
	private Date addtime;
	
	private Date modtime;
	
	private int retry;
	
	public ApiReportTask(){
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQueryparam() {
		return queryparam;
	}

	public void setQueryparam(String queryparam) {
		this.queryparam = queryparam;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getReportid() {
		return reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public String getMachineid() {
		return machineid;
	}

	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}

	public int getPerformancedata() {
		return performancedata;
	}

	public void setPerformancedata(int performancedata) {
		this.performancedata = performancedata;
	}

	public int getIszip() {
		return iszip;
	}

	public void setIszip(int iszip) {
		this.iszip = iszip;
	}

	public int getOpuser() {
		return opuser;
	}

	public void setOpuser(int opuser) {
		this.opuser = opuser;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getModtime() {
		return modtime;
	}

	public void setModtime(Date modtime) {
		this.modtime = modtime;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("queryparam", queryparam.toString())
		.append("status", status)
		.append("userid", userid)
		.append("reportid", reportid)
		.append("machineid", machineid)
		.append("performancedata", performancedata)
		.append("iszip", iszip)
		.append("opuser", opuser)
		.append("addtime", addtime)
		.append("modtime", modtime)
		.append("retry", retry)
        .toString();
	}
	
}
