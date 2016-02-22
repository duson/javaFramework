package duson.java.solutionConf.springmvc.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.cnit.pubds.domain.common.Config;
import com.cnit.pubds.domain.common.Constant;
import com.cnit.pubds.infrastructure.util.InitPropertiesUtil;
import com.cnit.pubds.infrastructure.util.web.CookieHelper;

public class AjaxSessionTimeoutFilter implements Filter {

	private Set<String> safeUrls;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.safeUrls = new HashSet<String>();
		String url = getPropertyFromInitParams(filterConfig, "safeUrls", null);
		if (url != null) {
			String[] urls = url.split("\n");
			for (String s : urls) {
				s = s.trim();
				if (!"".equals(s)) {
					this.safeUrls.add(s);
				}
			}
		}
	}

	/**
	 * 只针对ajax进行处理
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		Object casUrlObject = req.getSession().getAttribute("casUrl");
		if (null == casUrlObject) {
			req.getSession().setAttribute("casUrl", InitPropertiesUtil.get("cas.server"));
		}
		
		// 国际化
		if(req.getParameter(Constant.COOKIES_KEY_LANG) != null){
			String lang = req.getParameter(Constant.COOKIES_KEY_LANG).toString();
			CookieHelper.addCookie(res, Config.COOKIESKEY_LANGUAGE, lang, -1);
		}
		
		// 是否为公众云平台的代号
		if(req.getParameter(Constant.COOKIES_KEY_ACCOUNT_TYPE) != null){
			String type = req.getParameter(Constant.COOKIES_KEY_ACCOUNT_TYPE).toString();
			CookieHelper.addCookie(res, Constant.COOKIES_KEY_ACCOUNT_TYPE, type, -1);
			res.sendRedirect(req.getContextPath() + req.getServletPath());
			return;
		}
		
		// 判断session里是否有用户信息
		//if(!SecurityUtils.getSubject().isAuthenticated()) {
		if (req.getSession().getAttribute(Constant.SESSION_KEY_CURRENT_USERID) == null) {
			// 如果是ajax请求响应头会有，x-requested-with；
			//如果是安全地址不进行处理　
			if (req.getHeader("x-requested-with") != null
					&& req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")
					&& !this.isSafeUrl(req)) {
				//res.setStatus(901);// 表示session timeout
				res.sendError(901);
			} else {
				chain.doFilter(req, res);
			}
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {

	}

	private final String getPropertyFromInitParams(FilterConfig filterConfig,
			String propertyName, String defaultValue) {
		String value = filterConfig.getInitParameter(propertyName);
		if (StringUtils.isNotBlank(value)) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * 判断是否要跳过的地址
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param request
	 * @return [参数说明]
	 * @return boolean 
	 * @exception throws [违例类型] [违例说明]
	 */
	private boolean isSafeUrl(HttpServletRequest request) {
		String url = request.getServletPath();
		for (String safeUrl : safeUrls) {
			if(url.startsWith(safeUrl)) {
				//当前被请求的地址　包含filter里配置的安全地址,说明该地址是安全的
				return true;
			}
		}
		return false;
	}
}
