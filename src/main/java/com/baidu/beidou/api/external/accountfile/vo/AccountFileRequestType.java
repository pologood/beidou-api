package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: AccountFileRequestType  <br>
 * Function: 获取账户或者指定推广计划下的完整数据
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountFileRequestType {

	private long[] campaignIds;
	
	private int format;

	public long[] getCampaignIds() {
		return campaignIds;
	}

	public void setCampaignIds(long[] campaignIds) {
		this.campaignIds = campaignIds;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}
	
	
}
