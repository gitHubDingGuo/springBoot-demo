package top.javahouse.scheduled.api.sql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("scheduled_job")
public class ScheduledJob {

    @TableId(value = "job_id",type = IdType.AUTO)
    private Integer jobId;

    private String jobKey;

    private String cronExpression;

    private String taskExplain;

    private Integer status;

}