package org.spring.china.web.model;

import java.security.Timestamp;
import java.util.Date;

public class Model_QueryPagely {
	private int page;
	private int pageSize;
	private Long prevDate;

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

	public Long getPrevDate() {
		return prevDate;
	}

	public void setPrevDate(Long prevDate) {
		this.prevDate = prevDate;
	}
}
