package duson.java.solutionConf.springmvc.security;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnit.pubds.domain.common.Account;
import com.cnit.pubds.domain.common.Config;
import com.cnit.pubds.domain.common.Constant;
import com.cnit.pubds.domain.common.annotation.Privilege;
import com.cnit.pubds.web.vo.JsonResult;

public class PrivilegeInterceptor extends HandlerInterceptorAdapter {
	private Account getAccount(){
		Account account = null;
		
		Subject subject = SecurityUtils.getSubject();
		if(subject.getPrincipals() != null){
			List<Object> listPrincipals = subject.getPrincipals().asList();
			if(listPrincipals.size() > 2)
				account = (Account) listPrincipals.get(2);
		}
		
		return account;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof HandlerMethod)) return true;
		HandlerMethod handler2 = (HandlerMethod) handler;
		Privilege priv = handler2.getMethodAnnotation(Privilege.class);

		// 没有声明权限,放行
		if (null == priv) return true;

		// 判断是否登录了系统
		Account account = getAccount();
		if (null == account) {
			handler(request, response, Config.CAS_LOGIN_URL, "未登录");
			return false;
		}else{
			boolean hasRight = false;
			Map<Integer, Map<Integer, Integer>> privilege = account.getPrivilege();
			if(privilege.containsKey(Constant.SYSTEM_ID)){
				Map<Integer, Integer> sysPrivs = privilege.get(Constant.SYSTEM_ID);
				if(sysPrivs.containsKey(priv.priv().getIntValue())){
					Integer opMask = sysPrivs.get(priv.priv().getIntValue());
					hasRight = (opMask & priv.operaion().getIntValue()) > 0;
				}
			}
			
			if(!hasRight){
				handler(request, response, "/error/unauthorized", "无权限");
				return false;
			}
		}

		return super.preHandle(request, response, handler);
	}

	private void handler(HttpServletRequest request, HttpServletResponse response, String url, String msg)
			throws ServletException, IOException, UnsupportedEncodingException {
		/*
		// 需要登录
		if (priv.type() == ResultType.Page) {
			// 传统页面的登录
			request.getRequestDispatcher(url).forward(request, response);
		} else if (priv.type() == ResultType.Json) {
			// ajax页面的登录
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			OutputStream out = response.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
			// 返回json格式的提示
			JsonResult jsonResult = new JsonResult();
			jsonResult.setMsg(msg);
			pw.println(jsonResult);
			pw.flush();
			pw.close();
		}*/
        if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                .getHeader("X-Requested-With")!= null && request  
                .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
        	request.getRequestDispatcher(url).forward(request, response);
        }else{
            PrintWriter writer = response.getWriter();  
			JsonResult jsonResult = new JsonResult();
			jsonResult.setMsg(msg);
            writer.println(jsonResult);  
            writer.flush();  
        }
	}
	
}
