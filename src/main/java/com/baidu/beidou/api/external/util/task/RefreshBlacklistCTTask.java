package com.baidu.beidou.api.external.util.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.beidou.api.external.util.cache.ApiBlacklistCache;
import com.baidu.beidou.api.external.util.service.ApiBlacklistMgr;
import com.baidu.ctclient.ITaskUsingErrorCode;

/**
 * 
 * ClassName: RefreshBlacklistCTTask <br>
 * Function: 刷新api黑名单缓存的CT调用hassian远程调用
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class RefreshBlacklistCTTask implements ITaskUsingErrorCode,
		ApplicationContextAware {

	private static final Log LOG = LogFactory.getLog(RefreshBlacklistCTTask.class);

	private ApplicationContext context;

	public boolean execute() {
		try {
			// api模块黑名单
			ApiBlacklistMgr apiBlacklistMgr = (ApiBlacklistMgr) context
					.getBean("apiBlacklistMgr");
			ApiBlacklistCache.BLACKLIST_USERID_SET = apiBlacklistMgr
					.getAllBlacklistUserids();
			return true;
		} catch (Exception e) {
			LOG.fatal(e.getMessage(), e);
			return false;
		}

	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

}
