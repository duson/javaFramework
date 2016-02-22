package duson.java.solutionConf.springmvc;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/")
public class BaseController {
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
         
		CookieHelper.addCookie(response, Config.COOKIESKEY_LANGUAGE, lang, 36000);
	     
        return "redirect:" + request.getHeader("referer");
	}
	
	@RequestMapping(value = "/error/{view}")
	public String error(@PathVariable("view") String view){
		return "/error/" + view;
	}
	
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

    protected String getParameter(String name){
        return getParameter(name,"");
    }

    protected List<SortInfo> getSortInfos(String sort){
        return SortInfo.parseSortColumns(sort);
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
    
    protected String getLocaleCode(HttpServletRequest request){
		String locale = CookieHelper.getCookieValue(request, Config.COOKIESKEY_LANGUAGE);
		return locale != null ? locale : Config.COOKIESKEY_DEFAULT_LANGUAGE;
    }
    protected Locale getLocale(HttpServletRequest request){
		String locale = CookieHelper.getCookieValue(request, Config.COOKIESKEY_LANGUAGE);
		return new Locale(locale != null ? locale : Config.COOKIESKEY_DEFAULT_LANGUAGE);
    }
    protected String getI18nString(HttpServletRequest request, String key, Object... params){
    	Locale locale = getLocale(request);
		return messageSource.getMessage(key, params, locale);
    }

}
