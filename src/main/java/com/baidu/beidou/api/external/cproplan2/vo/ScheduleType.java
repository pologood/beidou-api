package com.baidu.beidou.api.external.cproplan2.vo;

import java.io.Serializable;

/**
 * ClassName: ScheduleType
 * Function: 推广计划日程安排
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class ScheduleType implements Serializable {

	private static final long serialVersionUID = 1L;
	private int weekDay; //星期一：1； 星期二：2； 星期三：3；星期四：4；星期五：5；星期六：6，星期天：7；
	private int startTime; //0-23
	private int endTime; //1-24

	/**
	 * @return the dayOfWeek
	 */
	public int getWeekDay() {
		return weekDay;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setWeekDay(int dayOfWeek) {
		this.weekDay = dayOfWeek;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + weekDay;
		result = prime * result + startTime;
		result = prime * result + endTime;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ScheduleType other = (ScheduleType) obj;
		if (weekDay != other.weekDay)
			return false;
		if (startTime != other.startTime)
			return false;
		if (endTime != other.endTime)
			return false;
		return true;
	}

	/**
	 * startTime
	 *
	 * @return  the startTime
	 */

	public int getStartTime() {
		return startTime;
	}

	/**
	 * startTime
	 *
	 * @param   startTime    the startTime to set
	 */

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	/**
	 * endTime
	 *
	 * @return  the endTime
	 */

	public int getEndTime() {
		return endTime;
	}

	/**
	 * endTime
	 *
	 * @param   endTime    the endTime to set
	 */

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

}
