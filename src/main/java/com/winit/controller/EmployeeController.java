package com.winit.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.winit.bean.Employee;
import com.winit.bean.EmployeeList;
import com.winit.bean.User;
import com.winit.dao.EmployeeDS;
import com.winit.dao.impl.UserDaoImpl;

@Controller
public class EmployeeController {
	
	private Logger logger = Logger.getLogger(EmployeeController.class);
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Autowired
	private RedisTemplate<String, String> cache;
	
	private EmployeeDS employeeDS;
	
	public void setEmployeeDS(EmployeeDS ds) {
		this.employeeDS = ds;
	}
	
	private Jaxb2Marshaller jaxb2Mashaller;
	
	public void setJaxb2Mashaller(Jaxb2Marshaller jaxb2Mashaller) {
		this.jaxb2Mashaller = jaxb2Mashaller;
	}

	private static final String XML_VIEW_NAME = "employees";
	
	@RequestMapping(method=RequestMethod.GET, value="/employee/{id}")
	public ModelAndView getEmployee(@PathVariable String id) {
		Employee e = employeeDS.get(Long.parseLong(id));
		return new ModelAndView(XML_VIEW_NAME, "object", e);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/employee/{id}")
	public ModelAndView updateEmployee(@RequestBody String body) {
		Source source = new StreamSource(new StringReader(body));
		Employee e = (Employee) jaxb2Mashaller.unmarshal(source);
		employeeDS.update(e);
		return new ModelAndView(XML_VIEW_NAME, "object", e);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/employee")
	public ModelAndView addEmployee(@RequestBody String body) {
		Source source = new StreamSource(new StringReader(body));
		Employee e = (Employee) jaxb2Mashaller.unmarshal(source);
		employeeDS.add(e);
		return new ModelAndView(XML_VIEW_NAME, "object", e);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/employee/{id}")
	public ModelAndView removeEmployee(@PathVariable String id) {
		employeeDS.remove(Long.parseLong(id));
		List<Employee> employees = employeeDS.getAll();
		EmployeeList list = new EmployeeList(employees);
		return new ModelAndView(XML_VIEW_NAME, "employees", list);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/employees")
	public ModelAndView getEmployees() {
		
		Employee employee = new Employee();
		if (cache.opsForValue().get("1") != null) { // get from cache
			employee.setName(cache.opsForValue().get("1")); 
			logger.info("get value from cache");
		}
		else {
			User u = new User();
			u.setId(1);
			User uu = userDaoImpl.getUserById(u);
			
			employee.setId(1);
			employee.setName(uu.getName());
			
			cache.opsForValue().set("1", uu.getName()); // set to cache
			logger.info("set value to cache");
		}
		employee.setEmail("yuanzm1990@gmail.com");

		List<Employee> employees = new ArrayList<Employee>();
		employees.add(employee);
		
		EmployeeList list = new EmployeeList(employees);
		return new ModelAndView(XML_VIEW_NAME, "employees", list);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/ajax")
	public void ajax(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String name = (String) request.getParameter("name");
		String location = (String) request.getParameter("location");
		response.getWriter().write("we get name and location: " + name + ", " + location);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/ajaxJson")
	public void ajaxJson(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String name = (String) request.getParameter("name");
		String location = (String) request.getParameter("location");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("location", location);
		String json = JSON.toJSONString(map,true);
		response.getWriter().write(json);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String name = (String) request.getParameter("name");
		String password = (String) request.getParameter("password");
		
		if (name.equals("darcy") && password.equals("oranges")) {
			response.getWriter().write("login in successfully !");
//			Cookie nameCookie = new Cookie("name",name);
//			Cookie passwordCookie = new Cookie("password",password);
//			response.addCookie(nameCookie);
//			response.addCookie(passwordCookie);
			HttpSession session = request.getSession();
			session.setAttribute("name", name);
			session.setAttribute("password", password);
		}
		else {
			response.getWriter().write("login in failed !");
		}
		response.getWriter().flush();
		response.getWriter().close();
		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/loginByCookie")
	public void loginByCookie(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String name = null, password = null;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("name")) {
				name = cookies[i].getValue();
			}
			if (cookies[i].getName().equals("password")) {
				password = cookies[i].getValue();
			}
		}
		
		HttpSession session = request.getSession();
		name = (String) session.getAttribute("name");
		password = (String) session.getAttribute("password");
		
		if (name != null && name.equals("darcy") && password != null && password.equals("oranges")) {
			response.getWriter().write("login in by cookie successfully !");
		}
		else {
			response.getWriter().write("login in by cookie failed !");
		}
		response.getWriter().flush();
		response.getWriter().close();
		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/loginBySession")
	public void loginBySession(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("name");
		String password = (String) session.getAttribute("password");
		
		if (name != null && name.equals("darcy") && password != null && password.equals("oranges")) {
			response.getWriter().write("login in by session successfully !");
		}
		else {
			response.getWriter().write("login in by session failed !");
		}
		response.getWriter().flush();
		response.getWriter().close();
		
	}
	
}
