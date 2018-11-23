package org.spring.china.base.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jzlover on 2017/6/10.
 */
@SuppressWarnings("serial")
public class TopicLike implements Serializable{
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Long userId;
    private Long topicId;
    private User user;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

    
}
