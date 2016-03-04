package duson.java.solutionConf.springmvc;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import duson.java.utils.web.CookieHelper;

@Controller
@RequestMapping("/")
public class BaseController {
	private static final String COOKIESKEY_LANGUAGE = "";
	private static final String COOKIESKEY_DEFAULT_LANGUAGE = "";
	
	@Resource
	protected MessageSource messageSource;
	
	@RequestMapping("changeLan")
	public String changeLanguage(String lang, HttpServletRequest request, HttpServletResponse response)
	{
         /*LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);  
         if (localeResolver == null) {  
             throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");  
         } 
         
         localeResolver.setLocale(request, response, new Locale(lang));*/
         
		CookieHelper.addCookie(response, COOKIESKEY_LANGUAGE, lang, 36000);
	     
        return "redirect:" + request.getHeader("referer");
	}
	
	@RequestMapping(value = "/error/{view}")
	public String error(@PathVariable("view") String view){
		return "/error/" + view;
	}
	
    /* 从请求中构造分页对象
    public PageQuery getPageQuery(){
        return getPageQuery(null);
    }

    public PageQuery getPageQuery(String defaultSort){
        Integer page = Integer.valueOf(getParameter("page", "1"));
        Integer limit =  Integer.valueOf(getParameter("limit", "15"));
        String sort = getParameter("sort", defaultSort);

        PageQuery pageQuery = new PageQuery(page, limit);

        List<SortInfo> sortInfos = getSortInfos(sort);
        pageQuery.getSortInfoList().addAll(sortInfos);

        return pageQuery;
    }

    protected List<SortInfo> getSortInfos(String sort){
        return SortInfo.parseSortColumns(sort);
    }
    */

    protected String getParameter(String name){
        return getParameter(name,"");
    }

    protected String getParameter(String name, String defaultValue){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
        String value = request.getParameter(name);
        if(value == null || value.equals("")){
            return defaultValue;
        }
        return value;
    }
    
    protected String getLocaleCode(HttpServletRequest request) throws UnsupportedEncodingException{
		String locale = CookieHelper.getCookieValue(request, COOKIESKEY_LANGUAGE);
		return locale != null ? locale : COOKIESKEY_DEFAULT_LANGUAGE;
    }
    protected Locale getLocale(HttpServletRequest request) throws UnsupportedEncodingException{
		String locale = CookieHelper.getCookieValue(request, COOKIESKEY_LANGUAGE);
		return new Locale(locale != null ? locale : COOKIESKEY_DEFAULT_LANGUAGE);
    }
    protected String getI18nString(HttpServletRequest request, String key, Object... params) throws UnsupportedEncodingException{
    	Locale locale = getLocale(request);
		return messageSource.getMessage(key, params, locale);
    }

}
