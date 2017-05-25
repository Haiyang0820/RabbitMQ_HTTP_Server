package com.lhf.rabbitmq.examples;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

  private static final String EXCHANGE_NAME = "logs";
  private static final String TASK_QUEUE_NAME = "task_queue";
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    //声明 '转发器' 以及 '转发策略'
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    int n=10;
	String[] sb = new String[n];
    for(int i=0;i<n;i++){
    	sb[i] = "message:"+i;
    }
    //声明 '队列名称' 以及 '持久化于队列'
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    String message=getMessage(sb);
    //发送消息
    channel.basicPublish(EXCHANGE_NAME, TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  private static String getMessage(String[] strings){
    if (strings.length < 1)
    	    return "info: Hello World!";
    return joinStrings(strings, " ");
  }

  private static String joinStrings(String[] strings, String delimiter) {
    int length = strings.length;
    if (length == 0) return "";
    StringBuilder words = new StringBuilder(strings[0]);
    for (int i = 1; i < length; i++) {
        words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }
}