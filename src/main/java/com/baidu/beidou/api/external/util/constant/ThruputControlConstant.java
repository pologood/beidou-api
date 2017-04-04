package com.baidu.beidou.api.external.util.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: ThruputControlConstant  <br>
 * Function: 流量控制常量类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 20, 2012
 */
public class ThruputControlConstant {
	/**
	 * 流量控制模块主要参数：sleep的时间以及wait的次数
	 */
	public static int SLEEP_TIME_WHEN_EXCEED_LIMIT;
	public static int WAIT_TIME_WHEN_EXCEED_LIMIT;

	/**
	 * 整个应用程序的总操作了阈值
	 */
	public static int APP_THRESHOLD;
	
	/**
	 * 单个用户的总操作阈值
	 */
	public static int USER_THRESHOLD;
	
	/**
	 * 保存着各个function对应的操作数据量阈值，不要修改，只能通过getThresholdByFunction返回值 <br>
	 * key为function名，value为操作量阈值
	 */
	public static Map<String, Integer> FUNCTION_THRESHOLD = new HashMap<String, Integer>();
	
	/**
	 * 根据function返回操作数据量阈值
	 * @param function 函数名
	 * @return 函数操作量阈值，如果未配置或未找到则返回ApiConstant.UNLIMIT_THRESHOLD，调用方需要自行判断处理
	 */
	public static Integer getThresholdByFunction(String function){
		Integer threshold = FUNCTION_THRESHOLD.get(function);
		if(threshold == null){
			return ApiConstant.UNLIMIT_THRESHOLD;
		} 
		return threshold;
	}
	
}
