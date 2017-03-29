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
            logger.debug(String.format("-----------------------------------------------------\n接口方法：%s.%s \n参数：%s\n结果：%s\n异常：%s, 响应时间：%dms", 
                    invoker.getInterface(), invocation.getMethodName(), JSON.toJSONString(invocation.getArguments()),
                    result != null ? JSON.toJSON(result.getValue()) : "",  
                    result != null ? result.getException() : "",
                    elapsed));
        }

        if(result.hasException()) {
            RpcResult ret = new RpcResult();
            String msg = null;

            String className = result.getException().getClass().getName();
            if(result.getException() instanceof RpcException) {
                RpcException rpcException = (RpcException)result.getException();
                if(rpcException.getCause() instanceof ConstraintViolationException) {
                    ConstraintViolationException ve = (ConstraintViolationException) rpcException.getCause();
                    StringBuilder errorBuilder = new StringBuilder();
                    for (ConstraintViolation cv : ve.getConstraintViolations()) {
                        if(errorBuilder.length() > 0) errorBuilder.append(",");
                        errorBuilder.append(cv.getMessage());
                    }
                    if(errorBuilder.length() > 0)
                        msg = errorBuilder.toString();
                }
            }
            else if (result.getException() instanceof BusinessException || className.startsWith("com.xxx.")) {
                msg = result.getException().getMessage();
            } else if(result.getException() instanceof JsonParseException || result.getException() instanceof UnrecognizedPropertyException) {
                msg = ErrorCode.InvalidJsonFormat.getDesc();
                errorCode = ErrorCode.InvalidJsonFormat.toString();
            } else if (result.getException() instanceof RuntimeException) {
                RuntimeException runtimeException = (RuntimeException)result.getException();
                if(runtimeException.getMessage().startsWith("com.xxx."))
                    msg = runtimeException.getMessage();
            }
            
            // 有统一返回值就返回RpcResult，否则直接抛出异常
            logger.debug(String.format("调用异常：%s", msg), result.getException());
            ret.setValue(msg);
            throw new PayException(msg);
           
            return ret;
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
