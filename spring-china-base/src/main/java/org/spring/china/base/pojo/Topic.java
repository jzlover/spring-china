package org.spring.china.base.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.spring.china.base.util.Common;

 

/**
 * Created by jzlover on 2017/6/10.
 */
public class Topic implements Serializable{

    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Long categoryId;
    private Long userId;
    private String title;
    private String content;
    private Integer status;
    private Boolean isTop;
    private Integer viewCount;
    private String contentHtml;
    private Boolean isDigest;
    private Integer likeCount;
    private Integer commentCount;
    private Category category;
    private User user;


    private List<TopicLike> likes=new ArrayList<TopicLike>();
    private List<TopicComment> comments=new ArrayList<TopicComment>();
    private List<Tag> tags=new ArrayList<Tag>();

    /*
     * 以下为非字段成员
     */
    //是否已经点过赞
    private Boolean liked;
    
    public Boolean getLiked() {
		return liked;
	}



	public void setLiked(Boolean liked) {
		this.liked = liked;
	}



	public String getCreatedAtFormat() {
		if(createdAt!=null)
			return Common.FormatDate(createdAt);
		else
			return "";
	}
    
    
    
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public Boolean getIsDigest() {
		return isDigest;
	}
	public void setIsDigest(Boolean isDigest) {
		this.isDigest = isDigest;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}



	public List<TopicLike> getLikes() {
		return likes;
	}



	public void setLikes(List<TopicLike> likes) {
		this.likes = likes;
	}



	public List<TopicComment> getComments() {
		return comments;
	}



	public void setComments(List<TopicComment> comments) {
		this.comments = comments;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}
