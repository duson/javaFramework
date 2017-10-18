package duson.java.solutionConf.springsecurity.authorize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object; //当前请求对象
		String requestUrl = fi.getRequestUrl();
		if (isMatcherAllowedRequest(fi)) return null ; //return null 表示允许访问，不做拦截
		
		// 根据请求Url返回有权限的角色列表
		List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
		
		String roleCode = "Role_USER";
		if(requestUrl.startsWith("/admin"))
			roleCode = "Role_ADMIN";
		ConfigAttribute cb = new SecurityConfig(roleCode);
		list.add(cb);
		
		return list;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	/**
     * 判断当前请求是否在允许请求的范围内
     * @param fi 当前请求
     * @return 是否在范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi){
        return allowedRequest().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
    }

   private List<String> allowedRequest(){
       return Arrays.asList("/login", "/static/**", "/", "/favicon.ico");
   }
    
}
