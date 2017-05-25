package com.lhf.rabbitmq.examples;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogs {
  private static final String TASK_QUEUE_NAME = "task_queue";
  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    //声明转发器 '名字' 以及 '转发策略'
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    //声明队列名称并设置为持久与队列中的
    String queueName = channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null).getQueue();
    //绑定 '转发器' 以及 '队列'
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
//    Consumer consumer = new DefaultConsumer(channel) {
//      @Override
//      public void handleDelivery(String consumerTag, Envelope envelope,
//                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
//        String message = new String(body, "UTF-8");
////        System.out.println(" [x] Received '" + message + "'");
//      }
//    };
    
    QueueingConsumer  qc=new QueueingConsumer(channel);
    channel.basicConsume(queueName, true, qc);
//    while (true) {
//    	GetResponse response = channel.basicGet(TASK_QUEUE_NAME, true);
//	    if(response!=null){
//	    	System.out.println(new String(response.getBody()));
//	    }
//    }
//    	if(qc.nextDelivery()==null)
//    		System.out.println("a");
//    	else {
//    		System.out.println("b");
//    	}
        QueueingConsumer.Delivery delivery;  
        System.out.println("a");  
        while((delivery=qc.nextDelivery(1000))!= null){//sao
        	System.out.println("b");  
        	String message = new String(delivery.getBody());  
            System.out.println("收到消息'" + message + "'");  
        }
        channel.close();
        connection.close();
 
  }
}