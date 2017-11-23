package com.facewnd.ad.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestPage<T> implements Serializable {
	private List<T> list = new ArrayList<T>();

	private long total;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	public void add(T entity) {
		this.list.add(entity);
	}
	
	public void add(List<T> collection) {
		this.list.addAll(collection);
	}
	
	public List<T> getList() {
		return this.list;
	}
}
