package com.winit.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.winit.hessian.ISayHelloService;

public class HessianTest {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ISayHelloService service = (ISayHelloService) ctx.getBean("sayHelloServiceClient");
		System.out.println(service.doSayHello("crab","come on!"));
	}

}
