package com.facewnd.ad.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="返回结果")
public class RestResult<T> {
	@ApiModelProperty(value="是否成功")
	private boolean success;
	@ApiModelProperty(value="错误代码")
	private String errorCode;
	@ApiModelProperty(value="错误信息")
	private String errorMsg;
	@ApiModelProperty(value="返回的业务结果")
	private T returnValue;
	
	public RestResult() {}
	
	public RestResult(boolean success, T returnValue) {
		this.success = success;
		this.returnValue = returnValue;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public RestResult<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public RestResult<T> setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public RestResult<T> setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}
	public T getReturnValue() {
		return returnValue;
	}
	public RestResult<T> setReturnValue(T returnValue) {
		this.returnValue = returnValue;
		return this;
	}
}
