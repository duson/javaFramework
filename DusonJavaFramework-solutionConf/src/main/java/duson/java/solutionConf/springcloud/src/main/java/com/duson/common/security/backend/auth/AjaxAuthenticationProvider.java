/**
 * com.facewnd.ad.common.security.backend.auth
 * AjaxAuthenticationProvider.java
 * 
 * 2017年10月16日-上午11:02:31
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.auth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.facewnd.ad.common.security.entity.LoginRequest;
import com.facewnd.ad.common.security.entity.SecurityUser;
import com.facewnd.ad.common.security.utils.UserUtils;
import com.facewnd.ad.modules.base.account.entity.AdManageUser;
import com.facewnd.ad.modules.base.account.entity.AdUser;
import com.facewnd.ad.modules.base.account.service.AdManageUserService;
import com.facewnd.ad.modules.base.account.service.AdUserService;
import com.facewnd.core.encry.Md5Encry;

/**
 * 
 * AjaxAuthenticationProvider
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 上午11:02:31
 * 
 * @version 1.0.0
 * 
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
	private final static Logger logger = LoggerFactory.getLogger(AjaxAuthenticationProvider.class);
	private final BCryptPasswordEncoder encoder;
	private final AdManageUserService manageUserService;

	@Autowired
	private AdUserService userService;

	@Autowired
	public AjaxAuthenticationProvider(final AdManageUserService manageUserService, final BCryptPasswordEncoder encoder) {
		this.manageUserService = manageUserService;
		this.encoder = encoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");
		String userName = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		LoginRequest loginRequest = (LoginRequest) authentication.getDetails();
		logger.debug("===AjaxAuthenticationProvider.username={},loginType={}", userName, loginRequest.getLoginType());
		SecurityUser securityUser = null;
		// 登录方式 front-login 运营登录 backend-login 运维登录
		if (loginRequest.getLoginType().equals(UserUtils.LOGINTYPE_BACKEND)) {
			AdManageUser manageUser = manageUserService.loadUserByUserAlias(userName).orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
			try {
				if (!encoder.matches(password, manageUser.getPassword2())) {
					throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
				}
			} catch (Exception e) {
				throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
			}

			List<String> roles = Arrays.asList("ROLE_ADMIN");
			List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			securityUser = new SecurityUser(loginRequest.getLoginType(),userName, manageUser.getMuserId(), authorities);
			return new UsernamePasswordAuthenticationToken(securityUser, password, authorities);
		} else {
			AdUser user = userService.loadUserByUserMobile(userName).orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
			try {
				if (!encoder.matches(password, user.getPassword2())) {
					throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
				}
			} catch (Exception e) {
				throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
			}
			List<String> roles = Arrays.asList("ROLE_CUSTOMER");
			List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			securityUser = new SecurityUser(loginRequest.getLoginType(),userName, user.getUserId(), authorities);
			return new UsernamePasswordAuthenticationToken(securityUser, password, authorities);
		}

		// if (!encoder.matches(password, manageUser.getPassword1())) {
		// throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
		// }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		// $2a$10$jOD4f.5udZ20s78MCIsfI.NMUUzHD9IzAJ4A4XQ9.5lSfInbLiCTC
		String password = "123456";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("====>>" + passwordEncoder.encode(Md5Encry.md5(password)));
	}

}
