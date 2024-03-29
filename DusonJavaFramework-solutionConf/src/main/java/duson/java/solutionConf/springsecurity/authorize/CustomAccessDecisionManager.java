package duson.java.solutionConf.springsecurity.authorize;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

	/**
	* Authentication authentication --->用户具有的角色权限 
	* Collection<ConfigAttribute> configAttributes --->访问该资源所需的角色权限
	*/
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		
		if(authentication == null){
            throw new AccessDeniedException("permission denied");
        }

        /* Jdk8的写法
        //当前用户拥有的角色集合
        List<String> roleCodes = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        //访问路径所需要的角色集合
        List<String> configRoleCodes = configAttributes.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toList());
        for (String roleCode : roleCodes){
            if(configRoleCodes.contains(roleCode)){
                return;
            }
        }*/
        
		Iterator<ConfigAttribute> iter = configAttributes.iterator();
		while (iter.hasNext()) {
			String accessResourceNeedRole = ((SecurityConfig) iter.next()).getAttribute();
			for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				String userOwnRole = grantedAuthority.getAuthority();
				if (accessResourceNeedRole.trim().equals(userOwnRole.trim())) {
					return;
				}
			}
		}

        throw new AccessDeniedException("无权限");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
