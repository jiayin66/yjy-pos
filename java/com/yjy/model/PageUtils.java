package com.yjy.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ApiModel("分页工具类")
public class PageUtils<T> {
	private List<T> data;
	@ApiParam("总数")
	private long sum;
	public PageUtils() {
		super();
	}
	public PageUtils(List<T> data, long sum) {
		super();
		this.data = data;
		this.sum = sum;
	}
	
}
