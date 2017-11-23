/**
 * com.facewnd.ad.common.security.entity
 * SecurityUser.java
 * 
 * 2017年10月12日-下午3:18:50
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * SecurityUser
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月12日 下午3:18:50
 * 
 * @version 1.0.0
 * 
 */
public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = -2982757106784292837L;
	
	private Long userId;
	private String userAlias;
	//登录方式 front-login 运营登录 backend-login 运维登录
	private String loginType;

	private final Collection<? extends GrantedAuthority> authorities;

	public SecurityUser(String loginType,String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
		this.loginType=loginType;
		this.userAlias=username;
		this.userId=userId;
		this.authorities = authorities;
	}

	public static SecurityUser create(String loginType,String username, Long userId, List<GrantedAuthority> authorities) {
		if (StringUtils.isBlank(username))
			throw new IllegalArgumentException("Username is blank: " + username);
		return new SecurityUser(loginType,username, userId, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.getPassword();
	}

	@Override
	public String getUsername() {
		return this.getUserAlias();
	}

	// 账户是否未过期
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 账户是否未锁定
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 密码是否未过期
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 账户是否激活
	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
}
