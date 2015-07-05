package com.winit.mq.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

public class SimpleMessageListener implements MessageListener {
	
	private Logger logger = Logger.getLogger(SimpleMessageListener.class);

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			logger.info("Message received: " + textMessage.getText());
		} catch (JMSException e) {
			logger.error(e.getMessage());
		}
	}

}
