package com.baidu.beidou.api.external.fc.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: FCCampaignUnitIdType  <br>
 * Function: FC推广计划和单元
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
public class FCCampaignUnitIdType implements Serializable{
	
	private static final long serialVersionUID = 23171793242411L;

	private long campaignId;
	
	private long[] unitIds;

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public long[] getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(long[] unitIds) {
		this.unitIds = unitIds;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("campaignId",campaignId)
		.append("unitIds", unitIds)
        .toString();
	}
	
}
