package org.spring.china.base.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserAllMsgCount implements Serializable{
	public Integer commentCount;
	public Integer privateMsgCount;
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public Integer getPrivateMsgCount() {
		return privateMsgCount;
	}
	public void setPrivateMsgCount(Integer privateMsgCount) {
		this.privateMsgCount = privateMsgCount;
	}
 
	
}
