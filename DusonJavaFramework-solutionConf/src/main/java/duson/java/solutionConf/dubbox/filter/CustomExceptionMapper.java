package duson.java.solutionConf.dubbox.filter;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.log4j.Logger;

import org.springframework.http.MediaType;

public class CustomExceptionMapper implements ExceptionMapper<Exception> {
	
	static final Logger logger = Logger.getLogger("custom");
	@Override
	public Response toResponse(Exception arg0) {
		Object result = new Object();

		String msg = "操作失败，请稍候访问";
		if(arg0 instanceof CreditException)
			msg = ((CreditException)arg0).getMessage();
		else if(ex instanceof JsonParseException || ex instanceof UnrecognizedPropertyException) 
			msg = "参数格式不正确";
		else if(arg0 instanceof RuntimeException)
			msg = arg0.getMessage();
		result.setErrorMsg(msg);
		logger.error("CustomExceptionMapper", arg0);

		return Response.ok(result, MediaType.APPLICATION_JSON_UTF8_VALUE).build();
	}

}
