package top.javahouse.scheduled.api.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.javahouse.scheduled.api.sql.service.ScheduledTaskService;

@Slf4j
@Component
public class ScheduledTaskRunner implements ApplicationRunner {
    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("----初始化定时任务开始----");
        scheduledTaskService.initTask();
        log.info("----初始化定时任务完成----");
    }
}