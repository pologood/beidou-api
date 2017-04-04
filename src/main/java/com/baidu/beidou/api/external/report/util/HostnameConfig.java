package com.baidu.beidou.api.external.report.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName: HostnameConfig  <br>
 * Function: 主机名单例获取工具类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 7, 2012
 */
public class HostnameConfig {

	private static final Log LOG = LogFactory.getLog(HostnameConfig.class);

	public static String HOSTNAME;

	private static HostnameConfig instance = null;
	
	private HostnameConfig(){
		
	}

	static{
		init();
	}
	
	public static HostnameConfig getInstance() {
		if (instance == null) {
			synchronized (HostnameConfig.class) { 
				if (instance == null) 
					instance = new HostnameConfig(); 
			}
		}
		return instance;
	}

	public static String getHOSTNAME() {
		return HOSTNAME;
	}

	public static void init() {
		String hostname = "";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOG.fatal(e.getMessage(), e);
		}
		HOSTNAME = hostname;
	}

}
