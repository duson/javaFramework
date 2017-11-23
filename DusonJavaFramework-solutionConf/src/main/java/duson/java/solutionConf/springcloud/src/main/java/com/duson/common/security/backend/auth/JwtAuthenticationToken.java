/**
 * com.facewnd.ad.common.security.backend.auth
 * JwtAuthenticationToken.java
 * 
 * 2017年10月16日-下午2:46:20
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.facewnd.ad.common.security.entity.RawAccessJwtToken;
import com.facewnd.ad.common.security.entity.SecurityUser;

/**
 * 
 * JwtAuthenticationToken
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午2:46:20
 * 
 * @version 1.0.0
 * 
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 7868186420982434487L;
	private RawAccessJwtToken rawAccessToken;
	private SecurityUser userContext;

	public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
		super(null);
		this.rawAccessToken = unsafeToken;
		this.setAuthenticated(false);
	}

	public JwtAuthenticationToken(SecurityUser userContext, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.eraseCredentials();
		this.userContext = userContext;
		super.setAuthenticated(true);
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return rawAccessToken;
	}

	@Override
	public Object getPrincipal() {
		return this.userContext;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.rawAccessToken = null;
	}

}
