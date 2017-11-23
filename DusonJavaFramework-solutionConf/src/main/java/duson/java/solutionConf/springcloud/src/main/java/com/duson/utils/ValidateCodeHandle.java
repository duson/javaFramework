/**
 * com.facewnd.ad.utils
 * ValidateCodeHandle.java
 * 
 * 2017年11月2日-下午5:18:47
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * ValidateCodeHandle
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年11月2日 下午5:18:47
 * 
 * @version 1.0.0
 * 
 */
public class ValidateCodeHandle {

	private static ConcurrentHashMap<String, String> validateCode = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, String> getCode() {
		return validateCode;
	}

	public static void save(String sessionId, String code) {
		validateCode.put(sessionId, code);
	}

	public static String getValidateCode(String sessionId) {
		Object obj = validateCode.get(sessionId);
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;
	}

	public static boolean matchCode(String sessionId, String inputCode) {
		String saveCode = getValidateCode(sessionId);
		if (null != saveCode && saveCode.equalsIgnoreCase(inputCode)) {
			return true;
		}
		return false;
	}

}
