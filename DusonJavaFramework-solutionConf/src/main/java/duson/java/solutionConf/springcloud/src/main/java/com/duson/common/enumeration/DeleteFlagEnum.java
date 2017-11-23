package com.facewnd.ad.common.enumeration;

public enum DeleteFlagEnum {
	
	/** 正常 */
	NORMAL((short)0),
	
	/** 删除 */
	DELETED((short)1),
	;

	private short value;
	DeleteFlagEnum(short value) {
		this.value = value;
	}
	
	public short getValue() {
		return this.value;
	}
}
