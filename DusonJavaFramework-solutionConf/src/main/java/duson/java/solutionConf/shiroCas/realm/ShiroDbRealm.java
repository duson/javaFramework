package duson.java.solutionConf.shiroCas.realm;

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

import com.cnit.pubds.domain.common.Account;

public class ShiroDbRealm extends AuthorizingRealm {

	@Resource
	//private IAccountRepository accountRepository;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Account user = (Account) principals.fromRealm(getName()).iterator().next();
		
        if (user != null) {  
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();  
            //info.addStringPermissions(user.getUserPermissions(user.getUserName()));  
            return info;  
        } else {  
            return null;  
        } 
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authcToken;
		
		Account user = new Account();// accountRepository.getUserByName(usernamePasswordToken.getUsername());  
        if (user != null) {  
            SecurityUtils.getSubject().getSession().setAttribute("loginUserInfo", user);  
            return new SimpleAuthenticationInfo(user, "", getName());  
        } else {  
            return null;  
        } 
	}
	
}
