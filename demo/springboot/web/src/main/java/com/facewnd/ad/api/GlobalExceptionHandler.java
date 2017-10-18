package com.facewnd.ad.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.facewnd.ad.common.enumeration.ErrorCode;
import com.facewnd.ad.common.exception.AdException;
import com.facewnd.ad.common.model.RestResult;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(value = { MethodArgumentNotValidException.class, IllegalArgumentException.class })
	@ResponseBody
	public RestResult<String> argumentHandler(HttpServletRequest req, Exception e) throws Exception {
		logger.warn("参数异常", e);
		
		StringBuilder message = new StringBuilder();
		if(e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;
			BindingResult bindingResult = ex.getBindingResult();  
		    for (FieldError fieldError : bindingResult.getFieldErrors()) {
		    	if(message.length() != 0)
		    		message.append("\n");
		    	message.append(fieldError.getDefaultMessage());  
		    }
		    if(message.length() == 0)
		    	message.append(ErrorCode.InvalidJsonFormat.getDesc());
		}
		return new RestResult<String>(false, null)
				.setErrorCode(ErrorCode.InvalidParams.toString())
				.setErrorMsg(message.toString());
	}

	@ExceptionHandler(value = AdException.class)
	@ResponseBody
	public RestResult<String> bizErrorHandler(HttpServletRequest req, AdException e) throws Exception {
		logger.warn("业务异常", e);
		return new RestResult<String>(false, null).setErrorCode(e.getErrorCode()).setErrorMsg(e.getMessage());
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	public RestResult<String> handleHttpMessageNotReadable(HttpServletRequest req, HttpMessageNotReadableException e) {
		logger.error("Json参数不正确", e);
		return new RestResult<String>(false, null).setErrorCode(ErrorCode.InvalidJsonFormat.toString()).setErrorMsg(ErrorCode.InvalidJsonFormat.getDesc());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public RestResult<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		logger.error("系统异常", e);
		return new RestResult<String>(false, null).setErrorCode(ErrorCode.Error.toString()).setErrorMsg("服务不可用，请稍候访问");
	}

	@ExceptionHandler(value = Throwable.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
}
