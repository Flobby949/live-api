package top.flobby.live.user.provider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import top.flobby.live.user.interfaces.IUserRpc;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import java.util.HashMap;
import java.util.Map;

class DubboTest {
    private static final String REGISTER_ADDRESS =
            "nacos://106.15.104.19:8848?namespace=test&&username=nacos&&password=nacos";
    private static RegistryConfig registryConfig;
    private static ApplicationConfig applicationConfig;
    private static ReferenceConfig<IUserRpc>
            userRpcReferenceConfig;
    private static Map<Class, Object> referMap = new HashMap<>();

    @BeforeAll
    public static void initConfig() {
        registryConfig = new RegistryConfig();
        applicationConfig = new ApplicationConfig();
        registryConfig.setAddress(REGISTER_ADDRESS);
        applicationConfig.setName("dubbo-test-application");
        applicationConfig.setRegistry(registryConfig);
        userRpcReferenceConfig = new ReferenceConfig<>();
        // roundrobin random leastactive shortestresponse  consistenthash
        userRpcReferenceConfig.setLoadbalance("random");
        userRpcReferenceConfig.setInterface(IUserRpc.class);
        referMap.put(IUserRpc.class,
                userRpcReferenceConfig.get());
    }

    @Test
    public void testUserRpc() {
        IUserRpc userRpc = (IUserRpc)
                referMap.get(IUserRpc.class);
        for (int i = 0; i < 1000; i++) {
            userRpc.test();
        }
    }
}