package top.javahouse.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/9/7 10:46
 */
@SpringBootApplication
public class SpringBootDemoDockerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoDockerApplication.class);
        application.run(args);
    }
}