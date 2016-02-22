package duson.java.solutionConf.cxf.sample;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface SampleAPI {
	
	/**
	 * @param json
	 * 作为Url参数，添加@QueryParam，不加则作为Post内容Body
	 * {deviceId: 211, day: "2014-02-01", materialHits: [{md5: "xxxx", hits: 11}, {md5: "xxxx", hits: 11}, ...]}
	 * @return
	 */
	@POST
	@Path("/test")
	@Produces({ "application/json;charset=UTF-8" })
	WSResult test(@QueryParam("data") String json, @HeaderParam("auth") String auth);
}
