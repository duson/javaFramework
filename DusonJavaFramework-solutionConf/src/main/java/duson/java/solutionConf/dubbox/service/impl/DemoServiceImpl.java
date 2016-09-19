package duson.java.solutionConf.dubbox.service.impl;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.Form;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import duson.java.solutionConf.dubbox.service.DemoService;

@Path("test")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class DemoServiceImpl implements DemoService {

  // 参数检测、正则表达式
  @Override
  @GET
  @Path("hello/{name: \\w+}")
  public String sayHello(@NotNull @PathParam("name") String name) {
    return "Hello," + name;
  }

   //Form Post方式：
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public void formPost(@Form DemoServiceImpl msg, @FormParam("systemId")Long systemId){}

}
