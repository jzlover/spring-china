package org.spring.china.base.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.spring.china.base.pojo.UserMsg;

@SuppressWarnings("serial")
public class UserMsgHistory implements Serializable{
	private List<UserMsg> userMsgs=new ArrayList<UserMsg>();
	
	private int remainedMsgCount;

	public List<UserMsg> getUserMsgs() {
		return userMsgs;
	}

	public void setUserMsgs(List<UserMsg> userMsgs) {
		this.userMsgs = userMsgs;
	}

	public int getRemainedMsgCount() {
		return remainedMsgCount;
	}

	public void setRemainedMsgCount(int remainedMsgCount) {
		this.remainedMsgCount = remainedMsgCount;
	}
	
	
	
}
