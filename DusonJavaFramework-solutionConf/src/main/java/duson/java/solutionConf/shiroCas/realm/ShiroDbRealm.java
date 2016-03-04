package duson.java.solutionConf.shiroCas.realm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroDbRealm extends AuthorizingRealm {
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object user = principals.fromRealm(getName()).iterator().next();
		
        if (user != null) {  
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
             // 根据用户名从数据库中查询权限（如：user:create）
            List<String> permissions = new ArrayList<String>();
            info.addStringPermissions(permissions);  
            return info;  
        } else {  
            return null;  
        } 
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authcToken;
		
		Object user = null; // 根据用户名(usernamePasswordToken.getUsername())从数据库中查询用户信息  
        if (user != null) {  
            SecurityUtils.getSubject().getSession().setAttribute("loginUserInfo", user);  
            return new SimpleAuthenticationInfo(user, "", getName());  
        } else {  
            return null;  
        } 
	}
	
}
