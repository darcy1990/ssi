package com.winit.hessian;

public class DefaultSayHelloServiceImpl implements ISayHelloService {

	@Override
	public String doSayHello(String name) {
		return doSayHello(name, "hello");
	}

	@Override
	public String doSayHello(String name, String welcomeStr) {
		return name + ", " + welcomeStr;
	}

}
