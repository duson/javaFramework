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
public class IpAddressInInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final String REST_DENIED_LIST = "";
	private static final String REST_ALLOWED_LIST = "";
	
	public IpAddressInInterceptor() {
		super(Phase.RECEIVE);
	}
	
	/**
	 * @param phase
	 */
	public IpAddressInInterceptor(String phase) {
		super(phase);
	}

	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ipAddress = request.getRemoteAddr(); // 客户端IP地址
		
		// 允许和拒绝的IP地址
		String[] deniedList = REST_DENIED_LIST.split(",");
		String[] allowedList = REST_ALLOWED_LIST.split(",");
		
		// 拒绝访问的地址
		if (deniedList != null && deniedList.length > 0) {
			for (String deniedIpAddress : deniedList) {
				if (deniedIpAddress.equals(ipAddress)) {
					throw new Fault(new IllegalAccessException("IP address " + ipAddress + " is denied"));
				}
			}
		}
		
		// 允许访问的地址
		if (allowedList != null && allowedList.length > 0) {
			boolean contains = false;
			for (String allowedIpAddress : allowedList) {
				if (allowedIpAddress.equals(ipAddress)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				throw new Fault(new IllegalAccessException("IP address " + ipAddress + " is not allowed"));
			}
		}
	}

}
