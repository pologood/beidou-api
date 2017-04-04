package com.baidu.beidou.api.external.util.service.impl;

import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.cache.IntensityControlCache;
import com.baidu.beidou.api.external.util.service.IntensityControlMgr;
import com.baidu.beidou.util.commontools.PropertiesReader;

/**
 * 
 * ClassName: IntensityControlMgr  <br>
 * Function: 负载控制mgr，负责读取本地配置文件，内容为 <p>
 * 			functionName, sleepTime的关联对，当调用functionName时，主动暂定sleepTime秒
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class IntensityControlMgrImpl implements IntensityControlMgr{

	private static final Log LOG = LogFactory.getLog(IntensityControlMgrImpl.class);
	
	private static final String FUNTION_SLEEP_TIME_FILE = "functionSleeptime";

	private Properties properties;
	
	/**
	 * 加载functionName, sleepTime的关联对，sleepTime为毫秒。用于负载控制。
	 */
	public void loadFunctionAndSleepTimeMap(){
		try {
			properties = PropertiesReader.fillProperties(FUNTION_SLEEP_TIME_FILE);
			for (Entry<Object, Object> entry : properties.entrySet()) {
	            String functionName = entry.getKey().toString().trim();
	            String sleepTime = entry.getValue().toString().trim();
	            if(StringUtils.isEmpty(functionName) || StringUtils.isEmpty(sleepTime)){
	            	LOG.warn("Find empty configuration in functionSleeptime properties file");
	            	continue;
	            }
	            if(!StringUtils.isNumeric(sleepTime)){
	            	LOG.warn("Sleeptime should be numeric in functionSleeptime properties file");
	            	continue;
	            }
	            int time = Integer.parseInt(sleepTime);
	            if(time < 100){
	            	LOG.warn("Sleeptime should be bigger than 100 milliseconds");
	            	continue;
	            }
	            IntensityControlCache.FUNCTION_SLEEPTIME_DICT.put(functionName, time);
	            LOG.info("function=[" + functionName + "] configured to sleep " + sleepTime + " milliseconds after execution");
	        }
		} catch (Exception e) {
		    LOG.warn("fail when loading functionSleeptime properties...cause:" + e.getMessage(), e);
		}
	}
	
}