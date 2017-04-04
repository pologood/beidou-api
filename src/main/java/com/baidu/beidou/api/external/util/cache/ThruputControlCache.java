package com.baidu.beidou.api.external.util.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * ClassName: ThruputControlCache <br>
 * Function: 流量控制记录信息缓存，全局唯一
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Apr 20, 2012
 */
public class ThruputControlCache {

	//private static final Log LOG = LogFactory.getLog(ThruputControlCache.class);

	/**
	 * 保存整个应用程序操作数据量
	 */
	private static Integer TOTAL_OPT_DATA_SIZE = new Integer(0);

	/**
	 * 保存在对象中key的一个特例，functionId=0表示所有的function调用的总数据量
	 */
	private static String USER_TOTAL_OPT_DATA_SIZE_SIGN = "*";

	/**
	 * 
	 * 保存用户调用接口的实时数据量对象，对象格式如下，其中functionId=0表示该用户的所有操作数据量汇总值 <br>
	 * 
	 * <code>
	 * {
	 * 		user1 => 
	 * 		{
	 * 	 		* => 64
	 * 			function001 => 10
	 * 	 		function002 => 20
	 * 	 		function003 => 34
	 * 		}
	 * 		user2 =>
	 * 		{
	 * 			* => 55
	 * 			function002 => 0
	 * 	 		function007 => 55
	 * 		}
	 * 		user3 =>
	 * 		{
	 * 			* => 56
	 * 	 		function006 => 11
	 * 	 		function007 => 45
	 * 		}
	 * }
	 * 
	 * </code>
	 * 
	 * <br>
	 * Note:采用ConcurrentHashMap保证线程安全，同时尽量做到高并发，而不采用不安全的HashMap和过度同步的Collections.synchronizedMap(new
	 * HashMap<Integer, Integer>())
	 * 
	 */
	private static Map<Integer, Map<String, Integer>> OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP = new ConcurrentHashMap<Integer, Map<String, Integer>>();

	/**
	 * 增加用户和function上的操作数据量
	 * 
	 * @param userId
	 * @param function
	 * @param dataSize
	 * @return
	 */
	public static void incr(int userId, String function, int dataSize) {
		// 应用程序总操作数据量增加
		TOTAL_OPT_DATA_SIZE += dataSize;

		// 用户与用户调用function的操作数据量增加
		Map<String, Integer> thruputByFunctionIdMap = OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP
				.get(userId);
		if (thruputByFunctionIdMap == null) {
			thruputByFunctionIdMap = new ConcurrentHashMap<String, Integer>();
			thruputByFunctionIdMap.put(USER_TOTAL_OPT_DATA_SIZE_SIGN, dataSize);
			thruputByFunctionIdMap.put(function, dataSize);
			OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP.put(userId,
					thruputByFunctionIdMap);
		} else {
			// 可保证该同步块内的所有代码针对单个用户是一个原子操作
			synchronized (thruputByFunctionIdMap) {
				Integer beforeSize = thruputByFunctionIdMap.get(function);
				if (beforeSize == null) {
					thruputByFunctionIdMap.put(function, dataSize);
				} else {
					thruputByFunctionIdMap.put(function, beforeSize + dataSize);
				}
				Integer beforeUserSize = thruputByFunctionIdMap
						.get(USER_TOTAL_OPT_DATA_SIZE_SIGN);
				if (beforeUserSize == null) {
					thruputByFunctionIdMap.put(USER_TOTAL_OPT_DATA_SIZE_SIGN,
							dataSize);
				} else {
					thruputByFunctionIdMap.put(USER_TOTAL_OPT_DATA_SIZE_SIGN,
							beforeUserSize + dataSize);
				}
				// System.out.println("before:" + beforeSize + ", add:" +
				// dataSize + ", after:" +
				// OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP.get(userId).get(functionId));
				// System.out.println("beforeUserSize:" + beforeUserSize + ",
				// add:" + dataSize + ", after:" +
				// OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP.get(userId).get(USER_TOTAL_OPT_DATA_SIZE_SIGN));
			}
		}
	}

	/**
	 * 减少用户和function上的操作数据量
	 * 
	 * @param userId
	 * @param function
	 * @param dataSize
	 * @return
	 */
	public static void dscr(int userId, String function, int dataSize) {
		// 应用程序总操作数据量减少
		TOTAL_OPT_DATA_SIZE -= dataSize;

		// 用户与用户调用function的操作数据量减少
		Map<String, Integer> thruputByFunctionIdMap = OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP
				.get(userId);
		if (thruputByFunctionIdMap == null) {
			return;
		}
		synchronized (thruputByFunctionIdMap) {
			Integer beforeSize = thruputByFunctionIdMap.get(function);
			if (beforeSize != null) {
				thruputByFunctionIdMap.put(function, beforeSize - dataSize);
			}
			Integer beforeUserSize = thruputByFunctionIdMap
					.get(USER_TOTAL_OPT_DATA_SIZE_SIGN);
			if (beforeUserSize != null) {
				thruputByFunctionIdMap.put(USER_TOTAL_OPT_DATA_SIZE_SIGN, beforeUserSize - dataSize);
			}
			return;
		}
	}

	/**
	 * 获取应用程序总的操作数据量
	 * 
	 * @param
	 * @return Integer
	 */
	public static Integer getAppTotalOptDataSize() {
		return TOTAL_OPT_DATA_SIZE;
	}

	/**
	 * 获取用户的总操作数据量
	 * 
	 * @param userId
	 * @return Integer
	 */
	public static Integer getUserOptDataSize(int userId) {
		return getUserOptDataSizeByFunctionId(userId,
				USER_TOTAL_OPT_DATA_SIZE_SIGN);
	}

	/**
	 * 获取用户的某个函数的操作数据量
	 * 
	 * @param userId
	 *            用户id
	 * @param function
	 *            函数接口名，例如addCampaign
	 * @return Integer 阈值
	 */
	public static Integer getUserOptDataSizeByFunctionId(int userId,
			String function) {
		Map<String, Integer> thruputByFunctionIdMap = OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP
				.get(userId);
		if (thruputByFunctionIdMap == null) {
			return 0;
		}
		Integer dataSize = thruputByFunctionIdMap.get(function);
		if (dataSize == null) {
			return 0;
		}
		return dataSize;
	}

	@SuppressWarnings("rawtypes")
	public static String getLiteralUserOptDataSize(int userId) {
		Map<String, Integer> thruputByFunctionIdMap = OPT_DATA_SIZE_BY_USERID_FUNCTIONID_MAP
				.get(userId);
		if(thruputByFunctionIdMap == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = thruputByFunctionIdMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			sb.append(entry.getKey());
			sb.append(":");
			sb.append(entry.getValue());
			sb.append("|");
		}
		return sb.toString();
	}

}
