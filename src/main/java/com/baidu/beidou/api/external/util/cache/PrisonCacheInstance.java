package com.baidu.beidou.api.external.util.cache;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PrisonCacheInstance {
	
	private static final Log LOG = LogFactory.getLog(PrisonCacheInstance.class);
	
	private static volatile PrisonCache<Date> instance = null;

	public static PrisonCache<Date> getCache(int size){
		if(instance == null){
			synchronized (PrisonCache.class) {
				if (instance == null) {
					try {
						instance = new PrisonCache<Date>(size);
					} catch (Exception e) {
						LOG.fatal(e.getMessage(), e);
					}
				}
			}
		}
		return instance;
	}
	
}
