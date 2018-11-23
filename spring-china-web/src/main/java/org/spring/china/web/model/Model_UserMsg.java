package org.spring.china.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Model_UserMsg implements Serializable{
	private Long toUserId;
	private Integer type;
	private String content;
	private String contentHtml;
	public Long getToUserId() {
		return toUserId;
	}
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	
	
}
