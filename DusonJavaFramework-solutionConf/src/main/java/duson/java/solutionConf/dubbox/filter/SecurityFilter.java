package duson.java.solutionConf.dubbox.filter;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

public class SecurityFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		HttpServletRequest req = (HttpServletRequest) RpcContext.getContext().getRequest();
		
		return invoker.invoke(invocation);
	}

}
