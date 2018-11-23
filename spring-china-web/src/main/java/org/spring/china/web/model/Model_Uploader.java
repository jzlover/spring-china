package org.spring.china.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Model_Uploader implements Serializable{
	//单个文件的名称，一般是GUID名称
	private String fileName;
	//原始文件的名称
	private String originalName;
	//完整的URL地址
	private String url;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
