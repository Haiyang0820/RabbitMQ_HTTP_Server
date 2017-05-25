package com.lhf.rabbitmq.model.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.lhf.rabbitmq.model.SPType;

@Component
public class FanoutBean implements SPType {
	private List<Object> message=null;
	private String exchange=null;
	private String queneName=null;
	private String bindingName=null;
	private Boolean isLasting;
	@Override
	public List<Object> getMessage() {
		return message;
	}
	@Override
	public void setMessage(List<Object> message) {
		this.message=message;
		
	}
	@Override
	public String getExchange() {
		return exchange;
	}
	@Override
	public void setExchange(String exchange) {
		this.exchange=exchange;
		
	}
	@Override
	public String getQueneName() {
		return queneName;
	}
	@Override
	public void setQueneName(String queneName) {
		this.queneName=queneName;
		
	}
	@Override
	public Boolean getIsLasting() {
		return isLasting;
	}
	@Override
	public void setIsLasting(Boolean isLasting) {
		this.isLasting=isLasting;
		
	}
	@Override
	public String getBindingName() {
		return bindingName;
	}
	@Override
	public void setBindingName(String bindingName) {
		this.bindingName=bindingName;
		
	}
	@Override
	public String getMatchingKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setMatchingKey(String matchingKey) {
		// TODO Auto-generated method stub
		
	}

}
