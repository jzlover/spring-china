package org.spring.china.base.pojo;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserMsg implements Serializable{
	private Long id;
	private Long senderUserId;
	private Long toUserId;
	private Integer type;
	private String content;
	private String contentHtml;
	private Date createdAt;
	private Date updatedAt;
	private User senderUser;
	private User toUser;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSenderUserId() {
		return senderUserId;
	}
	public void setSenderUserId(Long senderUserId) {
		this.senderUserId = senderUserId;
	}
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
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public User getSenderUser() {
		return senderUser;
	}
	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
	}
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
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
