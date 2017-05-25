package com.lhf.rabbitmq.action;

import org.springframework.stereotype.Controller;
@Controller("hello")
public class Hello {
	private String message="asd";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
