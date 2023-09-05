package top.javahouse.adminServer;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
//启动admin服务端服务
@EnableAdminServer
@SpringBootApplication
public class SpringBootDemoAdminServerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAdminServerApplication.class);
        application.run(args);
    }

}
