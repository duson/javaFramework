/**
 * com.facewnd.ad.common.security.backend.filter
 * AjaxLoginProcessingFilter.java
 * 
 * 2017年10月16日-上午10:37:31
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.facewnd.ad.common.config.PropertyConfig;
import com.facewnd.ad.common.security.entity.LoginRequest;
import com.facewnd.ad.common.security.exceptions.AuthMethodNotSupportedException;
import com.facewnd.ad.common.security.exceptions.ValidAuthCodeErrorException;
import com.facewnd.ad.common.security.utils.WebUtil;
import com.facewnd.ad.utils.ValidateCodeHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * AjaxLoginProcessingFilter Ajax 登录处理过滤器 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 上午10:37:31
 * 
 * @version 1.0.0
 * 
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private final static Logger logger = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;
	private final ObjectMapper objectMapper;
	@Autowired
	private PropertyConfig propertyConfig;

	public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
		super(defaultProcessUrl);
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
		this.objectMapper = mapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication method not supported. Request method: {}", request.getMethod());
			}
			throw new AuthMethodNotSupportedException("Authentication method not supported");
		}

		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
		if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
			throw new AuthenticationServiceException("Username or Password not provided");
		}

		//是否启用验证码功能
		if(propertyConfig.getEnbaleAuthCode()){
			if (null == loginRequest.getAuthCode() || StringUtils.isBlank(loginRequest.getAuthCode()))
				throw new ValidAuthCodeErrorException("验证码不能为空");
			if (!ValidateCodeHandle.matchCode(request.getSession().getId(), loginRequest.getAuthCode()))
				throw new ValidAuthCodeErrorException("验证码错误");
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		token.setDetails(loginRequest);
		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}

}
