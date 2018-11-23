package org.spring.china.base.result;

import java.io.Serializable;
import java.util.Date;

import org.spring.china.base.util.Common;
 

@SuppressWarnings("serial")
public class UserRelatedComment implements Serializable{
	private Integer type;
	private String title;
	private Long id;
	private Long cId;	
	private Long rId;
	private Long userId;
	private String nickName;
	private String contentHtml;
	private Date createdAt;
	private Boolean readed;
	public String getCreatedAtFormat() {
		if(createdAt!=null)
			return Common.FormatDate(createdAt);
		else
			return "";
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getcId() {
		return cId;
	}

	public void setcId(Long cId) {
		this.cId = cId;
	}

	public Long getrId() {
		return rId;
	}

	public void setrId(Long rId) {
		this.rId = rId;
	}

	public Boolean getReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}
	
	
}
