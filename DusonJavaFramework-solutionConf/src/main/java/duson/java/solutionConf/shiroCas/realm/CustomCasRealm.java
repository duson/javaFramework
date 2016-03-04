package duson.java.solutionConf.shiroCas.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.util.WebUtils;


/** 
 	// 获取用户信息
    Subject subject = SecurityUtils.getSubject();
	if(subject.getPrincipals() != null){
		List<Object> listPrincipals = subject.getPrincipals().asList();
		if(listPrincipals.size() > 2)
			account = (Account) listPrincipals.get(2);
	}
	
	// 得到Session
	Session session = SecurityUtils.getSubject().getSession();
 *
 */
public class CustomCasRealm extends CasRealm {
	private static final String SESSION_USER_ID = "";
	private static final String SESSION_PRIVILEGE = "";

	@SuppressWarnings("unchecked")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		
		AuthenticationInfo info = super.doGetAuthenticationInfo(authcToken);
		if(info == null || info.getPrincipals() == null) return null;
		
		List<Object> principals = info.getPrincipals().asList();
		Map<?, ?> attributes = (Map<?, ?>) principals.get(1);
		
		String userIdString = principals.get(0).toString();
		if(attributes.get("UserID") != null)
			userIdString = attributes.get("UserID").toString();
		if(userIdString == null || userIdString.isEmpty())
			throw new CasAuthenticationException("user invalid");
		
    	long userId = Long.parseLong(userIdString);
    	
    	try {
	    	Object account = null; // 根据用户ID（userId）通过Rpc或数据库得到用户信息
	        if(account == null)
	        	throw new CasAuthenticationException("user invalid");
	        
	        Session session = SecurityUtils.getSubject().getSession();
	        session.setAttribute(SESSION_USER_ID, userId);
	        
	        List<Object> list = CollectionUtils.asList("用户名", principals.get(1));
	        list.add(account);
	        
	        PrincipalCollection principalCollection = new SimplePrincipalCollection(list, getName());
	        return new SimpleAuthenticationInfo(principalCollection, (String)((CasToken) authcToken).getCredentials());
    	} catch(Exception e) {
    		return null;
    	}
        /*
        CasToken casToken = (CasToken) authcToken;
        if (authcToken == null) {
            return null;
        }
        
        String ticket = (String)casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }
        
        TicketValidator ticketValidator = ensureTicketValidator();

        try {
            // contact CAS server to validate service ticket
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
            // get principal, user id and attributes
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userId = casPrincipal.getName();

            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            casToken.setUserId(userId);
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                casToken.setRememberMe(true);
            }
            // create simple authentication info
            List<Object> principals = CollectionUtils.asList(userId, attributes);
            
            Account account = commonMapper.getAccountDetails(userId);
            if(account != null)
            	principals.add(account);
            
            PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
            return new SimpleAuthenticationInfo(principalCollection, ticket);
        } catch (TicketValidationException e) { 
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
        */
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		List<String> permissions = new ArrayList<String>();
		Session session = SecurityUtils.getSubject().getSession();
		
		// 如果从其它子系统跳转过来的，需要重新读取权限信息
		HttpServletRequest request = WebUtils.getHttpRequest(SecurityUtils.getSubject());
		String hostName = request.getServerName();
		if(request.getServerPort() != 80)
			hostName += ":" + request.getServerPort();
		String referer = request.getHeader("referer");
		String refererHostName = "";
		if(referer != null && referer.length() > 0) {
			if(referer.indexOf("//") > 0)
				refererHostName = referer.substring(referer.indexOf("//") + 2);
			if(referer.indexOf("/") > 0)
				refererHostName = refererHostName.substring(0, refererHostName.indexOf("/"));
		}
		if(referer != null && !hostName.toLowerCase().equals(refererHostName.toLowerCase()))
			session.removeAttribute(SESSION_PRIVILEGE);
		
		Object objPermissioin = session.getAttribute(SESSION_PRIVILEGE);
		if(objPermissioin == null){
			List<Object> principalList = principals.asList();
			Map attributes = (Map) principalList.get(1);
			Object account = principalList.get(2);
			
			// 由于CAS不支持实时更新权限，暂时使用数据查询权限的方式
			/*if(attributes.containsKey("PrivIdS") && attributes.get("PrivIdS") != null && !attributes.get("PrivIdS").toString().isEmpty()){
			 	String PrivIdS = attributes.get("PrivIdS").toString();
			 	
			 	// 提取权限和操作
			 	Pattern pattern = Pattern.compile("(\\d+)=(\\d+)");
			 	Matcher matcher = pattern.matcher(PrivIdS);
			 	int key, value;
			 	Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			 	while (matcher.find()) {
			 		key = Integer.parseInt(matcher.group(1));
			 		value = Integer.parseInt(matcher.group(2));
			 		if(map.containsKey(key)){
			 			map.put(key, map.get(key) | value);
			 		}else{
			 			map.put(key, value);
			 		}
			 	}
			 	logger.error("合并后："+map.toString());
			 	
			 	// 转化为Shiro功能集合
		 		for (Object obj : map.entrySet()) {		 	
		            Entry<Integer, Integer> entry = (Entry<Integer, Integer>) obj;
		            key = (Integer) entry.getKey();
		            value = (Integer) entry.getValue();
					int i = 0;
					do{
						if((value & 1) > 0){
							if(i==0)
								permissions.add(buildPermission(String.valueOf(key), String.valueOf(((long)Math.pow(2, i)))));
							else
								permissions.add(buildPermission(String.valueOf(key), String.valueOf(((long)Math.pow(2, i)))) + ":" + companyID);
						}
						i++;
					}while((value = value >> 1) > 0);
		 		}
			}else{*/
				// 从数据获取功能和操作
				List<Privilege> privs = null;
				// 转化为Shiro功能集合
				long privID, mask;
				for(Privilege pri : privs){
					privID = pri.getPrivID();
					mask = pri.getOperaMask();
					int i = 0;
					
					// 默认给予查看权限 0
					permissions.add(buildPermission(String.valueOf(privID), "0"));
					
					do{
						if((mask & 1) > 0){
							permissions.add(buildPermission(String.valueOf(privID), String.valueOf(((long)Math.pow(2, i)))));
						}
						i++;
					}while((mask = mask >> 1) > 0);
				}
			//}
			
			session.setAttribute(SESSION_PRIVILEGE, permissions);
		}else{
			permissions = (List<String>)objPermissioin;
		}
		
		SimpleAuthorizationInfo simpleAuthorizationInfo = (SimpleAuthorizationInfo) super.doGetAuthorizationInfo(principals);
		simpleAuthorizationInfo.addStringPermissions(permissions);
		
		return simpleAuthorizationInfo;
	}
	
	private String buildPermission(String priv, String opera){
		return String.format("%1$s:%2$s", priv, opera);
	}
	
	public class Privilege {
		private long privID;
		private long operaMask;
		
		public long getPrivID() {
			return privID;
		}
		public void setPrivID(long privID) {
			this.privID = privID;
		}
		public long getOperaMask() {
			return operaMask;
		}
		public void setOperaMask(long operaMask) {
			this.operaMask = operaMask;
		}
	}
}
