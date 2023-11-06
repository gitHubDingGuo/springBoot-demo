package top.javahouse.scheduled.api.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/*
 * @author:javahouse.top
 * @Description:基于注解的方式
 * @Date: 2023/10/27 18:00
 */
@Configuration
@EnableScheduling //开启定时任务
public class AnnotationScheduled {

    //添加定时任务
    @Scheduled(cron = "0/5 * * * * ?")
    private void myTasks() {
        System.out.println("执行定时任务 " + LocalDateTime.now());
    }

}
