package com.facewnd.ad.common.model;

import java.util.ArrayList;
import java.util.List;

public class RestPage<T> extends ArrayList<T> {
	private int total;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<T> getList() {
		return this;
	}
}
