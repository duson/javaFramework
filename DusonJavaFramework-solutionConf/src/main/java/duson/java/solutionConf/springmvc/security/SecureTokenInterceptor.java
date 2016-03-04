/**
 * 描述：
 * 作者：pmchen
 * 创建时间：2015-7-2 下午4:40:37 
 * Copyright (c) 2015, 深圳市商巢互联网技术有限公司 All Rights Reserved.
 */
package duson.java.solutionConf.springmvc.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 防CSRF攻击，请求需提供令牌
 * @author pmchen
 *
 */
public class SecureTokenInterceptor extends HandlerInterceptorAdapter {
	private static final String TOKEN_PARAMETER_NAME = "";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        // 忽略非POST请求
        if (RequestMethod.POST == RequestMethod.valueOf(req.getMethod())) {
            return checkValidToken(req, resp);
        }

        return true;
    }

    /**
     * 验证令牌
     * @param req
     * @param resp
     * @return
     * @throws IOException
     * @throws ModelAndViewDefiningException
     */
    private boolean checkValidToken(HttpServletRequest req, HttpServletResponse resp) throws IOException, ModelAndViewDefiningException {
        String tokenValue = req.getHeader(TOKEN_PARAMETER_NAME);

        HttpSession session = req.getSession();
        
        if (tokenValue == null || tokenValue.isEmpty()) {
            resp.sendError(400);
            return false;
        } else if (!tokenValue.equals(session.getValue(TOKEN_PARAMETER_NAME))) {
            ModelAndView mav = new ModelAndView("redirect:/error/unauthorized");
            throw new ModelAndViewDefiningException(mav);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {
        // 生产令牌，保存在Session和ViewModel中
    	
    	HttpSession session = req.getSession();
        
        if(session.getValue(TOKEN_PARAMETER_NAME) == null) {
	        String tokenValue = new Date().toString();
	        session.putValue(TOKEN_PARAMETER_NAME, tokenValue); 
	        modelAndView.addObject(TOKEN_PARAMETER_NAME, tokenValue);
        }
    }
}
