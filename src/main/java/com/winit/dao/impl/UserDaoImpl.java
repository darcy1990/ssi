package com.winit.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.winit.bean.User;
import com.winit.dao.UserDao;

public class UserDaoImpl implements UserDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public User getUserById(User user) {
		return (User)sqlSession.selectOne("getUser", user);
	}

}
