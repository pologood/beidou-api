package com.baidu.beidou.api.internal.business.vo;


public class UnitResultOne {
	private int status; // 0正常，1系统错误，2参数错误（包括不合法以及unitid2UnitInfo=null）
	private UnitInfo unitid2UnitInfo;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UnitInfo getUnitid2UnitInfo() {
		return unitid2UnitInfo;
	}
	public void setUnitid2UnitInfo(UnitInfo unitid2UnitInfo) {
		this.unitid2UnitInfo = unitid2UnitInfo;
	}
}
