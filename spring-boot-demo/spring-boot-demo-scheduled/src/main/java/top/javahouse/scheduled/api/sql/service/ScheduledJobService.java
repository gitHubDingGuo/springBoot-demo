package top.javahouse.scheduled.api.sql.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javahouse.scheduled.api.sql.ScheduledJob;

public interface ScheduledJobService extends IService<ScheduledJob> {

    /**
     * 修改定时任务，并重新启动
     * @param scheduledJob
     * @return
     */
    boolean updateOne(ScheduledJob scheduledJob);

}