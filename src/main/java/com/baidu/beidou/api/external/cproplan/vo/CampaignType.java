package com.baidu.beidou.api.external.cproplan.vo;

import java.io.Serializable;
import java.util.Date;

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
}
