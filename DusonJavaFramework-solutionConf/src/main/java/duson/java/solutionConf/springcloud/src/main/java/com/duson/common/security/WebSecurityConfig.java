/**
 * com.facewnd.ad.common.security
 * WebSecurityConfig.java
 * 
 * 2017年10月12日-下午3:05:18
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.facewnd.ad.common.security.auth.RestAuthenticationEntryPoint;
import com.facewnd.ad.common.security.backend.auth.AjaxAuthenticationProvider;
import com.facewnd.ad.common.security.backend.auth.JwtAuthenticationProvider;
import com.facewnd.ad.common.security.backend.filter.AjaxLoginProcessingFilter;
import com.facewnd.ad.common.security.backend.filter.JwtTokenAuthenticationProcessingFilter;
import com.facewnd.ad.common.security.backend.filter.SkipPathRequestMatcher;
import com.facewnd.ad.common.security.extractor.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * 
 * WebSecurityConfig
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月12日 下午3:05:18
 * 
 * @version 1.0.0
 * 
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 是否启用权限注解 默认关闭
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
	//获取验证码
	public static final String FORM_BASED_CODE_ENTRY_POINT = "/getCode";
	//公共url
    public static final String PUBLIC_ENTRY_POINT = "/pub/**";
	//登录
	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/login";
	//运维url
	public static final String TOKEN_BACKEND_AUTH_ENTRY_POINT = "/backend/**";
	//运营url
	public static final String TOKEN_FRONT_AUTH_ENTRY_POINT = "/front/**";
	public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
	
	public static final String[] SWAGGER_ENTRY_POINT = {"/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs/**", "/webjars/**"};
	public static final String[] APP_API_ENTRY_POINT = {"/api/v1/**"};

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	@Autowired
	private AuthenticationFailureHandler failureHandler;
	@Autowired
	private AjaxAuthenticationProvider ajaxAuthenticationProvider;
	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;
	@Autowired
	private TokenExtractor tokenExtractor;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ObjectMapper objectMapper;

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		// Filters will not get executed for the resources
////		web.ignoring().antMatchers("/", "/getCode", "/resources/**", "/static/**", "/public/**", "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**",
////				"/v2/api-docs/**", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff");
//	}

	@Bean
	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Bean
	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
		List<String> pathsToSkip = Lists.newArrayList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT,FORM_BASED_CODE_ENTRY_POINT,PUBLIC_ENTRY_POINT);
		pathsToSkip.addAll(Lists.newArrayList(SWAGGER_ENTRY_POINT));
		pathsToSkip.addAll(Lists.newArrayList(APP_API_ENTRY_POINT));
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, "/**");
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	@Bean
	protected BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // We don't need CSRF for JWT based authentication
				.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeRequests().antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
				.antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
				.and().authorizeRequests().antMatchers(TOKEN_BACKEND_AUTH_ENTRY_POINT).access("hasRole('ADMIN')") // .authenticated(),hasRole('ADMIN') and hasRole('DBA') Protected API End-points
				.and().authorizeRequests().antMatchers(TOKEN_FRONT_AUTH_ENTRY_POINT).access("hasRole('CUSTOMER')") // .authenticated(),hasRole('ADMIN') and hasRole('DBA') Protected API End-points
				.and().addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
