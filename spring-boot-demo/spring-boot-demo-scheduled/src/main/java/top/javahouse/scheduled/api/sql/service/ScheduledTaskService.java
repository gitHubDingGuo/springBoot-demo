package top.javahouse.scheduled.api.sql.service;

import top.javahouse.scheduled.api.sql.ScheduledJob;

public interface ScheduledTaskService{

    Boolean start(ScheduledJob scheduledJob);

    Boolean stop(String jobKey);

    Boolean restart(ScheduledJob scheduledJob);

    void initTask();
}