package com.winit.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.winit.mq.sender.SimpleMessageSender;

public class MQTest {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		SimpleMessageSender send = (SimpleMessageSender) ctx.getBean("messageSender");
		for (int i = 0; i < 5; i++) {
			send.sendMessage("Message" + i);
		}
	}
}
