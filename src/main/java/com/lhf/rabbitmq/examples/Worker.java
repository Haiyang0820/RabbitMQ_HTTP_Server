package com.lhf.rabbitmq.examples;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {//多开几个消费者进程

	private static final String TASK_QUEUE_NAME = "task_queue";

	  public static void main(String[] argv) throws Exception {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    final Connection connection = factory.newConnection();
	    final Channel channel = connection.createChannel();

	    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    channel.basicQos(1);

	    final Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	        String message = new String(body, "UTF-8");

	        System.out.println(" [x] Received '" + message + "'");
	        try {
	          doWork(message);
	        } finally {
	          System.out.println(" [x] Done");
	          channel.basicAck(envelope.getDeliveryTag(), false);
	        }
	      }
	    };
	    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
//	    Using this code we can be sure that 
//	    even if you kill a worker using CTRL+C while it was processing a message, 
//	    nothing will be lost. Soon after the worker dies 
//	    all unacknowledged messages will be redelivered.
	  }

	  private static void doWork(String task) {
	    for (char ch : task.toCharArray()) {
	      if (ch == '.') {
	        try {
	          Thread.sleep(1000);
	        } catch (InterruptedException _ignored) {
	          Thread.currentThread().interrupt();
	        }
	      }
	    }
	  }

}
