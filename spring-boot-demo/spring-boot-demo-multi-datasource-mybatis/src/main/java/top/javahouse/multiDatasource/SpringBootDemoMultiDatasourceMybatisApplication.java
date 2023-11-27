package top.javahouse.multiDatasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//https://blog.csdn.net/qq1017794482/article/details/128485480
//参考：https://blog.csdn.net/qq_44665283/article/details/129237053
/*
 * @author:javahouse.top
 * @Description: 多数据源
 * @Date: 2023/11/23 9:27
 */
@SpringBootApplication
public class SpringBootDemoMultiDatasourceMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoMultiDatasourceMybatisApplication.class, args);
    }

}

