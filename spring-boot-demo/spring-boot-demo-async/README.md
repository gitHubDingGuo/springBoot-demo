# spring-boot-demo-async

> 此 demo 主要演示了 Spring Boot 如何使用原生提供的异步任务支持，实现异步执行任务。

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-demo-async</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

    </dependencies>

    <build>
        <finalName>spring-boot-demo-async</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## application.yml
```yml
server:
  port: 8000

spring:
  application:
    name: async
  task:
    execution:
      pool:
        # 最大线程数
        max-size: 10
        # 核心线程数
        core-size: 5
        # 存活时间
        keep-alive: 10s
        # 队列大小
        queue-capacity: 100
        # 是否允许核心线程超时
        allow-core-thread-timeout: true
      # 线程名称前缀
      thread-name-prefix: async-task-



# 自定义配置核心线程数
async:
  executor:
    thread:
      core_pool_size: 5
      # 配置最大线程数
      max_pool_size: 5
      # 配置队列大小
      queue_capacity: 999
      # 配置线程最大空闲时间
      keep_alive_seconds: 60
      # 配置线程池中的线程的名称前缀
      name:
        prefix: custom-async-

```



## SpringBootDemoAsyncApplication.java
```java
package top.javahouse.async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * @author:javahouse.top
 * @Date: 2023/9/5 13:33
 */
//开启异步
@EnableAsync
@SpringBootApplication
public class SpringBootDemoAsyncApplication {

    @Autowired
    AsyncCaller caller;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAsyncApplication.class);
        application.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("---原生调用----");
            Thread.sleep(1000);
            caller.caller();
            System.out.println("---自定义调用----");
            Thread.sleep(1000);
            caller.poolOne();
            caller.poolTwo();
        };
    }

}

```


## AsyncCaller.java
```java
package top.javahouse.async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * @author:javahouse.top
 * @Description:调用入口
 * @Date: 2023/9/5 15:50
 */
@Component
public class AsyncCaller {

    @Autowired
    TaskFactory taskFactory;

    public void caller() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.doSomething();
    }

    public void poolOne() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.poolOne();
    }

    public void poolTwo() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.poolTwo();
    }

}

```


## TaskFactory.java
```java
package top.javahouse.async;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
/*
 * @author:javahouse.top
 * @Description: 任务工厂
 * @Date: 2023/9/5 15:51
 */
@Component
@Slf4j
public class TaskFactory {

    @Async
    public Future<Boolean> doSomething(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }

    @Async("poolOne")
    public Future<Boolean> poolOne(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----poolOne处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----poolOne处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }


    @Async("poolTwo")
    public Future<Boolean> poolTwo(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----poolTwo处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----poolTwo处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }

}
```

## CustomAsync.java
```java
package top.javahouse.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * @author:javahouse.top
 * @Description:自定义配置Async的线程池。(一个或者多个)
 * @Date: 2023/9/5 15:51
 */
@Configuration
public class CustomAsync {

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.keep_alive_seconds}")
    private int keepAliveSeconds;
    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "poolOne")
    public Executor executorA() {
        System.out.println("开启自定义poolOne的线程池！");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(maxPoolSize);
        // 设置缓冲队列大小
        executor.setQueueCapacity(queueCapacity);
        // 设置线程的最大空闲时间，超过了核心线程数之外的线程，在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置线程名字的前缀，设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(namePrefix);
        // 设置拒绝策略：当线程池达到最大线程数时，如何处理新任务
        // CALLER_RUNS：在添加到线程池失败时会由主线程自己来执行这个任务，
        // 当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行被拒绝的任务；如果执行程序已被关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        executor.initialize();
        return executor;
    }

    @Bean(name = "poolTwo")
    public Executor executorB() {
        System.out.println("开启自定义poolTwo的线程池！");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(maxPoolSize);
        // 设置缓冲队列大小
        executor.setQueueCapacity(queueCapacity);
        // 设置线程的最大空闲时间，超过了核心线程数之外的线程，在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置线程名字的前缀，设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(namePrefix);
        // 设置拒绝策略：当线程池达到最大线程数时，如何处理新任务
        // CALLER_RUNS：在添加到线程池失败时会由主线程自己来执行这个任务，
        // 当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行被拒绝的任务；如果执行程序已被关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        executor.initialize();
        return executor;
    }
}
```

##参考

https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#features.task-execution-and-scheduling