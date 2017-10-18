package duson.java.solutionConf.springsecurity.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		
        String errorMsg = null;
        if (exception instanceof RuntimeException) { // 自定义异常
            errorMsg = exception.getMessage();
        } else {
            
        }
        
        if(false) { // ajax请求，返回Json
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("staut", false);
	        jsonObject.put("errorMsg", errorMsg);
	        response.getWriter().write(jsonObject.toString());
        } else {
        	
        }
	}

}
