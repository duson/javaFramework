package duson.java.solutionConf.springmvc;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import duson.java.utils.web.CookieHelper;

@Component
public class I18nHelper {
	private static final String COOKIESKEY_LANGUAGE = "";
	
	@Resource
	private HttpServletRequest request;
	@Resource
	private MessageSource messageSource;
	
	public String getString(String key) throws UnsupportedEncodingException {
		return getString(key, null);
    }
	public String getString(String key, Object... params) throws UnsupportedEncodingException {
		String locale = CookieHelper.getCookieValue(request, COOKIESKEY_LANGUAGE);
		return messageSource.getMessage(key, params, locale == null ? null : new Locale(locale)); // new Locale(locale != null ? locale : Config.COOKIESKEY_DEFAULT_LANGUAGE)
    }
	
	public boolean isChinaLang() throws UnsupportedEncodingException{
		String locale = CookieHelper.getCookieValue(request, COOKIESKEY_LANGUAGE);
		if(locale == null) return true;
		return locale.equals("zh-CN");
	}
	
}
