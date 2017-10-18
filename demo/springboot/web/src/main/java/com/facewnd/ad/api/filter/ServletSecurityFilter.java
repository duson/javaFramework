package com.facewnd.ad.api.filter;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.facewnd.ad.api.model.RestRequest;
import com.facewnd.ad.common.config.PropertyConfig;
import com.facewnd.ad.common.model.RestResult;
import com.facewnd.core.encry.Md5Encry;
import com.facewnd.core.web.HttpServletRequestWrapperEx;
import com.facewnd.core.web.HttpServletResponseWrapperEx;

@WebFilter(urlPatterns = "/api/*", filterName = "servletSecurityFilter")
public class ServletSecurityFilter implements Filter {

	private PropertyConfig config;

	private static final Logger logger = LoggerFactory.getLogger(ServletSecurityFilter.class);
			
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
		config = ctx.getBean(PropertyConfig.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequestWrapperEx httpRequest = new HttpServletRequestWrapperEx((HttpServletRequest)request);
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		// 排除不需要签名的Url
		if(httpRequest.getRequestURI().startsWith("/api/v1/device/test")) {
			chain.doFilter(request, response);
			return;
		}
		
		RestRequest<SortedMap> requestModel = null;
		
		String method = httpRequest.getMethod().toUpperCase();
		if(method.equals("GET")) {
			Map<String, String> params = new HashMap<String, String>();
			
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			requestModel = JSON.parseObject(JSON.toJSONString(params), RestRequest.class);
		} else if(method.equals("POST")) {
			InputStream inputStream = httpRequest.getInputStream();
			String content = IOUtils.toString(inputStream, "UTF-8");
			requestModel = JSON.parseObject(content, RestRequest.class);
		}
		
		if(requestModel.getBizContent() instanceof Map) {
			SortedMap<String, Object> bizContent = new TreeMap<String, Object>((Map)requestModel.getBizContent());
			requestModel.setBizContent(bizContent);
		}
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(requestModel), Map.class, Feature.OrderedField);
		if(params != null) {
			params.remove("fileData");
			params.remove("sign");
		}
		
		if(!config.getValidate()) {
			this.doFilterAndLog(httpRequest, httpResponse, chain, params);
			return;
		}
		
		if(requestModel == null || StringUtils.isBlank(requestModel.getApiKey()) || StringUtils.isBlank(requestModel.getSign())) {
			this.error(request, httpResponse, params, "ApiKey或签名不能为空");
			return;
		}
		
		String apiKey = "";
		String secretKey = "";
		
		// 检查apiKey
		if(!apiKey.equals(requestModel.getApiKey())) {
			this.error(request, httpResponse, params, "无权限，apiKey无效");
			return;
		}
		
		// 检查签名
		try {
			String sign = generateSign(params, secretKey);
			if(!requestModel.getSign().equals(sign)) {
				logger.debug(sign + "===" + requestModel.getSign());
				this.error(request, httpResponse, params, "签名校验失败");
				return;
			}
		} catch (NoSuchAlgorithmException e) {
			this.error(request, httpResponse, params, "签名校验失败");
			return;
		}
		
		//chain.doFilter(httpRequest, response);
		this.doFilterAndLog(httpRequest, httpResponse, chain, params);
	}

	@Override
	public void destroy() { }

	private void error(ServletRequest request, HttpServletResponse httpResponse, Map<String, Object> params, String errorMsg) throws IOException {
		logger.debug(String.format("请求Url：%s IP:%s \n参数：%s，\n返回结果：%s\n", 
				((HttpServletRequest)request).getRequestURI(), request.getRemoteAddr(), JSON.toJSONString(params), errorMsg));
		
        httpResponse.setCharacterEncoding("UTF-8");    
        httpResponse.setContentType("application/json; charset=utf-8");   
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        RestResult result = new RestResult();
        result.setErrorMsg(errorMsg);
        httpResponse.getWriter().write(JSON.toJSONString(result));
	}
	
	private static String generateSign(Map<String, Object> params, String secretKey) throws NoSuchAlgorithmException {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(params);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            sb.append(m.getKey()).append("=").append(m.getValue()).append("&");
        }

		String paramStr = sb.append("key=").append(secretKey).toString();
		logger.debug("preMD5:" + paramStr);
		String md5Str = Md5Encry.md5(paramStr);

		return md5Str.toUpperCase();
	}
	
	private void doFilterAndLog(ServletRequest request, ServletResponse response, FilterChain chain, Map<String, Object> params) throws IOException, ServletException {
		HttpServletResponseWrapperEx responseEx = new HttpServletResponseWrapperEx((HttpServletResponse) response);
		chain.doFilter(request, responseEx);
		
		byte[] data = responseEx.getCopy();
        String responseString = new String(data, response.getCharacterEncoding());
        
		logger.debug(String.format("请求Url：%s IP:%s \n参数：%s，\n返回结果：%s\n", 
				((HttpServletRequest)request).getRequestURI(), request.getRemoteAddr(), JSON.toJSONString(params), responseString));
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String content = "{\"apiKey\":\"2B8FEF3BF7F04966A8CFDDAE960D33E8\",\"bizContent\": {\"deviceId\":2,\"question\": \"现在有什么优惠\",\"sceneIds\":[17,19],\"hotelId\":2},\"timestamp\":\"2017-09-19 11:35:50\"}";
		RestRequest requestModel = JSON.parseObject(content, RestRequest.class, Feature.OrderedField);
		
		SortedMap<String, Object> bizContent = new TreeMap<String, Object>((Map)requestModel.getBizContent());
		requestModel.setBizContent(bizContent);
		
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(requestModel), Map.class, Feature.OrderedField);
		String generateSign = generateSign(params, "6395651784CC43C995F3CDFFAD3795F6");
		System.out.println(generateSign);
	}
	
}
