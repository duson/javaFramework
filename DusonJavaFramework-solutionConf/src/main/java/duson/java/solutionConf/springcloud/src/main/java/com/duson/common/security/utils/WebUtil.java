/**
 * com.facewnd.ad.common.security.utils
 * WebUtil.java
 * 
 * 2017年10月16日-上午10:44:35
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * 
 * WebUtil
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 上午10:44:35
 * 
 * @version 1.0.0
 * 
 */
public class WebUtil {
	private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
	private static final String X_REQUESTED_WITH = "X-Requested-With";

	private static final String CONTENT_TYPE = "Content-type";
	private static final String CONTENT_TYPE_JSON = "application/json";

	public static boolean isAjax(HttpServletRequest request) {
		return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
	}

	public static boolean isAjax(SavedRequest request) {
		return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
	}

	public static boolean isContentTypeJson(SavedRequest request) {
		return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
	}
}
