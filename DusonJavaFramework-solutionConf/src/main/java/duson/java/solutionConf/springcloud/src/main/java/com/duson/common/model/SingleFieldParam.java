package com.facewnd.ad.common.model;

import java.io.Serializable;

public class SingleFieldParam<T> implements Serializable {
	private T val;

	public T getVal() {
		return val;
	}

	public void setVal(T val) {
		this.val = val;
	}

}
