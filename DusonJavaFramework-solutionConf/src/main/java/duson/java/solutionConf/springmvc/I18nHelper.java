package duson.java.solutionConf.springmvc;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class I18nHelper {
	@Resource
	private HttpServletRequest request;
	@Resource
	private MessageSource messageSource;
	
	public String getString(String key) {
		return getString(key, null);
    }
	public String getString(String key, Object... params) {
		String locale = CookieHelper.getCookieValue(request, Config.COOKIESKEY_LANGUAGE);
		return messageSource.getMessage(key, params, locale == null ? null : new Locale(locale)); // new Locale(locale != null ? locale : Config.COOKIESKEY_DEFAULT_LANGUAGE)
    }
	
	public boolean isChinaLang(){
		String locale = CookieHelper.getCookieValue(request, Config.COOKIESKEY_LANGUAGE);
		if(locale == null) return true;
		return locale.equals("zh-CN");
	}
	
}
