package com.baidu.beidou.api.external.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: ApiBlacklistCache  <br>
 * Function: 指定方法需要睡眠时间的缓存
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class IntensityControlCache {

	/*
	 * key: function
	 * value: sleep time
	 */
	public static Map<String, Integer> FUNCTION_SLEEPTIME_DICT = new HashMap<String, Integer>();
	
}
