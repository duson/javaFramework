package duson.java.solutionConf.cxf.interceptors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import duson.java.solutionConf.cxf.dto.WSResult;

public class ExceptionHandler implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException ex) {
		WSResult result = new WSResult();
		result.setMsg(ex.getMessage());
		return Response.serverError().entity(result).build();
	}

}
