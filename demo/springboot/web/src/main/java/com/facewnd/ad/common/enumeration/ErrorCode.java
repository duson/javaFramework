package com.facewnd.ad.common.enumeration;

public enum ErrorCode {
	// 公共
	Success("SUCCESS", "操作成功"),
	Error("COMM.SYSTEM_ERROR", "系统错误"),
	InvalidJsonFormat("COMM.INVALID_JSON_FORMAT", "Json参数不正确"),
	InvalidParams("COMM.INVALID_PARAMETER", "参数无效"),
	NotExist("COMM.TARGET_NOT_EXIST", "目标不存在"),
	
	// xxx业务
	
	;
	
	String errorCode;
	String desc;
	ErrorCode(String errorCode, String desc) {
		this.errorCode = errorCode;
		this.desc = desc;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	@Override
	public String toString() {
		return this.errorCode;
	}
}