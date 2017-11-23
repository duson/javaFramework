package com.facewnd.ad.api.model;

import javax.validation.Valid;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="请求实体")
public class RestRequest<T> {

	@NotBlank(message="appId不能为null")
	@ApiModelProperty(value="appId", required=true)
	@FormParam("appId")
	private String appId;
	
	@ApiModelProperty(value="业务参数是否启用AES加密")	
	private Boolean aesEncryty;
	
	@NotBlank(message="sign不能为null")
	@ApiModelProperty(value="签名", required=true)
	@FormParam("sign")
	private String sign;
	
	@NotBlank(message="timestamp不能为null")
	@ApiModelProperty(value="时间戳", required=true)
	@FormParam("timestamp")
	private String timestamp;
	
	@ApiModelProperty(value="业务参数")
	@Valid
	private T bizContent;
	
	public Boolean isAesEncryty() {
		return aesEncryty;
	}

	public void setAesEncryty(Boolean aesEncryty) {
		this.aesEncryty = aesEncryty;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public T getBizContent() {
		return bizContent;
	}

	public void setBizContent(T bizContent) {
		this.bizContent = bizContent;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
