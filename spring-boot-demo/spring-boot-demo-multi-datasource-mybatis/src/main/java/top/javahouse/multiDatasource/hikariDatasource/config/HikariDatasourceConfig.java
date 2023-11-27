package top.javahouse.multiDatasource.hikariDatasource.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import top.javahouse.multiDatasource.druidDatasource.config.DynamicDataSource;
import top.javahouse.multiDatasource.druidDatasource.config.DynamicDataSourceConfig;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//参考：https://blog.csdn.net/qq_44665283/article/details/129237053
@Configuration
public class HikariDatasourceConfig {

    /**
     * 创建 orders 数据源的配置对象
     */
    @Primary
    @Bean(name = "ordersDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.orders")
    // 读取 spring.datasource.orders 配置到 DataSourceProperties 对象
    public DataSourceProperties ordersDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 orders 数据源
     */
    @Bean(name = "ordersDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.orders.hikari")
    // 读取 spring.datasource.orders 配置到 HikariDataSource 对象
    public DataSource ordersDataSource() {
        // <1.1> 获得 DataSourceProperties 对象
        DataSourceProperties properties = this.ordersDataSourceProperties();
        // <1.2> 创建 HikariDataSource 对象
        return createHikariDataSource(properties);
    }

    /**
     * 创建 users 数据源的配置对象
     */
    @Bean(name = "usersDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.users")
    // 读取 spring.datasource.users 配置到 DataSourceProperties 对象
    public DataSourceProperties usersDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 users 数据源
     */
    @Bean(name = "usersDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.users.hikari")
    public DataSource usersDataSource() {
        // 获得 DataSourceProperties 对象
        DataSourceProperties properties = this.usersDataSourceProperties();
        // 创建 HikariDataSource 对象
        return createHikariDataSource(properties);
    }

    private static HikariDataSource createHikariDataSource(DataSourceProperties properties) {
        // 创建 HikariDataSource 对象
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        // 设置线程池名
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

    @Bean(name = "myHikariDataSource")
    public MyHikariDataSource dataSource(HikariDataSource ordersDataSource,HikariDataSource usersDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(HikariDatasourceConfig.DataSourceNames.HIKARI_DEFAULT, ordersDataSource);
        targetDataSources.put(HikariDatasourceConfig.DataSourceNames.HIKARI_ORDER, ordersDataSource);
        targetDataSources.put(HikariDatasourceConfig.DataSourceNames.HIKARI_USER, usersDataSource);
        return new MyHikariDataSource(ordersDataSource, targetDataSources);
    }


    public static class DataSourceNames {
        public static final String HIKARI_DEFAULT = "hikari_default";
        public static final String HIKARI_ORDER= "hikari_order";
        public static final String HIKARI_USER = "hikari_user";
    }



}
