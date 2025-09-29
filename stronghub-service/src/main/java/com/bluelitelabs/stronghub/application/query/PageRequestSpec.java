package com.bluelitelabs.stronghub.application.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

public class PageRequestSpec {
	@PositiveOrZero
	private Integer page;
	@Min(1)
	@Max(100)
	private Integer size;
	private String[] sort; // ej: name,asc | createdAt,desc

	public PageRequestSpec() {
	}

	public PageRequestSpec(Integer page, Integer size, String[] sort) {
		this.page = page;
		this.size = size;
		this.sort = sort;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String[] getSort() {
		return sort;
	}

	public void setSort(String[] sort) {
		this.sort = sort;
	}
}
