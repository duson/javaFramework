package duson.java.solutionConf.swagger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("rest/v1")
@Api(value="InnerService", description="InnerService desc", produces="application/json;charset=UTF-8")
public interface Example  {

	// 如果返回列表，responseContainer="List"
	@Path("test/{name: \\d+}")
	@ApiOperation(value="测试方法", notes="测试方法备注", httpMethod = "GET", response = String.class, responseContainer="List", produces="application/json;charset=UTF-8")
	String test(@PathParam("name") @ApiParam(value="名称", required=true) String name);
	
}
