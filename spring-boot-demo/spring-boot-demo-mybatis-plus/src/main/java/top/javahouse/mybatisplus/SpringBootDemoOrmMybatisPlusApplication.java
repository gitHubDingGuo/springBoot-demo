package top.javahouse.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 15:31
 */
//@MapperScan(basePackages = {"top.javahouse.mybatisplus.mapper"})
@SpringBootApplication
public class SpringBootDemoOrmMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoOrmMybatisPlusApplication.class, args);
    }
}
