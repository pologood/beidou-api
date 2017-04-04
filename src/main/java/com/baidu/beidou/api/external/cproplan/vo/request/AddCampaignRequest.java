package com.baidu.beidou.api.external.cproplan.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class AddCampaignRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -1769088871704954958L;
	
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
