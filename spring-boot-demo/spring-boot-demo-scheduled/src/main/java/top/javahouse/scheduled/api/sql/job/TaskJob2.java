package top.javahouse.scheduled.api.sql.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javahouse.scheduled.api.sql.ScheduledOfTask;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TaskJob2 implements ScheduledOfTask {
    @Override
    public void execute() {
        log.info("执行任务2 "+ LocalDateTime.now());
    }
}