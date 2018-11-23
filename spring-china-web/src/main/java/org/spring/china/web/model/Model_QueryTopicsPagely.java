package org.spring.china.web.model;

public class Model_QueryTopicsPagely extends Model_QueryPagely{
	private Integer status;
	private Long categoryId;
	private Long tagId;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
	
}
