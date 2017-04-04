package com.baidu.beidou.api.external.accountfile.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ApiAccountFileTask  <br>
 * Function: API所用的accountfile表BO
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class ApiAccountFileTask implements Serializable{
	
	private static final long serialVersionUID = 4123915230852732181L;	

	private long id;

	private int userid;

	private String planids;
	
	private int status;
	
	private String fileid;
	
	private String md5;
	
	private String machineid;
	
	private int format;
	
	private int opuser;
	
	private Date addtime;
	
	private Date modtime;
	
	private int retry;
	
	private Date handletime;

	private double version;

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public int getUserid() {
		return userid;
	}



	public void setUserid(int userid) {
		this.userid = userid;
	}



	public String getPlanids() {
		return planids;
	}



	public void setPlanids(String planids) {
		this.planids = planids;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getFileid() {
		return fileid;
	}



	public void setFileid(String fileid) {
		this.fileid = fileid;
	}



	public String getMd5() {
		return md5;
	}



	public void setMd5(String md5) {
		this.md5 = md5;
	}



	public String getMachineid() {
		return machineid;
	}



	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}



	public int getFormat() {
		return format;
	}



	public void setFormat(int format) {
		this.format = format;
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

	

	public Date getHandletime() {
		return handletime;
	}



	public void setHandletime(Date handletime) {
		this.handletime = handletime;
	}

	public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).append("userid", userid)
                .append("planids", planids).append("status", status).append("fileid", fileid)
                .append("machineid", machineid).append("format", format).append("opuser", opuser)
                .append("addtime", addtime).append("modtime", modtime).append("retry", retry)
                .append("handletime", handletime).append("version", version).toString();
    }
}
