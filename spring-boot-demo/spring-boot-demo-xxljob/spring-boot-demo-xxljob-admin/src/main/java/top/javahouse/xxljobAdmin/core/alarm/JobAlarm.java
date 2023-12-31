package top.javahouse.xxljobAdmin.core.alarm;

import top.javahouse.xxljobAdmin.core.model.XxlJobInfo;
import top.javahouse.xxljobAdmin.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
