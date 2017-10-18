package duson.java.solutionConf.springsecurity.authorize;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuthorizeFilter extends AbstractSecurityInterceptor implements Filter {

	@Autowired
	private SecurityMetadataSource filterSecurityMetadataSource;
	@Autowired  
    private CustomAccessDecisionManager accessDecisionManager;  
    @Autowired  
    private AuthenticationManager authenticationManager;

    @PostConstruct  
    public void init(){  
        super.setAuthenticationManager(authenticationManager);  
        super.setAccessDecisionManager(accessDecisionManager);  
    }
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		FilterInvocation fileInvocation = new FilterInvocation(request, response, chain);
		InterceptorStatusToken token = super.beforeInvocation(fileInvocation);
		
		try {
			fileInvocation.getChain().doFilter(request, response);
		} finally { 
			super.afterInvocation(token, null);
		}
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return filterSecurityMetadataSource;
	}

}
