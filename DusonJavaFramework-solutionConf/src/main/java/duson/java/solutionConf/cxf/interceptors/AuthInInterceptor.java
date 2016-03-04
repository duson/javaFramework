package duson.java.solutionConf.cxf.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;


/**
 * 
 * @author pmchen
 *
 */
public class AuthInInterceptor extends AbstractPhaseInterceptor<Message> {

	public AuthInInterceptor() {
		super(Phase.RECEIVE);
	}
	/**
	 * @param phase
	 */
	public AuthInInterceptor(String phase) {
		super(phase);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String authString = request.getHeader("auth");
				
		if(authString == null || authString.isEmpty())
			throw new Fault(new IllegalAccessException("非法访问"));
	}
	
	public static void main(String[] args)  {
		/*StringEntity paramsEntity = new StringEntity("json内容", "UTF-8");
		paramsEntity.setContentType("application/json");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost:8080/xxx");
		post.setHeader("auth", "");
		post.setEntity(paramsEntity);
		HttpResponse response = client.execute(post);*/
	}

}
