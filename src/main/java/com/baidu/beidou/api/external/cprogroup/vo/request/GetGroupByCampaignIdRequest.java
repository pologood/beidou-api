package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: GetGroupByCampaignIdRequest
 * Function: 获取某个推广计划下的所有推广组信息，需要输入推广计划ID
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GetGroupByCampaignIdRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -6093741584524934241L;
	
	private long campaignId;
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}
}
