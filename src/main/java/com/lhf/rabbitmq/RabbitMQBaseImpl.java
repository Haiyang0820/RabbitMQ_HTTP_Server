package com.lhf.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQBaseImpl implements RabbitMQBase {

	@Override
	public ConnectionFactory getFactory() throws IOException {
		return new ConnectionFactory();
	}
	
}
