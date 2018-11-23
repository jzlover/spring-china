package org.spring.china.base.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.spring.china.base.util.Common;



/**
 * Created by jzlover on 2017/6/10.
 */
@SuppressWarnings("serial")
public class User implements Serializable{
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String userName;
    private Integer gender;
    private String nickName;
    private String signature;
    private Boolean avatar;
    private String password;
    private String passwordSalt;
    private String wxId;
    private Integer verified;
    
    
    
    
    private List<UserRole> roles=new ArrayList<UserRole>();

    /*
     * 以下为非数据库字段
     */
    //用户所有的话题，包括全部的status=null，即查询数据库所有status的字段
    private Integer topicCounts;
    //用户正常发表的话题数量，status=1
    private Integer topicStatusReleaseCounts;
    //用户放入草稿箱的话题数量，status=0
    private Integer topicStatusDraftCounts;
    //用户删除的话题数量，status=-1
    private Integer topicStatusDeletedCounts;
    
    private String wxAvatarUrl;
    
    private Integer unReadedMsgCount;
    
    

	public Integer getUnReadedMsgCount() {
		return unReadedMsgCount;
	}

	public void setUnReadedMsgCount(Integer unReadedMsgCount) {
		this.unReadedMsgCount = unReadedMsgCount;
	}

	public String getWxAvatarUrl() {
		return wxAvatarUrl;
	}

	public void setWxAvatarUrl(String wxAvatarUrl) {
		this.wxAvatarUrl = wxAvatarUrl;
	}

	public Integer getTopicCounts() {
		return topicCounts;
	}
    
	public void setTopicCounts(Integer topicCounts) {
		this.topicCounts = topicCounts;
	}
	
	public Integer getTopicStatusReleaseCounts() {
		return topicStatusReleaseCounts;
	}

	public void setTopicStatusReleaseCounts(Integer topicStatusReleaseCounts) {
		this.topicStatusReleaseCounts = topicStatusReleaseCounts;
	}

	public Integer getTopicStatusDraftCounts() {
		return topicStatusDraftCounts;
	}

	public void setTopicStatusDraftCounts(Integer topicStatusDraftCounts) {
		this.topicStatusDraftCounts = topicStatusDraftCounts;
	}

	public Integer getTopicStatusDeletedCounts() {
		return topicStatusDeletedCounts;
	}

	public void setTopicStatusDeletedCounts(Integer topicStatusDeletedCounts) {
		this.topicStatusDeletedCounts = topicStatusDeletedCounts;
	}

	public String getAvatarSmall() {
		return Common.GetPortraitUrl(avatar, id, "small");
	}
	
	public String getAvatarNormal() {
		return Common.GetPortraitUrl(avatar, id, "normal");
	}
	
	public String getAvatarLarge() {
		return Common.GetPortraitUrl(avatar, id, "large");
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	

	public Boolean getAvatar() {
		return avatar;
	}

	public void setAvatar(Boolean avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}
    
     
    
    

}
