package com.baidu.beidou.api.external.cproplan2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class GetCampaignByCampaignIdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 5253569509793244630L;
	
	private long[] campaignIds;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(campaignIds)) {
			result = campaignIds.length;
		}
		
		return result;
	}

	public long[] getCampaignIds() {
		return campaignIds;
	}

	public void setCampaignIds(long[] campaignIds) {
		this.campaignIds = campaignIds;
	}
}
