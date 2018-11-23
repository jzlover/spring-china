package org.spring.china.web.model;

import org.spring.china.base.pojo.User;



@SuppressWarnings("serial")
public class Model_RequestMessage {
	private Integer msgType;
	private User user;
	

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
