package com.lhf.rabbitmq.action;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lhf.rabbitmq.service.TestService;


public class helloTest {
//	@Resource
//	private  Hello hello;
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
////		System.out.println(hello.getMessage());
//		  BeanFactory factory = new ClassPathXmlApplicationContext("Spring.xml");
////	        Hello action = factory.getBean("Hello", Hello.class);
//	        System.out.println(hello.getMessage());
//		
//	}
	@Test//
	public void test(){
		BeanFactory factory = new ClassPathXmlApplicationContext("Spring.xml");
		Hello hello=(Hello) factory.getBean("hello");
		System.out.println(hello.getMessage());
	}
	@Test//
	public void testService(){
		BeanFactory factory = new ClassPathXmlApplicationContext("Spring.xml");
		TestService testService=(TestService) factory.getBean("testServiceImpl");
		testService.testSay();
	}

}
