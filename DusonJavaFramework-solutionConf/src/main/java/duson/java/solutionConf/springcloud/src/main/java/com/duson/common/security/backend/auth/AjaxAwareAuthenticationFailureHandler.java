/**
 * com.facewnd.ad.common.security.backend.auth
 * AjaxAwareAuthenticationFailureHandler.java
 * 
 * 2017年10月16日-下午2:03:25
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.facewnd.ad.common.enumeration.ErrorCode;
import com.facewnd.ad.common.model.RestResult;
import com.facewnd.ad.common.security.exceptions.JwtExpiredTokenException;
import com.facewnd.ad.common.security.exceptions.ValidAuthCodeErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * AjaxAwareAuthenticationFailureHandler
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午2:03:25
 * 
 * @version 1.0.0
 * 
 */
@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final ObjectMapper mapper;

	@Autowired
	public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		RestResult<Object> restResult = new RestResult<Object>();
		restResult.setSuccess(false);
		if (e instanceof BadCredentialsException) {
			if (e.getMessage().equals("Invalid JWT token: ")) {
				restResult.setErrorMsg(ErrorCode.InvalidToken.getDesc());
				restResult.setErrorCode(ErrorCode.InvalidToken.toString());
				mapper.writeValue(response.getWriter(), restResult);
			} else {
				restResult.setErrorMsg(ErrorCode.InvalidUser.getDesc());
				restResult.setErrorCode(ErrorCode.InvalidUser.toString());
				mapper.writeValue(response.getWriter(), restResult);
			}
		} else if (e instanceof JwtExpiredTokenException) {
			restResult.setErrorMsg(ErrorCode.EXPIREDToken.getDesc());
			restResult.setErrorCode(ErrorCode.EXPIREDToken.toString());
			mapper.writeValue(response.getWriter(), restResult);
			// mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof UsernameNotFoundException) {
			restResult.setErrorMsg("用户名不存在");
			restResult.setErrorCode("USER_IS_NULL");
			mapper.writeValue(response.getWriter(), restResult);
			// mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof ValidAuthCodeErrorException) {
			restResult.setErrorMsg(e.getMessage());
			restResult.setErrorCode("USER_IS_NULL");
			mapper.writeValue(response.getWriter(), restResult);
			// mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else {
			restResult.setErrorMsg("Authentication failed");
			restResult.setErrorCode(ErrorCode.Error.toString());
			mapper.writeValue(response.getWriter(), restResult);
		}
		// mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
	}
}
