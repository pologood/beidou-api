package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class UnitResult {
	private int status; // 0正常，1系统错误，2参数错误，unitid2Name为null
	private Map<Long, UnitInfo> unitid2Name;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Long, UnitInfo> getUnitid2Name() {
		return unitid2Name;
	}
	public void setUnitid2Name(Map<Long, UnitInfo> unitid2Name) {
		this.unitid2Name = unitid2Name;
	}

}
