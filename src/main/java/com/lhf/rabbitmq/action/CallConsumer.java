package com.lhf.rabbitmq.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lhf.rabbitmq.pubScribMethod;
import com.lhf.rabbitmq.model.SPType;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

@Scope("prototype")//设置为多例
@Controller
public class CallConsumer implements pubScribMethod{
	@Autowired
	@Qualifier("directBean")
	private SPType directType;
	@Autowired
	@Qualifier("fanoutBean")
	private SPType fanoutType;
	@Autowired
	@Qualifier("topicBean")
	private SPType topicType;

	/**
	 * fanout	发布订阅策略-此为轮询发送策略
	 * @param   isLasting 		是否消息队列中保持持久态
	   @param	message   		消息内容
	   @param	queueName		队列名称
	   @param	exchangeName	转发器名称
	 * @return	ResponseEntity<Object> 消息内容
	 * @author  lihf
	 */
	@RequestMapping(value="/callFonOutConsumer",method=RequestMethod.GET)
	public ResponseEntity<Object> callFonOutConsumer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="exchangeName",required=true)String exchangeName){
		HttpServletResponse rsp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		ArrayList<String> list=new ArrayList<String>();
		JSONArray json;
		String messageOut=null;
		Connection connection = null;
		Channel channel = null ;
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("127.0.0.1");//消息队列服务器地址
		try {
			connection =factory.newConnection();
			channel=connection.createChannel();
			final Timer timer = new Timer();
	        TimerTask tt=new TimerTask() { 
	            @Override                                                                                                                                                                                                                                                                                                                                                                                
	            public void run() {
	                System.out.println("到点啦！");
	                timer.cancel();
	            }
	        };
	        timer.schedule(tt,1111);
			channel.exchangeDeclare(exchangeName, pubScribMethod.FANOUT);
		    channel.queueDeclare(queueName,true,false,false,null).getQueue();
		    channel.queueBind(queueName, exchangeName, "");
		    QueueingConsumer  consumer=new QueueingConsumer(channel);
		    channel.basicConsume(queueName, true, consumer);
		    QueueingConsumer.Delivery delivery;
	        try {
	        	PrintWriter out= rsp.getWriter();
				while((delivery=consumer.nextDelivery(100))!= null){
					messageOut = new String(delivery.getBody());
				    System.out.println("收到消息'" + messageOut + "'");
				    list.add(messageOut);
				}
				json=JSONArray.fromObject(list);
				out.print(json);
				channel.close();
				connection.close();
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/callDirectConsumer",method=RequestMethod.GET)
	public ResponseEntity<Object> callDirectConsumer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="bindNames",required=true)String bindNames,
			@RequestParam(name="exchangeName",required=true)String exchangeName){
		HttpServletResponse rsp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		ArrayList<Object> list=new ArrayList<Object>();
		JSONArray json;
		String messageOut=null;
		Connection connection = null;
		Channel channel = null ;
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("127.0.0.1");//消息队列服务器地址
		try {
			connection =factory.newConnection();
			channel=connection.createChannel();
			final Timer timer = new Timer();
	        TimerTask tt=new TimerTask() { 
	            @Override                                                                                                                                                                                                                                                                                                                                                                                
	            public void run() {
	                System.out.println("到点啦！");
	                timer.cancel();
	            }
	        };
	        timer.schedule(tt,1111);
			channel.exchangeDeclare(exchangeName, pubScribMethod.DIRECT);
		    channel.queueDeclare(queueName,true,false,false,null).getQueue();
		    channel.queueBind(queueName, exchangeName,bindNames);
		    QueueingConsumer  consumer=new QueueingConsumer(channel);
		    channel.basicConsume(queueName, true, consumer);
		    QueueingConsumer.Delivery delivery;
	        try {
	        	PrintWriter out= rsp.getWriter();
				while((delivery=consumer.nextDelivery(100))!= null){
					messageOut = new String(delivery.getBody());
				    System.out.println("收到消息'" + messageOut + "'");
				    list.add(messageOut);
				}
				if(list.size()!=0){
					 directType.setMessage(list);
				}
				json=JSONArray.fromObject(directType.getMessage());
				out.print(json);
				channel.close();
				connection.close();
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * topic	发布订阅策略
	 * @param   isLasting 		是否消息队列中保持持久态
	   @param	message   		消息内容
	   @param	queueName		队列名称
	   @param	exchangeName	转发器名称
	 * @return	ResponseEntity<Object> 消息内容
	 * @author  lihf
	 */
	@RequestMapping(value="/callTopicConsumer",method=RequestMethod.GET)
	public ResponseEntity<Object> callTopicConsumer(
			@RequestParam(name="isLasting",required=true)Boolean isLasting,
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="exchangeName",required=true)String exchangeName,
			@RequestParam(name="macthingKey",required=true)String macthingKey
			){
		HttpServletResponse rsp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		ArrayList<Object> list=new ArrayList<Object>();
		JSONArray json;
		String messageOut;
		String[] bindings={"default"};
		if(macthingKey!=null&&!macthingKey.equals("")){
			if(-1==macthingKey.indexOf(',')){
				bindings[0]=macthingKey;
			}else {
				bindings=macthingKey.split(",");
			}
		}
		try {
			Connection connection = null;
			Channel channel = null ;
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("127.0.0.1");//消息队列服务器地址
			connection =factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchangeName, pubScribMethod.TOPIC);
		    channel.queueDeclare(queueName,true,false,false,null).getQueue();
			for (String bind : bindings) {
				channel.queueBind(queueName, exchangeName, bind);
			}
			QueueingConsumer  consumer=new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			QueueingConsumer.Delivery delivery;
	        try {
	        	PrintWriter out= rsp.getWriter();
				while((delivery=consumer.nextDelivery(100))!= null){
					messageOut = new String(delivery.getBody());
				    System.out.println("收到消息'" + messageOut + "'");
				    list.add(messageOut);
				}
				if(list.size()!=0){
					topicType.setMessage(list);
				}
				json=JSONArray.fromObject(topicType.getMessage());
				out.print(json);
				channel.close();
				connection.close();
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/callRPCConsumer",method=RequestMethod.GET)
	public ResponseEntity<Object> callRPCConsumer(
			@RequestParam(name="queueName",required=true)String queueName,
			@RequestParam(name="message",required=true)String message){
		HttpServletResponse rsp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		ArrayList<Object> list=new ArrayList<Object>();
		JSONArray json;
		String response = null;
		Connection connection = null;
		Channel channel = null;
		String requestQueueName = queueName;
		String replyQueueName = null;
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			replyQueueName = channel.queueDeclare().getQueue();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
	    	PrintWriter out= rsp.getWriter();
			response = call(message,channel,requestQueueName,replyQueueName);
			list.add(response);
			json=JSONArray.fromObject(list);
			out.print(json);
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
			    channel.close();
			    connection.close();
			}catch (IOException _ignore) {}
		}
		return null;
	}
	public String call(String message,Channel channel,String requestQueueName,String replyQueueName) throws IOException, InterruptedException {
	    String corrId = UUID.randomUUID().toString();
	    AMQP.BasicProperties props = new AMQP.BasicProperties
	            .Builder()
	            .correlationId(corrId)
	            .replyTo(replyQueueName)
	            .build();
	    channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
	    final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);
	    channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	        if (properties.getCorrelationId().equals(corrId)) {
	          response.offer(new String(body, "UTF-8"));
	        }
	      }
	    });
	    return response.take();
	  }
	@Test
	public void test(){
	}

	
}
