package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: AdInfo  <br>
 * Function: 创意其他信息，包括状态、拒绝理由
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class AdInfo implements Serializable{
	
	private static final long serialVersionUID = 13322792842591L;

	private long adId;
	
	private int status;
	
	private String refuseReason;
	
	private String reserved;

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	
	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("adId",adId)
		.append("status", status)
		.append("refuseReason", refuseReason)
		.append("reserved", reserved)
        .toString();
	}
	
}
