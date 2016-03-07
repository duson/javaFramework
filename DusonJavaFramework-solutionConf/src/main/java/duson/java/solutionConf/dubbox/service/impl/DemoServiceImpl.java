package duson.java.solutionConf.dubbox.service.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import duson.java.solutionConf.dubbox.service.DemoService;

@Path("test")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class DemoServiceImpl implements DemoService {

  @Override
  @GET
  @Path("hello/{name}")
  public String sayHello(@PathParam("name") String name) {
    return "Hello," + name;
  }

}
