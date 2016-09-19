package duson.java.solutionConf.dubbox.filter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import com.alibaba.dubbo.rpc.protocol.rest.RestConstraintViolation;
import com.alibaba.dubbo.rpc.protocol.rest.RpcExceptionMapper;
import com.alibaba.dubbo.rpc.protocol.rest.ViolationReport;

public class ValidationExceptionMapper extends RpcExceptionMapper {
	
	// 接口检验的错误信息
	protected Response handleConstraintViolationException(ConstraintViolationException cve) {
        ViolationReport report = new ViolationReport();
        for (ConstraintViolation cv : cve.getConstraintViolations()) {
            report.addConstraintViolation(new RestConstraintViolation(
                    cv.getPropertyPath().toString(),
                    cv.getMessage(),
                    cv.getInvalidValue() == null ? "null" : cv.getInvalidValue().toString()));
        }

        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation cv : cve.getConstraintViolations()) {
            errors.add(cv.getMessage());
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(report).type(ContentType.APPLICATION_JSON_UTF8_VALUE).build();
    }
}
