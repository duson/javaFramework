/**
 * com.facewnd.ad.common.security.backend.auth
 * AjaxAwareAuthenticationSuccessHandler.java
 * 
 * 2017年10月16日-上午11:47:29
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.auth;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.facewnd.ad.common.security.entity.AccessJwtToken;
import com.facewnd.ad.common.security.entity.JwtTokenFactory;
import com.facewnd.ad.common.security.entity.SecurityUser;
import com.facewnd.ad.common.security.utils.UserUtils;
import com.facewnd.ad.modules.base.account.entity.AdManageUser;
import com.facewnd.ad.modules.base.account.entity.AdUser;
import com.facewnd.ad.modules.base.account.service.AdManageUserService;
import com.facewnd.ad.modules.base.account.service.AdUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * AjaxAwareAuthenticationSuccessHandler
 * 
 * 创建人：pengcaiyun
 * 最后修改人：pengcaiyun
 * 2017年10月16日 上午11:47:29
 * 
 * @version 1.0.0
 * 
 */
@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final static Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationSuccessHandler.class);
	private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;
    
    @Autowired
	private AdUserService userService;
    @Autowired
	private AdManageUserService manageUserService;

    @Autowired
    public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	logger.debug("=======AjaxAwareAuthenticationSuccessHandler.onAuthenticationSuccess");
        SecurityUser userContext = (SecurityUser) authentication.getPrincipal();
        AccessJwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        AccessJwtToken refreshToken = tokenFactory.createRefreshToken(userContext);
        if(userContext.getLoginType().equals(UserUtils.LOGINTYPE_BACKEND)){
        	AdManageUser queryObj=manageUserService.queryAdManageUserById(userContext.getUserId());
        	AdManageUser manageUser=new AdManageUser();
        	manageUser.setMuserId(userContext.getUserId());
        	manageUser.setLoginDtm(new Date());
        	manageUser.setLoginNumb(queryObj.getLoginNumb()+1);
        	manageUserService.editManageUser(manageUser);
        }else if(userContext.getLoginType().equals(UserUtils.LOGINTYPE_FRONT)){
        	AdUser queryObj=userService.queryAdUserById(userContext.getUserId());
        	AdUser updateObj=new AdUser();
        	updateObj.setUserId(userContext.getUserId());
        	updateObj.setLoginDtm(new Date());
        	updateObj.setLoginNumb(queryObj.getLoginNumb()+1);
        	userService.editAdUser(updateObj);
        }
        
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());
        tokenMap.put("userName", userContext.getUserAlias());
        tokenMap.put("success", true);
        tokenMap.put("errorCode", HttpStatus.OK.value());
        tokenMap.put("errorMsg", "登录成功");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     * 
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
