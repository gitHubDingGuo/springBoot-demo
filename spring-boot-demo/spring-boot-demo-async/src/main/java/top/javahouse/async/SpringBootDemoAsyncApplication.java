package top.javahouse.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * @author:javahouse.top
 * @Date: 2023/9/5 13:33
 */
//开启异步
@EnableAsync
@SpringBootApplication
public class SpringBootDemoAsyncApplication {

    @Autowired
    AsyncCaller caller;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAsyncApplication.class);
        application.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("---原生调用----");
            Thread.sleep(1000);
            caller.caller();
            System.out.println("---自定义调用----");
            Thread.sleep(1000);
            caller.poolOne();
            caller.poolTwo();
        };
    }

}
