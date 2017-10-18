package duson.java.solutionConf.springsecurity.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import duson.java.solutionConf.springsecurity.SecurityUser;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private String username;
    private String password;
    private SecurityUser principal;
    
    public CustomAuthenticationToken() {
    	super(null);
        super.setAuthenticated(false);
    }
    
	public CustomAuthenticationToken(SecurityUser user) {
		super(user.getAuthorities());
		this.principal = user;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
