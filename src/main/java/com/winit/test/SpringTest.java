package com.winit.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.winit.bean.HelloBean;

public class SpringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		HelloBean bean = (HelloBean) ctx.getBean("helloBean");
		System.out.println(bean.getHelloWorld());
	}

}
