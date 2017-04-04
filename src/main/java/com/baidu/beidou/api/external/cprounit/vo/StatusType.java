package com.baidu.beidou.api.external.cprounit.vo;

import java.io.Serializable;

public class StatusType implements Serializable {

	private static final long serialVersionUID = 1L;
	private long adId; // 创意ID
	private int status; // 创意状态，可设置值为：0：生效；1：搁置；在生效状态下该为搁置，搁置状态下改为生效。

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
}
