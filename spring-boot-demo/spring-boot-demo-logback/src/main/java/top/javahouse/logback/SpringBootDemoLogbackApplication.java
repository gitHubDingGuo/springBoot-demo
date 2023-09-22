package top.javahouse.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:启动入口
 * @Date: 2023/9/22 13:43
 */
@SpringBootApplication
public class SpringBootDemoLogbackApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoLogbackApplication.class);
        application.run(args);

    }
}