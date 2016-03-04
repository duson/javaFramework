package duson.java.solutionConf.cxf.sample;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import duson.java.solutionConf.cxf.dto.WSResult;


@Service
@WebService(endpointInterface = "接口全类名")
public class SampleAPIImpl implements SampleAPI {
	
	@Override
	public WSResult test(String json, String auth) {
		WSResult result = new WSResult();
				
		return result;
	}

}
