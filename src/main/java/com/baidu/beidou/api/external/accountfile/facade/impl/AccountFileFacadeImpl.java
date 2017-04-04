package com.baidu.beidou.api.external.accountfile.facade.impl;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jms.core.JmsTemplate;
import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileFacade;
import com.baidu.beidou.api.external.accountfile.service.AccountFileMgr;

/**
 * 
 * ClassName: AccountFileFacadeImpl  <br>
 * Function: jms代理，实现handleMessage接口与插入msg到queue中
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountFileFacadeImpl implements AccountFileFacade {

	private static final Log log = LogFactory.getLog(AccountFileFacadeImpl.class);
	
	private AccountFileMgr accountFileMgr;
	
	private JmsTemplate jmsTemplate;
	
	private RabbitTemplate rabbitTemplate;
	
	//消息是否jms和rabbitmq同时写入 true:同时写入多份，false：只写入rabbitmq，原有方式下线
	private boolean enable;
	
	/**
	 * receive jms message
	 */
	public void handleMessage(ApiAccountFileTask task) throws JMSException {
		log.info("get taskid=[" + task.getId() + "] from jms");
		accountFileMgr.process(task);
	}
	
	/**
	 * 
	 * addTask 添加任务到jms
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 4, 2012
	 */
	public void addTask(ApiAccountFileTask task) {
		log.info("put taskid=[" + task.getId() + "] into jms");
		sendOptInformation(task);
	}

	/**
     * send jms message
     */
    private void sendOptInformation(ApiAccountFileTask task) {
    	if (enable) {
    		getJmsTemplate().convertAndSend(task);
    	}
        
        try {
        	rabbitTemplate.convertAndSend(task);
        	log.info("taskid=[" + task.getId() + "] add into rabbitmq success");
        } catch (Exception e) {
        	log.error("add message into rabbitmq fail . taskid : " + task.getId(), e);
        }
    }

	public AccountFileMgr getAccountFileMgr() {
		return accountFileMgr;
	}

	public void setAccountFileMgr(AccountFileMgr accountFileMgr) {
		this.accountFileMgr = accountFileMgr;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
