package com.baidu.beidou.api.external.fc.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: FCCampaignUnitType  <br>
 * Function: FC推广计划和单元
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
public class FCCampaignUnitType implements Serializable{
	
	private static final long serialVersionUID = 23371792242111L;

	private long campaignId;
	
	private String campaignName;
	
	private FCUnitType[] units;

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public FCUnitType[] getUnits() {
		return units;
	}

	public void setUnits(FCUnitType[] units) {
		this.units = units;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("campaignId",campaignId)
		.append("campaignName", campaignName)
		.append("units", units)
        .toString();
	}
	
}
