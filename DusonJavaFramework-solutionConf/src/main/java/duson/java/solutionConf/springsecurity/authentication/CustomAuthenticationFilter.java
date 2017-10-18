package duson.java.solutionConf.springsecurity.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	@Autowired
	private CustomAuthenticationFailureHandler failureHandler;
	
	protected CustomAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		
		setAuthenticationSuccessHandler(successHandler);
		setAuthenticationFailureHandler(failureHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		CustomAuthenticationToken token = new CustomAuthenticationToken();
		String tokenHead = request.getHeader("");
		// 根据tokenHead获取用户信息
        token.setUsername("abc");
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        
        return this.getAuthenticationManager().authenticate(token);

	}

}
