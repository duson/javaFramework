package duson.java.solutionConf.springmvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 这个类必须声明到Spring中去，才能实现全局异常处理
 * 为了简单的进行异常处理，Spring提供了SimpleMappingExceptionResolver类
 */
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
	protected static final Logger logger = LoggerFactory.getLogger(CustomHandlerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// 将漏掉的异常记录到日志文件中
		logger.error(ex.getMessage(), ex);
		
		// 判断是否为Ajax请求
		// 如果是ajax请求响应头会有，x-requested-with；
		if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			response.setContentType("Content-type:application/json;charset=utf-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.write("json字符串");
			} catch (IOException e1) {
				logger.error(e1.getMessage(), e1);
			} finally {
				writer.flush();
				writer.close();
			}
			return null;
		}
		return new ModelAndView(new RedirectView("/500"));
	}
}
