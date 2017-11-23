/**
 * com.facewnd.ad.common.security.entity
 * LoginRequest.java
 * 
 * 2017年10月16日-上午10:45:58
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * LoginRequest
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 上午10:45:58
 * 
 * @version 1.0.0
 * 
 */
public class LoginRequest {
	private String username;
	private String password;
	private String loginType;//登录方式  front-login 运营登录 backend-login 运维登录
	private String authCode;//验证码

	@JsonCreator
	public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
