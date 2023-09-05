package top.javahouse.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
@SpringBootApplication
public class SpringBootDemoBannerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoBannerApplication.class);
        /**
         * Banner.Mode.OFF:关闭
         * Banner.Mode.CONSOLE:控制台输出，默认方式
         * Banner.Mode.LOG:日志输出方式
         */
        application.setBannerMode(Banner.Mode.LOG);
        application.run(args);    }

}
