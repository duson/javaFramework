package duson.java.solutionConf.cxf.interceptors;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * 
 * @author pmchen
 *
 */
public class AuthInInterceptor extends AbstractPhaseInterceptor<Message> {

	public DeviceAuthInInterceptor() {
		super(Phase.RECEIVE);
	}
	/**
	 * @param phase
	 */
	public DeviceAuthInInterceptor(String phase) {
		super(phase);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String authString = request.getHeader("auth");
				
		if(authString == null || authString.isEmpty())
			throw new Fault(new IllegalAccessException("非法访问"));
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		StringEntity paramsEntity = new StringEntity("json内容", "UTF-8");
		paramsEntity.setContentType("application/json");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost:8080/xxx");
		post.setHeader("auth", "");
		post.setEntity(paramsEntity);
		HttpResponse response = client.execute(post);
	}

}
