/**
 * com.facewnd.ad.common.security.entity
 * Scopes.java
 * 
 * 2017年10月16日-下午1:54:53
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

/**
 * 
 * Scopes
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午1:54:53
 * 
 * @version 1.0.0
 * 
 */
public enum Scopes {
	REFRESH_TOKEN;
	public String authority() {
		return "ROLE_" + this.name();
	}
}
