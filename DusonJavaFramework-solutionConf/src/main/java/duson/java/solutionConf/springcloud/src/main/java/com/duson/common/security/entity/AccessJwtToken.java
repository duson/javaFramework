/**
 * com.facewnd.ad.common.security.entity
 * AccessJwtToken.java
 * 
 * 2017年10月16日-下午1:50:34
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;

/**
 * 
 * AccessJwtToken
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午1:50:34
 * 
 * @version 1.0.0
 * 
 */
public class AccessJwtToken implements JwtToken {
	private final String rawToken;
	@JsonIgnore
	private Claims claims;

	protected AccessJwtToken(final String token, Claims claims) {
		this.rawToken = token;
		this.claims = claims;
	}

	@Override
	public String getToken() {
		return this.rawToken;
	}

	public Claims getClaims() {
		return claims;
	}
}
