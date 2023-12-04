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
        return super.invoke(invoker, invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        // 如果有异常
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            // 获取抛出的异常
            Throwable exception = appResponse.getException();
            String classname = exception.getClass().getName();
            // 如果是自定义异常，直接抛出
            if (classname.startsWith("top.flobby.live.common.exception")) {
                return;
            }
            // 如果是其他异常，使用Dubbo的业务进行处理
            super.onResponse(appResponse, invoker, invocation);
        }
    }
}

