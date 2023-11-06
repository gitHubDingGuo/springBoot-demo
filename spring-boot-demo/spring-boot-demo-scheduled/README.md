# spring-boot-demo-scheduled

> 此 demo 主要演示了 Spring Boot 如何使用scheduled

## AnnotationScheduled.java
```java
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
```
## ScheduledJobController.java
```java
package top.javahouse.scheduled.api.sql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.scheduled.api.sql.ScheduledJob;
import top.javahouse.scheduled.api.sql.service.ScheduledJobService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/job")
public class ScheduledJobController {

    @Autowired
    private ScheduledJobService scheduledJobService;

    @PostMapping(value = "/update")
    public ResponseEntity<String> update(HttpServletRequest request, ScheduledJob scheduledJob) {
        if (scheduledJobService.updateOne(scheduledJob)) {
            return ResponseEntity.ok("修改成功");
        } else {
            return ResponseEntity.ok("修改成功");

        }

    }
}

```
## ScheduledJobServiceImpl.java
```java
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
```
## ScheduledTaskServiceImpl.java
```java
package top.javahouse.scheduled.api.sql.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.javahouse.scheduled.api.sql.ScheduledJob;
import top.javahouse.scheduled.api.sql.ScheduledOfTask;
import top.javahouse.scheduled.api.sql.SpringContextUtil;
import top.javahouse.scheduled.api.sql.service.ScheduledJobService;
import top.javahouse.scheduled.api.sql.service.ScheduledTaskService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    /**
     * 可重入锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 定时任务线程池
     */
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 启动状态的定时任务集合
     */
    public Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    @Autowired
    private ScheduledJobService scheduledJobService;

    @Override
    public Boolean start(ScheduledJob scheduledJob) {
        String jobKey = scheduledJob.getJobKey();
        log.info("启动定时任务"+jobKey);
        //添加锁放一个线程启动，防止多人启动多次
        lock.lock();
        log.info("加锁完成");

        try {
            if(this.isStart(jobKey)){
                log.info("当前任务在启动状态中");
                return false;
            }
            //任务启动
            this.doStartTask(scheduledJob);
        } finally {
            lock.unlock();
            log.info("解锁完毕");
        }

        return true;
    }

    /**
     * 任务是否已经启动
     */
    private Boolean isStart(String taskKey) {
        //校验是否已经启动
        if (scheduledFutureMap.containsKey(taskKey)) {
            if (!scheduledFutureMap.get(taskKey).isCancelled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean stop(String jobKey) {
        log.info("停止任务 "+jobKey);
        boolean flag = scheduledFutureMap.containsKey(jobKey);
        log.info("当前实例是否存在 "+flag);
        if(flag){
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(jobKey);

            scheduledFuture.cancel(true);

            scheduledFutureMap.remove(jobKey);
        }
        return flag;
    }

    @Override
    public Boolean restart(ScheduledJob scheduledJob) {
        log.info("重启定时任务"+scheduledJob.getJobKey());
        //停止
        this.stop(scheduledJob.getJobKey());

        return this.start(scheduledJob);
    }

    /**
     * 执行启动任务
     */
    public void doStartTask(ScheduledJob sj){
        log.info(sj.getJobKey());
        if(sj.getStatus().intValue() != 1)
            return;
        Class<?> clazz;
        ScheduledOfTask task;
        try {
            clazz = Class.forName(sj.getJobKey());
            task = (ScheduledOfTask) SpringContextUtil.getBean(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("spring_scheduled_cron表数据" + sj.getJobKey() + "有误", e);
        }
        Assert.isAssignable(ScheduledOfTask.class, task.getClass(), "定时任务类必须实现ScheduledOfTask接口");
        ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(task,(triggerContext -> new CronTrigger(sj.getCronExpression()).nextExecutionTime(triggerContext)));
        scheduledFutureMap.put(sj.getJobKey(),scheduledFuture);
    }

    @Override
    public void initTask() {
        List<ScheduledJob> list = scheduledJobService.list();
        for (ScheduledJob sj : list) {
            if(sj.getStatus().intValue() == -1) //未启用
                continue;
            doStartTask(sj);
        }
    }
}
```
## TaskJob1.java
```java
package top.javahouse.scheduled.api.sql.job;
import top.javahouse.scheduled.api.sql.job.TaskJob1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javahouse.scheduled.api.sql.ScheduledOfTask;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TaskJob1 implements ScheduledOfTask {
    @Override
    public void execute() {
        log.info("执行任务1 "+ LocalDateTime.now());
    }
}
```
## TaskJob2.java
```java
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
```
## ScheduledJobMapper.java
```java
package top.javahouse.scheduled.api.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javahouse.scheduled.api.sql.ScheduledJob;

@Mapper
public interface ScheduledJobMapper extends BaseMapper<ScheduledJob> {
}
```
## ScheduledConfig.java
```java
package top.javahouse.scheduled.api.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Slf4j
public class ScheduledConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        log.info("创建定时任务调度线程池 start");
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("taskExecutor-");
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        log.info("创建定时任务调度线程池 end");
        return threadPoolTaskScheduler;
    }

}
```
## ScheduledJob.java
```java
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
```
## ScheduledOfTask.java
```java
package top.javahouse.scheduled.api.sql;

public interface ScheduledOfTask extends Runnable{

    void execute();

    @Override
    default void run() {
        execute();
    }
}
```
## ScheduledTaskRunner.java
```java
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
```
## ScheduledJobService.java
```java
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
```
## ScheduledTaskService.java
```java
package top.javahouse.scheduled.api.sql.service;

import top.javahouse.scheduled.api.sql.ScheduledJob;

public interface ScheduledTaskService{

    Boolean start(ScheduledJob scheduledJob);

    Boolean stop(String jobKey);

    Boolean restart(ScheduledJob scheduledJob);

    void initTask();
}
```
## SpringContextUtil.java
```java
package top.javahouse.scheduled.api.sql;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {


    private static ApplicationContext applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtil.applicationContext == null){
            SpringContextUtil.applicationContext  = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
```
## SpringBootDemoScheduledApplication.java
```java
package top.javahouse.scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootDemoScheduledApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoScheduledApplication.class, args);
    }
}
```
## application.yml
```yml
server:
  port: 8000

spring:
  application:
    name: mybatis
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver #如果mysql版本低但引入的连接jar包高的话要加一个cj
    url: jdbc:mysql://localhost:3306/my_project?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&useSSL=false
    username: root
    password: 12580

mybatis:
  #mapper xml 扫描路径
  mapperLocations:  classpath*:/mapper/**Mapper.xml,classpath*:/mapper/**/**Mapper.xml
  #配置文件
  config-location: classpath:mybatis-config.xml
    #开启驼峰自动映射
  map-underscore-to-camel-case: true


#开启日志
logging:
  level:
    org.apache.ibatis: INFO

spring.main.allow-circular-references: true
```
