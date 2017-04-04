package com.baidu.beidou.api.external.util.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 为无线白名单准备的cache
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-6-24 下午3:00:37
 */
public class WirelessTradeCache {

	public static Set<Integer> firstWirelessTradeSet = new HashSet<Integer>();
	
	public static Set<Integer> secondWirelessTradeSet = new HashSet<Integer>();
	
	public static boolean isWirelessTrade(Integer tradeId) {
		if (firstWirelessTradeSet.contains(tradeId) || secondWirelessTradeSet.contains(tradeId)) {
			return true;
		}
		return false;
	}
	
}
