package com.lhf.rabbitmq.model;

import java.util.List;

public interface SPType {

	public List<Object> getMessage();
	public void setMessage(List<Object> message);
	public String getExchange();
	public void setExchange(String exchange) ;
	public String getQueneName();
	public void setQueneName(String queneName);
	public Boolean getIsLasting();
	public void setIsLasting(Boolean isLasting);
	public String getBindingName();
	public void setBindingName(String matchingKey);
	public String getMatchingKey();
	public void setMatchingKey(String matchingKey);
}
