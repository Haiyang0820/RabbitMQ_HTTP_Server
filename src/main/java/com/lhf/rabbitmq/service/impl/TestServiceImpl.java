package com.lhf.rabbitmq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lhf.rabbitmq.dao.TestDao;
import com.lhf.rabbitmq.service.TestService;
@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private TestDao testDao;
	@Override
	public void testSay() {
		// TODO Auto-generated method stub
		this.testDao.testSay();
	}

}
