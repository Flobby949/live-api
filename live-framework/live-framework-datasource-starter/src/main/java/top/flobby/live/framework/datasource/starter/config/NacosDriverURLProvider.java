package top.flobby.live.framework.datasource.starter.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 从 nacos 读取 shardingJDBC 配置
 * @create : 2023-11-30 12:49
 **/


public class NacosDriverURLProvider implements ShardingSphereDriverURLProvider {

    public static final Logger logger = LoggerFactory.getLogger(NacosDriverURLProvider.class);
    public static final String NACOS_TYPE = "nacos:";
    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    public static final String DEFAULT_NACOS_ACCOUNT = "nacos";

    /**
     * 判断 url 是否符合 nacos 的格式
     * 判断当前的配置是否是 nacos 的配置
     *
     * @param url 网址
     * @return boolean
     */
    @Override
    public boolean accept(String url) {
        return StringUtils.isNotBlank(url) && url.contains(NACOS_TYPE);
    }

    /**
     * 获取nacos上sharding-jdbc的配置内容
     *
     * @param url 网址
     * @return {@link byte[]}
     */
    @Override
    public byte[] getContent(String url) {
        if (StringUtils.isEmpty(url)) {
            return new byte[0];
        }
        // 得到例如：127.0.0.1:8848:live-user-sharding-jdbc.yaml?username=nacos&&password=nacos&&namespace=test 格式的 url
        String nacosUrl = url.substring(url.lastIndexOf(NACOS_TYPE) + NACOS_TYPE.length());
        /*
         * 得到三个字符串，分别是：
         * 127.0.0.1
         * 8848
         * 账号密码非必须
         * live-user-sharding-jdbc.yaml?namespace=test&&username=nacos&&password=nacos
         */
        String[] nacosStr = nacosUrl.split(":");
        String nacosFileStr = nacosStr[2];
        /*
         * 得到两个字符串
         * live-user-sharding-jdbc.yaml
         * namespace=test&&username=nacos&&password=nacos
         */
        String[] nacosFileProp = nacosFileStr.split("\\?");
        String dataId = nacosFileProp[0];
        Properties properties = getProperties(nacosFileProp, nacosStr);
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, DEFAULT_GROUP, 6000);
            // logger.info(content);
            return content.getBytes();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取属性
     *
     * @param nacosFileProp nacos 上配置文件的属性
     * @param nacosStr      nacos 配置的属性
     * @return {@link Properties}
     */
    private static Properties getProperties(String[] nacosFileProp, String[] nacosStr) {
        String[] acceptProp = nacosFileProp[1].split("&&");
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosStr[0] + ":" + nacosStr[1]);
        for (String propertyName : acceptProp) {
            String[] propertyItem = propertyName.split("=");
            String key = propertyItem[0];
            String value = propertyItem[1];
            // 设置默认账号密码
            properties.setProperty(PropertyKeyConst.USERNAME, DEFAULT_NACOS_ACCOUNT);
            properties.setProperty(PropertyKeyConst.PASSWORD, DEFAULT_NACOS_ACCOUNT);
            if (PropertyKeyConst.USERNAME.equals(key)) {
                properties.setProperty(PropertyKeyConst.USERNAME, value);
            } else if (PropertyKeyConst.PASSWORD.equals(key)) {
                properties.setProperty(PropertyKeyConst.PASSWORD, value);
            } else if (PropertyKeyConst.NAMESPACE.equals(key)) {
                properties.setProperty(PropertyKeyConst.NAMESPACE, value);
            }
        }
        return properties;
    }
}
