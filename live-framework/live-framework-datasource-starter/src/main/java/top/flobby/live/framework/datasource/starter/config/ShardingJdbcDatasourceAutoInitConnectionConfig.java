package top.flobby.live.framework.datasource.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 数据源初始化
 * @create : 2023-11-19 12:39
 **/

@Slf4j
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            log.info("dataSource: " + dataSource);
            Connection connection = dataSource.getConnection();
        };
    }
}

