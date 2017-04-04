package com.baidu.beidou.api.external.accountfile.facade;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;

/**
 * 
 * ClassName: ApiReportMessageConverter  <br>
 * Function: JMS队列中对象转换器
 *
 * @author zhangxu
 * @version 2.0.1
 * @since cpweb357
 * @date Apr 4, 2012
 */
public class AccountFileMessageConverter implements MessageConverter{

	private static transient Log logger = LogFactory.getLog(AccountFileMessageConverter.class);
	
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
                	ApiAccountFileTask msg = (ApiAccountFileTask) aMsg.getObject();
 
                    return msg;
                } catch (Exception e) {
                    logger.error("Message:[" + message + "] is not a instance of ApiAccountFileTask.");
                    throw new JMSException("Message:[" + message + "] is not a instance of ApiAccountFileTask.");
                }
            } else {
                logger.error("Message:[" + message + "] is not " 
				    + "a instance of ActiveMQObjectMessage[ApiAccountFileTask].");
                throw new JMSException("Message:[" + message + "] is not " 
				    + "a instance of ActiveMQObjectMessage[ApiAccountFileTask].");
            }
        } else {
            logger.error("Message:[" + message + "] is not a instance of ObjectMessage.");
            throw new JMSException("Message:[" + message + "] is not a instance of ObjectMessage.");
        }
	}

	public Message toMessage(Object obj, Session session)
			throws JMSException, MessageConversionException {
		if (logger.isDebugEnabled()) {
            logger.debug("Convert ApiAccountFileTask object to JMS message: " + obj);
        }
 
        if (obj instanceof ApiAccountFileTask) {
            ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
            msg.setObject((ApiAccountFileTask) obj);
 
            return msg;
        } else {
            logger.error("Object:[" + obj + "] is not a instance of ApiAccountFileTask.");
            throw new JMSException("Object:[" + obj + "] is not a instance of ApiAccountFileTask.");
        }
	}


}
