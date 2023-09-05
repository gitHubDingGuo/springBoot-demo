package top.javahouse.adminClients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Proxy;

/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
@SpringBootApplication
public class SpringBootDemoAdminClientsApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAdminClientsApplication.class);
        application.run(args);
    }
}
