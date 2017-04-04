package com.baidu.beidou.api.internal.business.vo;

import java.util.Map;

public class PlanResult{
	private int status; //0正常，1错误, 2参数错误，planid2Name为null
	private Map<Integer, PlanInfo> planid2Name;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Integer, PlanInfo> getPlanid2Name() {
		return planid2Name;
	}
	public void setPlanid2Name(Map<Integer, PlanInfo> planid2Name) {
		this.planid2Name = planid2Name;
	}
}

