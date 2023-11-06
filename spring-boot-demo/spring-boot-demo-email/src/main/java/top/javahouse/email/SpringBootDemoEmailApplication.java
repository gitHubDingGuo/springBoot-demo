package top.javahouse.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/9/7 10:46
 */
@SpringBootApplication
public class SpringBootDemoEmailApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoEmailApplication.class);
        application.run(args);
    }
}