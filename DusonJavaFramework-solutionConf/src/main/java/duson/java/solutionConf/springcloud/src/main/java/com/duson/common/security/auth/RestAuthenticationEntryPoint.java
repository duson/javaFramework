package com.facewnd.ad.common.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.facewnd.ad.common.model.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
			throws IOException, ServletException {
		//response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		RestResult<Object> restResult=new RestResult<Object>();
		restResult.setSuccess(false);
		restResult.setErrorMsg("用户未登录");
		restResult.setErrorCode("Unauthorized");
		mapper.writeValue(response.getWriter(), restResult);
	}
}
