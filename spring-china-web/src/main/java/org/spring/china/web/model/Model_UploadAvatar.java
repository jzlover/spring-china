package org.spring.china.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Model_UploadAvatar implements Serializable{
	private String avatar;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
