package org.spring.china.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Model_UserInitData implements Serializable {
	private int commentUnReadedCount;

	public int getCommentUnReadedCount() {
		return commentUnReadedCount;
	}

	public void setCommentUnReadedCount(int commentUnReadedCount) {
		this.commentUnReadedCount = commentUnReadedCount;
	}
	
}
