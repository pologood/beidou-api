package com.baidu.beidou.api.external.util.config.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.XmlUtil;
import com.baidu.beidou.api.external.util.config.ThruputControlConfigService;
import com.baidu.beidou.api.external.util.config.monitor.ThruputControlConfigMonitor;
import com.baidu.beidou.api.external.util.config.xml.Config;
import com.baidu.beidou.api.external.util.config.xml.Item;
import com.baidu.beidou.api.external.util.constant.ThruputControlConstant;

/**
 * 
 * ClassName: ThruputControlConfigServiceImpl  <br>
 * Function: 流量控制配置类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 19, 2012
 */
public class ThruputControlConfigServiceImpl implements ThruputControlConfigService{

	private static final Log LOG = LogFactory.getLog(ThruputControlConfigServiceImpl.class);
	
	/**
	 * 加载流量控制配置文件
	 * @param  
	 * @return 
	 */
	public void loadConfig(){
		try {
			LOG.info("Start to load ThruputControlConfigService xml configuration....");
			
			//先解析XML
			Config config = XmlUtil.fromXml(this.getClass().getResource("/thruput-config.xml").getFile(), Config.class);
			
			//获取所有配置项
			ThruputControlConstant.SLEEP_TIME_WHEN_EXCEED_LIMIT = config.getSleepTime();
			ThruputControlConstant.WAIT_TIME_WHEN_EXCEED_LIMIT = config.getWaitTimes();
			LOG.info("Load successfully for hold sleep time:" + ThruputControlConstant.SLEEP_TIME_WHEN_EXCEED_LIMIT);
			LOG.info("Load successfully for wait times:" + ThruputControlConstant.WAIT_TIME_WHEN_EXCEED_LIMIT);
			ThruputControlConstant.APP_THRESHOLD = config.getAppThreshold();
			ThruputControlConstant.USER_THRESHOLD = config.getUserThreshold();
			LOG.info("Load successfully for APP_THRESHOLD:" + ThruputControlConstant.APP_THRESHOLD);
			LOG.info("Load successfully for USER_THRESHOLD:" + ThruputControlConstant.USER_THRESHOLD);
			List<Item> functions = config.getItems();
			int funtionCount = 0;
			//同步函数级别配置，防止多线程在配置更新时使用有问题
			synchronized(ThruputControlConstant.FUNCTION_THRESHOLD){
				ThruputControlConstant.FUNCTION_THRESHOLD.clear(); //清空对象
				String name = null;
				for(Item function : functions){
					funtionCount++;
					//int id = function.getId(); //id暂时未用
					name = function.getName();
					int threshold = function.getThreshold();
					if(ThruputControlConstant.FUNCTION_THRESHOLD.containsKey(name)){
						LOG.error("duplicated configuration for function " + function);
						continue;
					}
					ThruputControlConstant.FUNCTION_THRESHOLD.put(name, threshold);
					LOG.info("Load successfully for " + function);
				}
			}
			
			//修改监控文件修改时间
			ThruputControlConfigMonitor.updateModifiedTime();
			
			LOG.info("Load total " + funtionCount + " function configurations");
			LOG.info("End to load ThruputControlConfigService xml configuration....");
			
		} catch (Exception e) {
			LOG.error("加载流量控制配置XML文件出现错误", e);
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) throws Exception{
		ThruputControlConfigService i = new ThruputControlConfigServiceImpl();
		i.loadConfig();
	}
	
}
