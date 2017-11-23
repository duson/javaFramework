/**
 * com.facewnd.ad.common.security.auth
 * AccessDeniedHandlerExtImpl.java
 * 
 * 2017年10月27日-下午5:12:57
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.facewnd.ad.common.model.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * AccessDeniedHandlerExtImpl
 * 
 * 创建人：pengcaiyun
 * 最后修改人：pengcaiyun
 * 2017年10月27日 下午5:12:57
 * 
 * @version 1.0.0
 * 
 */
@Component
public class AccessDeniedHandlerExtImpl implements AccessDeniedHandler {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		RestResult<Object> restResult=new RestResult<Object>();
		restResult.setSuccess(false);
		restResult.setErrorMsg("未授权的请求");
		restResult.setErrorCode("DeniedAccess");
		mapper.writeValue(response.getWriter(), restResult);

	}

}
