package com.baidu.beidou.api.external.cproplan2.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class UpdateCampaignRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 5095516526073752270L;
	
	private CampaignType[] campaignTypes;
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(campaignTypes)) {
			result = campaignTypes.length;
		}
		
		return result;
	}

	public CampaignType[] getCampaignTypes() {
		return campaignTypes;
	}

	public void setCampaignTypes(CampaignType[] campaignTypes) {
		this.campaignTypes = campaignTypes;
	}
}
