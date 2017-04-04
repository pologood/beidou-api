package com.baidu.beidou.api.external.report.facade;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.vo.ApiReportView;

/**
 * 
 * ClassName: ApiReportMessageConverter  <br>
 * Function: JMS队列中对象转换器
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportMessageConverter implements MessageConverter{

	private static transient Log logger = LogFactory.getLog(ApiReportMessageConverter.class);
	
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		if (logger.isDebugEnabled()) {
            logger.debug("Receive JMS message: " + message);
        }
 
        if (message instanceof ObjectMessage) {
            ObjectMessage oMsg = (ObjectMessage) message;
 
            if (oMsg instanceof ActiveMQObjectMessage) {
                ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage) oMsg;
 
                try {
                	ApiReportTask msg = (ApiReportTask) aMsg.getObject();
 
                    return msg;
                } catch (Exception e) {
                    logger.error("Message:[" + message + "] is not a instance of ApiReportTask.");
                    throw new JMSException("Message:[" + message + "] is not a instance of ApiReportTask.");
                }
            } else {
                logger.error("Message:[" + message + "] is not " 
				    + "a instance of ActiveMQObjectMessage[ApiReportTask].");
                throw new JMSException("Message:[" + message + "] is not " 
				    + "a instance of ActiveMQObjectMessage[ApiReportTask].");
            }
        } else {
            logger.error("Message:[" + message + "] is not a instance of ObjectMessage.");
            throw new JMSException("Message:[" + message + "] is not a instance of ObjectMessage.");
        }
	}

	public Message toMessage(Object obj, Session session)
			throws JMSException, MessageConversionException {
		if (logger.isDebugEnabled()) {
            logger.debug("Convert ApiReportTask object to JMS message: " + obj);
        }
 
        if (obj instanceof ApiReportTask) {
            ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
            msg.setObject((ApiReportTask) obj);
 
            return msg;
        } else {
            logger.error("Object:[" + obj + "] is not a instance of ApiReportTask.");
            throw new JMSException("Object:[" + obj + "] is not a instance of ApiReportTask.");
        }
	}


}
