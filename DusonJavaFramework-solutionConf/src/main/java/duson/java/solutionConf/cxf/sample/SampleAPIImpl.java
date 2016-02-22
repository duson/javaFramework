package duson.java.solutionConf.cxf.sample;

import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Service;


@Service
@WebService(endpointInterface = "com.cnit.pubds.webservice.material.MaterialAPI")
public class SampleAPIImpl implements SampleAPI {
	
	@Override
	public WSResult test(String json, String auth) {
		WSResult result = new WSResult();
				
		return result;
	}

}
