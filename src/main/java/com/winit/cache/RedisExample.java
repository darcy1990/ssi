package com.winit.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.winit.bean.MQLoginLog;
import com.winit.constant.MQConstant;

/**
 * An example to show redis with spring-data
 * @author Matthew Yuan
 *
 */
public class RedisExample {
	
	private static final Object TAB = "	";

	@Autowired
	private RedisTemplate<String, String> template;
	
	@Resource(name="redisTemplate") // from JSR
	private ListOperations<String, MQLoginLog> listOps;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws MalformedURLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisExample example = (RedisExample) context.getBean("redisExample");
		
		ListOperations<String, MQLoginLog> listOps = example.listOps;
		ExecutorService executorService = example.executorService;
		
		Object o = listOps.leftPop(MQConstant.CACHED_LOGIN_LOG_KEY, 1 ,TimeUnit.SECONDS);
		while (o != null) {
			listOps.leftPop(MQConstant.CACHED_LOGIN_LOG_KEY, 1 ,TimeUnit.SECONDS);
		}
		
		MQLoginLog logPush = new MQLoginLog();
		logPush.setPlatform(1);
		logPush.setType(2);
		logPush.setThirdPartyType(2);
		logPush.setLoginInfo("123456");
		logPush.setResult(1);
		logPush.setResultType(5);
		logPush.setMemberId(123456L);
		logPush.setLoginIP("10.10.10.12");
		logPush.setLoginTime(new Date());
		logPush.setChannelId("123");
		logPush.setSubChannelId("446");
		
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			listOps.rightPush(MQConstant.CACHED_LOGIN_LOG_KEY, logPush);
//			System.out.println(i);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("push time: " + (t2 - t1));
		
		for (int i = 0; i < 5; i++) {
			executorService.execute(example.new popThread());
		}
		executorService.shutdown();
		
		long t3 = System.currentTimeMillis();
		System.out.println("pop time: " + (t3 - t2));

	}
	
	class popThread implements Runnable {

		@Override
		public void run() {
			try {
				MQLoginLog log = listOps.leftPop(MQConstant.CACHED_LOGIN_LOG_KEY, 1 ,TimeUnit.SECONDS);
				StringBuilder sb = new StringBuilder();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String loginDate = "";
				while (log != null) {
					int thirdPartyType = log.getThirdPartyType() >= 0 ? log.getThirdPartyType() : 0;
					log.setThirdPartyType(thirdPartyType);
					sb.append(log.getPlatform()).append(TAB);
					sb.append(log.getResult()).append(TAB);
					sb.append(log.getLoginInfo()).append(TAB);
					sb.append(log.getMemberId()).append(TAB);
					sb.append(log.getType()).append(TAB);
					sb.append(log.getResultType()).append(TAB);
					sb.append(log.getChannelId()).append(TAB);
					sb.append(log.getLoginIP()).append(TAB);
					loginDate = formatter.format(log.getLoginTime());
					sb.append(loginDate).append(TAB);
					sb.append(log.getThirdPartyType()).append(TAB);
					sb.append(log.getSubChannelId()).append(TAB);
					sb.append("\n");
					
					log = listOps.leftPop(MQConstant.CACHED_LOGIN_LOG_KEY, 1 ,TimeUnit.SECONDS);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
