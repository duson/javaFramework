/**
 * com.facewnd.ad.common.security.utils
 * UserUtils.java
 * 
 * 2017年10月17日-上午10:55:44
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.utils;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.facewnd.ad.common.security.entity.SecurityUser;

/**
 * 
 * UserUtils
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月17日 上午10:55:44
 * 
 * @version 1.0.0
 * 
 */
public class UserUtils {
	
	//运营登录
	public final static String LOGINTYPE_FRONT="front-login";
	//运维登录
	public final static String LOGINTYPE_BACKEND="backend-login";

	/**
	 * 获取当前登录用户ID
	 * @return 
	 * Long
	 * @exception 
	 * @since  1.0.0
	 */
	public static Long sessionUserId() {
		Optional<SecurityUser> securityUser = Optional.of((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return securityUser.map(u -> u.getUserId()).orElse(null);
	}
	/**
	 * 获取当前登录入口  登录方式 front-login 运营登录 backend-login 运维登录
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String sessionLoginType(){
		Optional<SecurityUser> securityUser = Optional.of((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return securityUser.map(u -> u.getLoginType()).orElse(null);
	}

}
