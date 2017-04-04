package com.baidu.beidou.api.external.accountfile.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.util.DateUtils;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.StringUtils;


/**
 * 
 * ClassName: PlanVo  <br>
 * Function: 推广计划数据 <br>
 * 
 * 属性包括：CampaignId,CampaignName,预算,状态,起始时间,结束时间,投放日程
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 30, 2012
 */
public class PlanVo implements AbstractVo{

	private long campaignId;
	
	private String campaignName;
	
	private int budget;
	
	private Integer status;
	
	private Date startDate;
	
	private Date endDate;
	
	private ScheduleType[] schedule; 
	
	private Integer type;
	
	private Integer wirelessBidRatio;
	
	//private Boolean isDeviceEnabled; //是否限制移动设备
	
	private int device; //移动设备
	
	//private Boolean isOsEnabled; //是否限制移动操作系统
	
	private long[] os; //移动操作系统
	
	private int planType;
	
	public String[] toStringArray(){
		String[] str = new String[12];
		str[0] = String.valueOf(this.getCampaignId());
		str[1] = String.valueOf(this.getCampaignName());
		str[2] = String.valueOf(this.getBudget());
		str[3] = String.valueOf(this.getStatus());
		str[4] = String.valueOf(DateUtils.toISOString(this.getStartDate()));
		str[5] = String.valueOf(DateUtils.toISOString(this.getEndDate()));
		str[6] = String.valueOf(ArrayUtils.toString(this.getSchedule()));
		str[7] = String.valueOf(ArrayUtils.toString(this.getType()));
		str[8] = String.valueOf(ArrayUtils.toString(this.getWirelessBidRatio()));
		str[9] = String.valueOf(ArrayUtils.toString(this.getDevice()));
		List<Long> os = CollectionsUtil.tranformLongArrayToLongList(this.getOs());
		str[10] = String.valueOf(StringUtils.makeStrFromCollection(os, AccountFileWebConstants.ACCOUNTFILE_DATA_SEPERATOR_STR));
		str[11] = String.valueOf(this.planType);
		return str;
	}

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

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ScheduleType[] getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleType[] schedule) {
		this.schedule = schedule;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getWirelessBidRatio() {
		return wirelessBidRatio;
	}

	public void setWirelessBidRatio(Integer wirelessBidRatio) {
		this.wirelessBidRatio = wirelessBidRatio;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public long[] getOs() {
		return os;
	}

	public void setOs(long[] os) {
		this.os = os;
	}

	public int getPlanType() {
		return planType;
	}

	public void setPlanType(int planType) {
		this.planType = planType;
	}
	
}
