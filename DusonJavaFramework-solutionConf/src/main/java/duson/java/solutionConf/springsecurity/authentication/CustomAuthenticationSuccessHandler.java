package duson.java.solutionConf.springsecurity.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		response.setContentType("text/html;charset=UTF-8");
        
        // 如果是Ajax请求，返回Json
        if(false) {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("token", "");
	        response.getWriter().write(jsonObject.toString());
        } else {
	        String redirect = request.getParameter("redirect");
	        if (StringUtils.isEmpty(redirect)) {
	            DefaultSavedRequest savedRequest = (DefaultSavedRequest) this.requestCache.getRequest(request, response);
	            if (savedRequest != null) {
	                redirect = savedRequest.getRequestURI() + "?" + savedRequest.getQueryString();
	            } else {
	                redirect = request.getContextPath() + "/";
	            }
	        } else {
	            this.requestCache.removeRequest(request, response);
	        }
	
	        HttpSession session = request.getSession(false);
	        if (session != null)
	            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	        
	        response.sendRedirect(redirect);
        }
	}

}
