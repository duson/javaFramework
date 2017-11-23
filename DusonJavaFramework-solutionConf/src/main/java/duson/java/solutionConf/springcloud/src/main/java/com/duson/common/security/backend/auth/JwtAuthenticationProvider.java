/**
 * com.facewnd.ad.common.security.backend.auth
 * JwtAuthenticationProvider.java
 * 
 * 2017年10月16日-下午2:49:17
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.facewnd.ad.common.security.entity.JwtSettings;
import com.facewnd.ad.common.security.entity.RawAccessJwtToken;
import com.facewnd.ad.common.security.entity.SecurityUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * 
 * JwtAuthenticationProvider 1. 验证 access token 的签名 2. 从访问令牌中提取身份和授权声明和使用它们来创建SecurityUser 3. 如果访问令牌是畸形的,过期的或者只是如果令牌不签署与适当的签名密钥身份验证就会抛出异常 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午2:49:17
 * 
 * @version 1.0.0
 * 
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtSettings jwtSettings;

	@Autowired
	public JwtAuthenticationProvider(JwtSettings jwtSettings) {
		this.jwtSettings = jwtSettings;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
		Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
		String subject = jwsClaims.getBody().getSubject();
		List<?> scopes = jwsClaims.getBody().get("scopes", List.class);
		Long userId = jwsClaims.getBody().get("uId", Long.class);
		String loginType = jwsClaims.getBody().get("loginType", String.class);
		List<GrantedAuthority> authorities = scopes.stream().map(authority -> new SimpleGrantedAuthority(authority.toString())).collect(Collectors.toList());
		SecurityUser context = SecurityUser.create(loginType, subject, userId, authorities);
		return new JwtAuthenticationToken(context, context.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
