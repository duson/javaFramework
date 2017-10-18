package duson.java.solutionConf.springsecurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserService implements UserDetailsService {

	@Override
	public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
		// 根据用户名根据拥有的角色
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("Role_USER"));
		return new SecurityUser(username, "123456", authorities, null);
	}

}
