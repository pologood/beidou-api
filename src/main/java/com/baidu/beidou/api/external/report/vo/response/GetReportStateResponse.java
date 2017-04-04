package com.baidu.beidou.api.external.report.vo.response;

public class GetReportStateResponse {

	/**
	 * 1：等待中
		2：处理中
		3：处理成功
		4: 处理失败
	 */
	private int isGenerated;

	public int getIsGenerated() {
		return isGenerated;
	}

	public void setIsGenerated(int isGenerated) {
		this.isGenerated = isGenerated;
	}
	
}
