package top.javahouse.ehcache.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


//Spring Boot应用程序在启动后，会遍历CommandLineRunner接口的实例并运行它们的run方法。
// 也可以利用@Order注解（或者实现Order接口）来规定所有CommandLineRunner实例的运行顺序。
//@Order 注解的执行优先级是按value值从小到大顺序。
@Component
@Order(value = 1)
public class MyStartupRunner implements CommandLineRunner {

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public void run(String... args) {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作 注意多副本的问题 <<<<<<<<<<<<<");
        Object data = redisTemplate.opsForValue().get("commandLineRunner");
        if (Objects.isNull(data)) {
            redisTemplate.opsForValue().set("commandLineRunner", "CommandLineRunner");
            System.out.println("Spring Boot 启动以后然后再加载缓存数据 CommandLineRunner");
        }
        System.out.println("CommandLineRunner");
    }

}

