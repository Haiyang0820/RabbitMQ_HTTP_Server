package com.lhf.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.ConnectionFactory;

public interface RabbitMQBase {
	public ConnectionFactory getFactory()throws IOException;
}
