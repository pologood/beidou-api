package com.baidu.beidou.api.external.util.config.xml;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * ClassName: Config  <br>
 * Function: 流量控制总配置项
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class Config {
	
	/**
	 * 流量控制模块主要参数：sleep的时间以及wait的次数
	 */
	private int sleepTime;
	private int waitTimes;
	
	/**
	 * 整个应用程序的总操作了阈值
	 */
	private int appThreshold;
	
	/**
	 * 单个用户的总操作阈值
	 */
	private int userThreshold;
	
	/**
	 * 保存着各个function对应的操作数据量阈值
	 */
	private List<Item> items = new ArrayList<Item>();
	

	public int getAppThreshold() {
		return appThreshold;
	}

	public void setAppThreshold(int appThreshold) {
		this.appThreshold = appThreshold;
	}

	public int getUserThreshold() {
		return userThreshold;
	}

	public void setUserThreshold(int userThreshold) {
		this.userThreshold = userThreshold;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}
}
