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