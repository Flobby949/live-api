package top.flobby.live.common.utils;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 保证每个 docker 容器的日志挂载目录唯一性
 * @create : 2023-11-29 17:01
 **/

public class IpLogConversionRule extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return this.getLogIndex();
    }

    private String getLogIndex() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000));
    }
}
