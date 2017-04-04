package com.baidu.beidou.api.external.report.facade.impl;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.facade.ApiReportFacade;
import com.baidu.beidou.api.external.report.service.ApiReportMgr;
import com.baidu.beidou.api.external.report.vo.ApiReportView;
import com.baidu.beidou.api.external.report.vo.ReportRequestType;

/**
 * 
 * ClassName: ApiReportFacadeImpl  <br>
 * Function: jms代理，实现handleMessage接口与插入msg到queue中
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportFacadeImpl implements ApiReportFacade {

	private static final Log log = LogFactory.getLog(ApiReportFacadeImpl.class);
	
	private ApiReportMgr apiReportMgr;
	
	private JmsTemplate jmsTemplate;
	
	/**
	 * receive jms message
	 */
	public void handleMessage(ApiReportTask task) throws JMSException {
		log.info("get taskid=[" + task.getId() + "] from jms");
		apiReportMgr.process(task);
	}
	
	/**
	 * 
	 * addTask 添加任务到jms
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 4, 2012
	 */
	public void addTask(ApiReportTask task) {
		log.info("put taskid=[" + task.getId() + "] into jms");
		sendOptInformation(task);
	}

	/**
     * send jms message
     */
    private void sendOptInformation(ApiReportTask task) {
        getJmsTemplate().convertAndSend(task);
    }

	public ApiReportMgr getApiReportMgr() {
		return apiReportMgr;
	}

	public void setApiReportMgr(ApiReportMgr apiReportMgr) {
		this.apiReportMgr = apiReportMgr;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	
}
