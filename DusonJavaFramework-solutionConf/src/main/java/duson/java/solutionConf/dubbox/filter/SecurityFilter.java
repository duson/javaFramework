package duson.java.solutionConf.dubbox.filter;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

public class SecurityFilter implements Filter {
	static final Log logger = LogFactory.getLog("custom");
	
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		HttpServletRequest req = (HttpServletRequest) RpcContext.getContext().getRequest();
				
		long start = System.currentTimeMillis();  
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;  
        if (invoker.getUrl() != null) {  
            // log.info("[" +invoker.getInterface() +"] [" + invocation.getMethodName() +"] [" + elapsed +"]" );  
        	logger.debug(String.format("[{}], [{}], {}, [{}], [{}], [{}]", invoker.getInterface(), invocation.getMethodName(),   
                    JSON.toJSON(invocation.getArguments()), result != null ? JSON.toJSON(result.getValue()) : "",  
                    result != null ? result.getException() : "", elapsed));
        }

        // 获取请求参数

        RestRequest requestParams = null;
        
        for(Object arg : args) {
            if(arg instanceof RestRequest) {
                requestParams = (RestRequest)arg;
                break;
            }
        }
        if(requestParams == null) {
            retVal.setErrorMsg("非法访问");
            return new RpcResult(retVal);
        }
        
        //WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());
        
        String json = JSON.toJSONString(requestParams);
        Map<String, Object> params = JSON.parseObject(json, Map.class, Feature.OrderedField);
        
        return result;
	}

}
