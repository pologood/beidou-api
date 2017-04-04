package com.baidu.beidou.api.external.cproplan2.constant;

/**
 * ClassName: PlanConstant
 * Function: 推广计划常量
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public class PlanConstant {

	public static final String CAMPAIGN_TYPES = "campaignTypes";
	public static final String CAMPAIGN_IDS = "campaignIds";
	
	public static final String POSITION_CAMPAIGN_ID = "campaignId";
	public static final String POSITION_CAMPAIGN_NAME = "campaignName";
	public static final String POSITION_BUDGET = "budget";
	public static final String POSITION_STATUS = "status";
	public static final String POSITION_START_DATE = "startDate";
	public static final String POSITION_END_DATE = "endDate";
	public static final String POSITION_SCHEDULE = "schedule";
	public static final String POSITION_TYPE = "type";
	public static final String POSITION_WIRELESSBIDRATIO = "wirelessBidRatio";
	public static final String POSITION_IS_DEVICE_ENABLED = "isDeviceEnabled";
	public static final String POSITION_DEVICE = "device";
	public static final String POSITION_IS_OS_ENABLED = "isOsEnabled";
	public static final String POSITION_OS = "os";
	

	public static final int LENGTH_PLAN_NAME = 30;

	public static final int FULL_SCHEDULE = (1 << 24) - 1; // 全天投放
	
	// 查询推广计划最多返回的数量（因北斗星中对一个用户下的计划数不再限制，为防止极端情况，添加了数量限制）
	public static final int MAX_QUERY_PLAN_NUM = 500;
	
	public static final int ALL_DEVICE = 1;
	public static final int ALL_OS = 1;
	
}
