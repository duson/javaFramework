package com.facewnd.ad.common.enumeration;

public enum ErrorCode {
	// 公共
	Success("SUCCESS", "操作成功"),
	Error("COMM.SYSTEM_ERROR", "系统错误"),
	InvalidJsonFormat("COMM.INVALID_JSON_FORMAT", "Json参数不正确"),
	InvalidParams("COMM.INVALID_PARAMETER", "参数无效"),
	NotExist("COMM.TARGET_NOT_EXIST", "目标不存在"),
	Exist("COMM.TARGET_EXIST", "目标已存在"),
	
	// 账号
	UserExist("USER_EXIST", "用户已存在"),
	CompanyExist("COMPANY_EXIST", "公司名已存在"),
	InvalidUser("INVALID_USER", "无效的用户名和密码"),
	InvalidToken("INVALID_Token", "无效的Token"),
	EXPIREDToken("Expired_Token", "Token 已过期"),
	
	// 终端
	Bind_MultiCompany("BIND.MULTI_COMPANY", "有多个公司，请指定绑定的公司"),
	Bind_No_Available_License("BIND.NO_AVAILABLE_LICENSE", "无可用的有效授权"),
	
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