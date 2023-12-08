package top.flobby.live.im.router.provider.cluster;

import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-08 10:31
 **/

public class ImRouterClusterInvoker<T> extends AbstractClusterInvoker<T> {

    public ImRouterClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @Override
    protected Result doInvoke(Invocation invocation, List list, LoadBalance loadbalance) throws RpcException {
        checkWhetherDestroyed();
        String ip = (String) RpcContext.getContext().get("ip");
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException("ip is null");
        }
        System.out.println("ip: " + ip);
        // 获取指定Rpc服务提供者的所有地址信息
        List<Invoker<T>> invokers = list(invocation);
        Invoker<T> matchInvoker = invokers.stream().filter(invoker -> {
            // 获取服务提供者的ip
            String serverIp = invoker.getUrl().getHost() + ":" + invoker.getUrl().getPort();
            return ip.equals(serverIp);
        }).findFirst().orElse(null);
        if (matchInvoker == null) {
            throw new RuntimeException("no match invoker, ip invalid");
        }
        return matchInvoker.invoke(invocation);
    }
}
