package com.baidu.beidou.api.external.accountfile.config.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.beidou.api.external.accountfile.config.AccountFileConfigService;
import com.baidu.beidou.api.external.accountfile.config.AccountFileItem;
import com.baidu.beidou.api.external.accountfile.config.xml.Config;
import com.baidu.beidou.api.external.accountfile.config.xml.Item;
import com.baidu.beidou.api.external.util.XmlUtil;

/**
 * 
 * ClassName: AccountFileConfigServiceImpl  <br>
 * Function: 账户信息数据配置服务
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class AccountFileConfigServiceImpl implements AccountFileConfigService, ApplicationContextAware{
	
	private static final Log LOG = LogFactory.getLog(AccountFileConfigServiceImpl.class);

	private ApplicationContext springContext; 
	
	private List<AccountFileItem> itemList = new ArrayList<AccountFileItem>();
	
	
	/**
	 * 加载账户信息文件处理的配置
	 * @param  
	 * @return 
	 */
	public void loadConfig(){
		try {
			LOG.info("Start to load AccountFileService xml configuration....");
			
			//先解析XML
			Config config = XmlUtil.fromXml(AccountFileConfigServiceImpl.class.getResourceAsStream("/accountfile-config.xml"), Config.class);
			
			//获取所有配置项
			List<Item> itemList = config.getItems();
			for(Item item : itemList){
				AccountFileItemImpl itemConfig = new AccountFileItemImpl(item, springContext);
				this.itemList.add(itemConfig);
			}
			LOG.info("Load total " + this.itemList.size() + " items");
			LOG.info("End to load AccountFileService xml configuration....");
		} catch (Exception e) {
			LOG.error("加载账户信息数据配置XML文件出现错误", e);
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 获取所有处理账户信息结构的项目
	 * @param  
	 * @return List<AccountFileItem> 所有处理账户信息结构的项目
	 */
	public List<AccountFileItem> getItems(){
		return itemList;
	}

	/**
	 * 实现ApplicationContextAware接口便于从Srping applicaiton context中获取bean
	 */
	public void setApplicationContext(ApplicationContext springContext) throws BeansException {
		this.springContext = springContext;
	}
	
}
