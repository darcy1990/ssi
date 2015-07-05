package com.winit.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.winit.bean.User;
import com.winit.dao.UserDao;

public class MybatisTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao impl = (UserDao) ctx.getBean("ProxyUserDaoImpl"); // use proxy
		User u = new User();
		u.setId(1);
		User uu = impl.getUserById(u);
		System.out.println(uu.getCreateTime());
	}

}
