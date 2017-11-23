/**
 * com.facewnd.ad.common.security.entity
 * JwtTokenFactory.java
 * 
 * 2017年10月16日-上午11:49:13
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * JwtTokenFactory
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 上午11:49:13
 * 
 * @version 1.0.0
 * 
 */
@Component
public class JwtTokenFactory {

	private final JwtSettings settings;

	@Autowired
	public JwtTokenFactory(JwtSettings settings) {
		this.settings = settings;
	}

	/**
	 * 创建签署了JWT访问令牌
	 * @param securityUser
	 * @return 
	 * AccessJwtToken
	 * @exception 
	 * @since  1.0.0
	 */
	public AccessJwtToken createAccessJwtToken(SecurityUser securityUser) {
		if (StringUtils.isBlank(securityUser.getUsername()))
			throw new IllegalArgumentException("Cannot create JWT Token without username");

		if (securityUser.getAuthorities() == null || securityUser.getAuthorities().isEmpty())
			throw new IllegalArgumentException("User doesn't have any privileges");

		Claims claims = Jwts.claims().setSubject(securityUser.getUsername());
		claims.put("scopes", securityUser.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
		claims.put("uId", securityUser.getUserId());
		// 登录方式 front-login 运营登录 backend-login 运维登录
		claims.put("loginType", securityUser.getLoginType());
		LocalDateTime currentTime = LocalDateTime.now();

		String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

		return new AccessJwtToken(token, claims);
	}

	/**
	 * 创建签署了JWT刷新令牌
	 * @param securityUser
	 * @return 
	 * AccessJwtToken
	 * @exception 
	 * @since  1.0.0
	 */
	public AccessJwtToken createRefreshToken(SecurityUser securityUser) {
		if (StringUtils.isBlank(securityUser.getUsername())) {
			throw new IllegalArgumentException("Cannot create JWT Token without username");
		}

		LocalDateTime currentTime = LocalDateTime.now();

		Claims claims = Jwts.claims().setSubject(securityUser.getUsername());
		claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
		claims.put("uId", securityUser.getUserId());
		claims.put("loginType", securityUser.getLoginType());
		String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getRefreshTokenExpTime()).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
		return new AccessJwtToken(token, claims);
	}

}
