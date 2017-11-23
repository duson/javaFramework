/**
 * com.facewnd.ad.common.security.exceptions
 * AuthCodeInValidException.java
 * 
 * 2017年11月2日-下午5:07:03
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 * AuthCodeInValidException
 * 无效的验证码异常
 * 创建人：pengcaiyun
 * 最后修改人：pengcaiyun
 * 2017年11月2日 下午5:07:03
 * 
 * @version 1.0.0
 * 
 */
public class ValidAuthCodeErrorException extends AuthenticationException {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1224746851464371023L;

	public ValidAuthCodeErrorException(String msg) {
		super(msg);
	}

}
