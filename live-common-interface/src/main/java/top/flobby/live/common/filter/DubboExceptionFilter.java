package top.flobby.live.common.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : Dubbo异常拦截
 * @create : 2023-12-03 18:42
 **/

@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter extends ExceptionFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation); // 获取rpcResult
        // 如果没有异常发生 或者是通用服务就直接返回
        // 也可以沿用源码中 if (result.hasException() && GenericService.class != invoker.getInterface()){} return result;在else中返回的做法
        if (!result.hasException() || GenericService.class == invoker.getInterface()) {
            return result;
        }
        Throwable exception = result.getException(); // 获取抛出的异常
        String classname = exception.getClass().getName();
        // 如果异常是我们这个路径里的异常，直接抛出，否则交给父类处理
        // 或者你们可以根据自己的需求进行放行，只需要完成判断后如果不符合就交给父类执行，否则就自己执行
        if (classname.startsWith("top.flobby.live.common.exception")) {
            return result;
        }
        return super.invoke(invoker, invocation);
    }
}

