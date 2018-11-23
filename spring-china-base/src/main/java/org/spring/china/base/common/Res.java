package org.spring.china.base.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Res<T> implements Serializable{
	public Res(){
		this.code=0;
		this.msg="成功";
	}
	private int code;
	private String msg;
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}
