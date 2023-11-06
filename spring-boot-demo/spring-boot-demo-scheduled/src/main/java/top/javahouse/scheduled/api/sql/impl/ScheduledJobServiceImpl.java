package top.javahouse.scheduled.api.sql.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.scheduled.api.sql.ScheduledJob;
import top.javahouse.scheduled.api.sql.mapper.ScheduledJobMapper;
import top.javahouse.scheduled.api.sql.service.ScheduledJobService;
import top.javahouse.scheduled.api.sql.service.ScheduledTaskService;

@Service
@Slf4j
public class ScheduledJobServiceImpl extends ServiceImpl<ScheduledJobMapper, ScheduledJob> implements ScheduledJobService {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Override
    public boolean updateOne(ScheduledJob scheduledJob) {
        if(updateById(scheduledJob))
            scheduledTaskService.restart(getById(scheduledJob.getJobId()));
        return true;
    }


}