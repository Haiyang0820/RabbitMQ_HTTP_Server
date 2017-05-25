package com.lhf.rabbitmq.action;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lhf.rabbitmq.pubScribMethod;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

//@Scope("prototype")//设置为多例
@Controller
public class CallProducer implements pubScribMethod{

	/**
	 * fanout	发布订阅策略
	 * @param   isLasting 		是否消息队列中保持持久态
	   @param	message   		消息内容
	   @param	queueName		队列名称
	   @param	exchangeName	转发器名称
	 * @return	ResponseEntity<Object> 发布状态
	 * @author  lihf
	 */
	@RequestMapping(value="/callFonOutProducer",method=RequestMethod.GET)
	public ResponseEntity<Object> callFonOutProducer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="message",required=true)String message,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="exchangeName",required=true)String exchangeName){

			Connection connection=null;
			Channel channel = null ;
		try {
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("127.0.0.1");//消息队列服务器地址
			connection =factory.newConnection();
			channel=connection.createChannel();
			
			channel.exchangeDeclare(exchangeName, pubScribMethod.FANOUT);
			channel.queueDeclare(queueName, isLasting, false, false, null);
			channel.basicPublish(exchangeName, queueName, null, message.getBytes("UTF-8"));
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
		//TODO 建立一个对象通过ResponseEntity返回消息发布状态
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * direct	发布订阅策略
	 * @param   isLasting 		是否消息队列中保持持久态
	   @param	message   		消息内容
	   @param	queueName		队列名称
	   @param	exchangeName	转发器名称
	 * @return	ResponseEntity<Object> 发布状态
	 * @author  lihf
	 *///待解决 ： direct的群发功能
	@RequestMapping(value="/callDirectProducer",method=RequestMethod.GET)
	public ResponseEntity<Object> callDirectProducer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="message",required=true)String message,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="exchangeName",required=true)String exchangeName,//转发器名称无用 类型相同名称不同效果一样
			@RequestParam(name="bindNames",required=true)String bindName){
		System.out.println("call start");
			Connection connection=null;
			Channel channel = null ;
		try {
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("127.0.0.1");//消息队列服务器地址
			connection =factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchangeName, pubScribMethod.DIRECT);
			channel.queueDeclare(queueName, isLasting, false, false, null);
			channel.basicPublish(exchangeName, bindName, null, message.getBytes("UTF-8"));
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * topic	发布订阅策略
	 * @param   isLasting 		是否消息队列中保持持久态
	   @param	message   		消息内容
	   @param	queueName		队列名称
	   @param	exchangeName	转发器名称
	   @param	routingKey		匹配关键字
	 * @return	ResponseEntity<Object> 发布状态
	 * @author  lihf
	 */
	@RequestMapping(value="/callTopicProducer",method=RequestMethod.GET)
	public ResponseEntity<Object> callTopicProducer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="message",required=true)String message,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="exchangeName",required=true)String exchangeName,
			@RequestParam(name="macthingKey",required=true)String macthingKey){
			Connection connection=null;
			Channel channel = null ;
		try {
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("127.0.0.1");//消息队列服务器地址
			connection =factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchangeName, pubScribMethod.TOPIC);
			channel.queueDeclare(queueName, isLasting, false, false, null);
			channel.basicPublish(exchangeName, macthingKey, null, message.getBytes("UTF-8"));
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/callRPCProducer",method=RequestMethod.GET)
	public void callRPCProducer(String queueName){
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = null;
	    try {
	      connection      = factory.newConnection();
	      Channel channel = connection.createChannel();
	      channel.queueDeclare(queueName, false, false, false, null);
	      channel.basicQos(1);
	      System.out.println(" [x] Awaiting RPC requests");
	      Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	          AMQP.BasicProperties replyProps = new AMQP.BasicProperties
	                  .Builder()
	                  .correlationId(properties.getCorrelationId())
	                  .build();
	          String response = "";
	          try {
	            String message = new String(body,"UTF-8");
	            int n = Integer.parseInt(message);
	            System.out.println(" [.] fib(" + message + ")");
	            response += fib(n);
	          }
	          catch (RuntimeException e){
	            System.out.println(" [.] " + e.toString());
	          }
	          finally {
	            channel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
	            channel.basicAck(envelope.getDeliveryTag(), false);
	          }
	        }
	      };
	      channel.basicConsume(queueName, false, consumer);
	      while(true) {
	        try {
	          Thread.sleep(100);
	        } catch (InterruptedException _ignore) {}
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	      return;
	    }
	    finally {
	      if (connection != null)
	        try {
	          connection.close();
	        } catch (IOException _ignore) {
	        	return;
	        }
	      return;
	    }
	}
	private static int fib(int n) {
	    if (n ==0) return 0;
	    if (n == 1) return 1;
	    return fib(n-1) + fib(n-2);
	}
}
