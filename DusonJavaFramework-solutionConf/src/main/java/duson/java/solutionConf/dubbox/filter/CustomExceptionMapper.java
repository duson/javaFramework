package duson.java.solutionConf.dubbox.filter;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.http.MediaType;


public class CustomExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception arg0) {
		
		Object result = new Object();
		//result.setErrorMsg(arg0.getMessage());
		return Response.ok(result, MediaType.APPLICATION_JSON_UTF8_VALUE).build();
	}

}
