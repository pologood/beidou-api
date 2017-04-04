package com.baidu.beidou.api.util.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baidu.beidou.api.external.accountfile.config.AccountFileConfigService;
import com.baidu.beidou.api.external.site.service.SiteFileMgr;
import com.baidu.beidou.api.external.util.cache.ApiBlacklistCache;
import com.baidu.beidou.api.external.util.cache.WirelessTradeCache;
import com.baidu.beidou.api.external.util.config.ThruputControlConfigService;
import com.baidu.beidou.api.external.util.service.ApiBlacklistMgr;
import com.baidu.beidou.api.external.util.service.IntensityControlMgr;
import com.baidu.beidou.api.internal.fcindex.ServiceFinder;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.cproplan.service.CproPlanConstantMgr;
import com.baidu.beidou.cprounit.service.LoadRefuseReasonMgr;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.ServiceLocator;
import com.baidu.beidou.util.akadriver.exception.ConfigFileNotFoundException;
import com.baidu.beidou.util.akadriver.exception.InitLoadConfigFileException;
import com.baidu.beidou.util.akadriver.service.impl.AkaDriverFactoryImpl;
import com.baidu.fengchao.tools.init.RPCServiceHandler;

/**
 * ClassName: InitSystemListener 
 * Function: 初始化
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class InitSystemListener implements ServletContextListener {

	private static final Log log = LogFactory.getLog(InitSystemListener.class);

	public void contextInitialized(ServletContextEvent event) {

//		BeanFactory factory = WebApplicationContextUtils
//				.getWebApplicationContext(event.getServletContext());
		
		WebApplicationContext factory = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

		// 构造beanFactory别称
		try {
			ServiceFinder.getInstance().setFactory(factory);
			ServiceLocator.getInstance().setFactory(factory);
		} catch (Exception e) {
			LogUtils.fatal(log, e.getMessage(), e);
		}
		
		AccountFileConfigService accountFileConfigService = (AccountFileConfigService) factory.getBean("accountFileConfigService");
		accountFileConfigService.loadConfig();

		// 初始化推广计划配置
		// CproPlanConstantMgr cproPlanConstantMgr = (CproPlanConstantMgr)
		// factory.getBean("cproPlanConstantMgr");
		// cproPlanConstantMgr.loadSystemConf();

		// 初始化推广组配置
		CproGroupConstantMgr cproGroupConstantMgr = (CproGroupConstantMgr) factory
				.getBean("cproGroupConstantMgr");
		cproGroupConstantMgr.loadSystemConf();

		CproPlanConstantMgr cproPlanConstantMgr = (CproPlanConstantMgr)factory
				.getBean("cproPlanConstantMgr");
		cproPlanConstantMgr.loadCproplanConstant();
		
		// 初始化cprounit的配置。
		// LoadUnitConfigure loadUnitConfigure = (LoadUnitConfigure) factory
		// .getBean("loadUnitConf");
		// loadUnitConfigure.loadConfigure();

		// 初始化拒绝理由
		LoadRefuseReasonMgr loadRefuseReasonMgr = (LoadRefuseReasonMgr) factory.getBean("loadRefuseReasonMgr");
		loadRefuseReasonMgr.loadRefuseReason();

		try {
			AkaDriverFactoryImpl.getInstance().init("aka"); // ClassLoader.getResource("aka.properties")
		} catch (ConfigFileNotFoundException e) {
			LogUtils.fatal(log, e.getMessage(), e);
		} catch (InitLoadConfigFileException e) {
			LogUtils.fatal(log, e.getMessage(), e);
		}
		
		// api模块黑名单
		ApiBlacklistMgr apiBlacklistMgr = (ApiBlacklistMgr) factory.getBean("apiBlacklistMgr");
		ApiBlacklistCache.BLACKLIST_USERID_SET = apiBlacklistMgr.getAllBlacklistUserids();
		
		// 加入function和sleeptime关联对配置
		IntensityControlMgr intensityControlMgr = (IntensityControlMgr)factory.getBean("intensityControlMgr");
		intensityControlMgr.loadFunctionAndSleepTimeMap();
		
		
		// 处理accountfile模块未处理完的任务
		com.baidu.beidou.api.external.accountfile.system.InitUndoneTasks initAccountFileUndoneTaskMgr = (com.baidu.beidou.api.external.accountfile.system.InitUndoneTasks) factory
		.getBean("initAccountFileUndoneTaskMgr");
		initAccountFileUndoneTaskMgr.init();
		
		// 加载流量控制配置
		ThruputControlConfigService thruputControlConfigService = (ThruputControlConfigService) factory.getBean("thruputControlConfigService");
		thruputControlConfigService.loadConfig();
		
		for (TradeInfo trade : UnionSiteCache.tradeInfoCache.getAllTrade()) {
			if (trade.getTradeid() == 28) {
				WirelessTradeCache.firstWirelessTradeSet.add(trade.getTradeid());
				continue;
			}
			if (trade.getParentid() == 28) {
				WirelessTradeCache.secondWirelessTradeSet.add(trade.getTradeid());
				continue;
			}
		}
		
		SiteFileMgr siteFileMgr = (SiteFileMgr)factory.getBean("siteFileMgr");
		siteFileMgr.refresh();
		
		try {
			RPCServiceHandler.init(factory);
		} catch (Exception e) {
			log.fatal("Failed to export services to DR-API !!!", e);
		}
		
	}

	public void contextDestroyed(ServletContextEvent event) {

	}
}

