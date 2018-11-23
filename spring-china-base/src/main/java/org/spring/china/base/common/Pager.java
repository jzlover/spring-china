package org.spring.china.base.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pager implements Serializable{
	private int counts;
	private int page;
	private int pageSize;
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
 
	
	
}
