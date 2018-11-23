package org.spring.china.base.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jzlover on 2017/6/10.
 */
@SuppressWarnings("serial")
public class TopicComment implements Serializable{
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Long userId;
    private Long topicId;
    private String content;
    private String contentHtml;
    private Boolean isTop;
    private Integer likeCount;
    private Integer status;
    private User user;
    
    
    private Boolean liked;
    
    public Boolean getLiked() {
		return liked;
	}
	public void setLiked(Boolean liked) {
		this.liked = liked;
	}
	private List<TopicCommentLike> likes=new ArrayList<TopicCommentLike>();
    
	private List<TopicCommentTo> tos=new ArrayList<TopicCommentTo>();
	
	
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
	
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<TopicCommentLike> getLikes() {
		return likes;
	}
	public void setLikes(List<TopicCommentLike> likes) {
		this.likes = likes;
	}
	public List<TopicCommentTo> getTos() {
		return tos;
	}
	public void setTos(List<TopicCommentTo> tos) {
		this.tos = tos;
	}

	
    
}
