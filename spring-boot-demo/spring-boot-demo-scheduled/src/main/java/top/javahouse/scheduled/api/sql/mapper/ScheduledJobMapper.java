package top.javahouse.scheduled.api.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javahouse.scheduled.api.sql.ScheduledJob;

@Mapper
public interface ScheduledJobMapper extends BaseMapper<ScheduledJob> {
}