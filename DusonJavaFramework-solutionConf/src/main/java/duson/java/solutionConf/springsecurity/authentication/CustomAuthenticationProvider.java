package duson.java.solutionConf.springsecurity.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import duson.java.solutionConf.springsecurity.SecurityUser;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;
		SecurityUser user = null; // 根据token从数据中验证用户，非法抛出自定义异常
		
        CustomAuthenticationToken result = new CustomAuthenticationToken(user);
        result.setDetails(authentication.getDetails());
        return result;
    }

	@Override
	public boolean supports(Class<?> authentication) {
		// 支持不同类型的Token
		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
