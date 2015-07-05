package com.winit.inteceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class LogMethodInteceptor implements MethodInterceptor {
	
	private Logger logger = Logger.getLogger(LogMethodInteceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String info = invocation.getClass().getName() + " " + invocation.getMethod().getName();
		logger.info(info);
		
		Object result = invocation.proceed();
		return result;
	}

}
