package top.javahouse.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/*
 * @author:javahouse.top
 * @Description: 执行顺序依次是：Filter、DispatcherServlet、HandlerInterceptor、Controller。
 * @Date: 2023/9/7 10:46
 */
@SpringBootApplication
@ServletComponentScan(basePackages = {"top.javahouse.filter", "cn.my.service.filter"})
public class SpringBootDemoFilterApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoFilterApplication.class);
        application.run(args);
    }
}