package top.javahouse.multiDatasource.druidDatasource.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置多数据源
 */
@Configuration
public class DynamicDataSourceConfig {


    @Bean
    @ConfigurationProperties("spring.datasource.druid.default")
    public DataSource defaultDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.ds1")
    public DataSource firstDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.ds2")
    public DataSource secondDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource defaultDataSource, DataSource firstDataSource, DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceNames.DEFAULT, defaultDataSource);
        targetDataSources.put(DataSourceNames.DS_1, firstDataSource);
        targetDataSources.put(DataSourceNames.DS_2, secondDataSource);
        return new DynamicDataSource(defaultDataSource, targetDataSources);
    }


    public static class DataSourceNames {
        public static final String DEFAULT = "default";
        public static final String DS_1 = "ds1";
        public static final String DS_2 = "ds2";
    }
}
