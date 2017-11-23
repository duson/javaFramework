/**
 * com.facewnd.ad.common.security.backend.filter
 * JwtTokenAuthenticationProcessingFilter.java
 * 
 * 2017年10月16日-下午2:27:38
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.facewnd.ad.common.security.WebSecurityConfig;
import com.facewnd.ad.common.security.backend.auth.JwtAuthenticationToken;
import com.facewnd.ad.common.security.entity.RawAccessJwtToken;
import com.facewnd.ad.common.security.extractor.TokenExtractor;

/**
 * 
 * JwtTokenAuthenticationProcessingFilter
 * 1. 检查访问令牌在X-Authorization头。如果发现访问令牌的头,委托认证JwtAuthenticationProvider否则抛出身份验证异常 
 * 2. 调用成功或失败策略基于由JwtAuthenticationProvider执行身份验证过程的结果
 * 创建人：pengcaiyun
 * 最后修改人：pengcaiyun
 * 2017年10月16日 下午2:27:38
 * 
 * @version 1.0.0
 * 
 */
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;
    
    @Autowired
    public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler, 
            TokenExtractor tokenExtractor, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String tokenPayload = request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM);
        RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
