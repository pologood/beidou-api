package com.baidu.beidou.api.external.cproplan2.vo;

import java.io.Serializable;
import java.util.Date;

import com.baidu.beidou.cproplan.constant.CproPlanConstant;

/**
 * ClassName: CampaignType
 * Function: 推广计划信息设置
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class CampaignType implements Serializable {
	private static final long serialVersionUID = 1L;
	private long campaignId = -1; //在新增接口中，没有意义。
	private String campaignName; //新增的计划名
	private int budget = -1; //预算， 单位为元
	private Integer status = -1; //状态：0：生效；1: 搁置；2：删除；3：为开始；4：已结束。对新增接口，只可以设置生效或搁置
	private Date startDate; //起始时间：yyyyMMdd, 
	private Date endDate; //结束时间: yyyyMMdd,  为null,表示不修改结束时间,为空字符串,表示设置结束时间为空
	private ScheduleType[] schedule; //投放日程。如果为null或者为空数组，则表示去全日程投放
	private Integer type = null;//CproPlanConstant.PROMOTIONTYPE_ALL; // 推广类型:0-所有功能【默认】，1-仅无线
	private Integer wirelessBidRatio = null;//CproPlanConstant.WIRELESSBIDRATIO_DEFAULT; // 无线出价/PC出价比例 * 100
	private Boolean isDeviceEnabled; //是否限制移动设备
	private int device; //移动设备
	private Boolean isOsEnabled; //是否限制移动操作系统
	private long[] os; //移动操作系统
	
	//冗余字段
	private Integer reserved1; //用做表示plantype，计划类型，0：普通计划，1：at右计划
	private Integer reserved2;
	private Integer[] reserved3;
	private Long reserved4;
	private Long[] reserved5;
	private String reserved6;
	private String reserved7;
	
	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long planId) {
		this.campaignId = planId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String planName) {
		this.campaignName = planName;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Boolean getIsDeviceEnabled() {
		return isDeviceEnabled;
	}

	public void setIsDeviceEnabled(Boolean isDeviceEnabled) {
		this.isDeviceEnabled = isDeviceEnabled;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public Boolean getIsOsEnabled() {
		return isOsEnabled;
	}

	public void setIsOsEnabled(Boolean isOsEnabled) {
		this.isOsEnabled = isOsEnabled;
	}

	public long[] getOs() {
		return os;
	}

	public void setOs(long[] os) {
		this.os = os;
	}

	public Integer getReserved1() {
		return reserved1;
	}

	public void setReserved1(Integer reserved1) {
		this.reserved1 = reserved1;
	}

	public Integer getReserved2() {
		return reserved2;
	}

	public void setReserved2(Integer reserved2) {
		this.reserved2 = reserved2;
	}

	public Integer[] getReserved3() {
		return reserved3;
	}

	public void setReserved3(Integer[] reserved3) {
		this.reserved3 = reserved3;
	}

	public Long getReserved4() {
		return reserved4;
	}

	public void setReserved4(Long reserved4) {
		this.reserved4 = reserved4;
	}

	public Long[] getReserved5() {
		return reserved5;
	}

	public void setReserved5(Long[] reserved5) {
		this.reserved5 = reserved5;
	}

	public String getReserved6() {
		return reserved6;
	}

	public void setReserved6(String reserved6) {
		this.reserved6 = reserved6;
	}

	public String getReserved7() {
		return reserved7;
	}

	public void setReserved7(String reserved7) {
		this.reserved7 = reserved7;
	}
	
}
