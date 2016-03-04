package duson.java.solutionConf.springmvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import duson.java.core.exception.BusinessException;

public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(CustomSimpleMappingExceptionResolver.class);
			
	@Override  
    protected ModelAndView doResolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex) {  
        String viewName = determineViewName(ex, request);  
        if (viewName != null) {// JSP格式返回  
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                    .getHeader("X-Requested-With")!= null && request  
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {  
                Integer statusCode = determineStatusCode(request, viewName);  
                if (statusCode != null) {  
                    applyStatusCodeIfPossible(request, response, statusCode);  
                }  
                return getModelAndView(viewName, ex, request);  
            } else {// JSON格式返回  
                try {  
                    PrintWriter writer = response.getWriter(); 
                    String msg;
                    if(ex instanceof UnauthorizedException)
                    	msg = "无权限";
                    else if(ex instanceof BusinessException)
                    	msg = ex.getMessage();
                    else {
                    	msg = "操作失败，请稍候重试";
                    	logger.error("CustomSimpleMappingExceptionResolver - Json", ex);
                    }
                    
                    if(msg != null) {
                    	writer.write(msg);  
                    	writer.flush();  
                    }
                } catch (IOException e) {
                	logger.error("CustomSimpleMappingExceptionResolver IOException - Json", e);
                    //e.printStackTrace();  
                }  
                return null;  
            }  
        } else {  
            return null;  
        }  
    }  
}
