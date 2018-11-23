package org.spring.china.web.model;

import java.io.Serializable;

public class Model_UserTipInfo implements Serializable{
	private Long id;
	private String userName;
	private String nickName;
	private String signature;
	private String avatarNormal;
	private String avatarSmall;
	private Integer mine;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAvatarNormal() {
		return avatarNormal;
	}
	public void setAvatarNormal(String avatarNormal) {
		this.avatarNormal = avatarNormal;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getMine() {
		return mine;
	}
	public void setMine(Integer mine) {
		this.mine = mine;
	}
	public String getAvatarSmall() {
		return avatarSmall;
	}
	public void setAvatarSmall(String avatarSmall) {
		this.avatarSmall = avatarSmall;
	}
	
	
	
}
