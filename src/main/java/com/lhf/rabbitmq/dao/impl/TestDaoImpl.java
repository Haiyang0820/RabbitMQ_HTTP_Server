package com.lhf.rabbitmq.dao.impl;

import org.springframework.stereotype.Repository;

import com.lhf.rabbitmq.dao.TestDao;
@Repository
public class TestDaoImpl implements TestDao {

	@Override
	public void testSay() {
		System.out.println("this is daoTest");
	}

}
