package duson.java.solutionConf.springmvc.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof HandlerMethod)) return true;
		HandlerMethod handler2 = (HandlerMethod) handler;
		
		Privilege priv = handler2.getMethodAnnotation(Privilege.class);

		// 没有声明权限,放行
		if (null == priv) return true;

		// 判断是否登录了系统
		Object account = null;
		if (null == account) {
			handler(request, response, "登录Url", "未登录");
			return false;
		}else{
			boolean hasRight = false;
			Map<Integer, Integer> privilege = null; // 从其它地方获取用户权限
			if(privilege.containsKey(priv.priv())){
				Integer opMask = privilege.get(priv.priv());
				hasRight = (opMask & priv.operaion()) > 0;
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
            writer.println(msg); // 最好输出Json格式的字符串
            writer.flush();  
        }
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	public @interface Privilege {
		int type() default 0;
		int priv();
		int operaion() default 0;
	}

}
