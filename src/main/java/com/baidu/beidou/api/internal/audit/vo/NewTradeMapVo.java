package com.baidu.beidou.api.internal.audit.vo;

public class NewTradeMapVo {
	
	private Integer newTradeId;
	private Integer oldTradeId;
	
	public NewTradeMapVo(Integer newTradeId, Integer oldTradeId) {
		this.newTradeId = newTradeId;
		this.oldTradeId = oldTradeId;
	}
	
	public Integer getNewTradeId() {
		return newTradeId;
	}
	public void setNewTradeId(Integer newTradeId) {
		this.newTradeId = newTradeId;
	}
	public Integer getOldTradeId() {
		return oldTradeId;
	}
	public void setOldTradeId(Integer oldTradeId) {
		this.oldTradeId = oldTradeId;
	}
	
}
