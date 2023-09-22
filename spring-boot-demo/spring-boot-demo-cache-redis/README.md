# spring-boot-demo-async

> 此 demo 主要演示了 Spring Boot cache 集成redis。

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

    <artifactId>spring-boot-demo-cache-redis</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>2.7.13</version>
        </dependency>


        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

    </dependencies>

    <build>
        <finalName>spring-boot-demo-cache-redis</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
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
    name: cacheRedis
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0 #redis默认数据库为0号,可手动选
  cache:
    redis:
      time-to-live: 1800000   #设置缓存过期时间，可选
```


## RedisConfig.java	
```java

package top.javahouse.ehcache.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * SpringCache配置–RedisConfig.java
 *
 * @author : ourLang
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {

    /**
     * 申明缓存管理器，会创建一个切面（aspect）并触发Spring缓存注解的切点（pointcut）
     * 根据类或者方法所使用的注解以及缓存的状态，这个切面会从缓存中获取数据，将数据添加到缓存之中或者从缓存中移除某个值
     *
     * @return RedisCacheManager
     */
    /*@Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }*/

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
```

## EhcacheController.java	
```java

package top.javahouse.ehcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.ehcache.service.EhcacheService;


@RestController
public class EhcacheController {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("getById")
    public ResponseEntity<?> getById() {
        System.out.println(ehcacheService.getById(1L));
        //第二次会取缓存，如果第一次缓存成功了
        System.out.println(ehcacheService.getById(1L));
        return ResponseEntity.ok(null);
    }

    @GetMapping("getByIdCondition")
    public ResponseEntity<?> getByIdCondition() {
        //当为true时表示先尝试从缓存中获取；若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
        System.out.println(ehcacheService.getByIdCondition(1L,true));
        //直接获取缓存数据
        System.out.println(ehcacheService.getById(1L));
        //删除数据
        ehcacheService.delete(1L);
        //不走缓存、直接执行方法、并且返回结果也不会丢到缓存中
        System.out.println(ehcacheService.getByIdCondition(2L,false));
        System.out.println(ehcacheService.getByIdCondition(2L,false));
        return ResponseEntity.ok(null);
    }

    @GetMapping("getByIdUnless")
    public ResponseEntity<?> getByIdUnless()  {
        //当为true时不走缓存、直接执行方法、并且返回结果也不会丢到缓存中
        System.out.println(ehcacheService.getByIdUnless(1L,true));
        //当为false时表示先尝试从缓存中获取；缓存存在直接返回，若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
        System.out.println(ehcacheService.getByIdUnless(1L,false));
        //这一次取缓存
        System.out.println(ehcacheService.getByIdUnless(1L,false));
        try {
            Thread.sleep(10*1000);
            ehcacheService.delete(1L);
        }catch (Exception e){}
        return ResponseEntity.ok(null);
    }


    @GetMapping("add")
    public ResponseEntity<?> add() {
        /*被标注的方法每次都会被调用，然后方法执行完毕之后，会将方法结果丢到缓存中；当标注在类上，相当于在类的所有方法上标注了@CachePut。
        有3种情况，结果不会丢到缓存当方法向外抛出的时候condition的计算结果为false的时候unless的计算结果为true的时候*/
        //新增文章，由于add方法上面有@CachePut注解，所以新增之后会自动丢到缓存中
        ehcacheService.add(4L, "文章4");
        System.out.println(ehcacheService.getById(4L));
        //相同的key，缓存会更新内容
        ehcacheService.add(4L, "文章444444");
        System.out.println(ehcacheService.getById(4L));
        return ResponseEntity.ok(null);
    }


    @GetMapping("delete")
    public ResponseEntity<?> delete() {
        //执行删除操作，delete方法上面有@CacheEvict方法，会清除缓存
        ehcacheService.delete(1L);
        return ResponseEntity.ok(null);

    }

}
```

## Book.java	
```java

package top.javahouse.ehcache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 2892248514883451461L;
    private Long id;
    private String name;
}
```

## EhcacheService.java	
```java

package top.javahouse.ehcache.service;


import top.javahouse.ehcache.entity.Book;

public interface EhcacheService {

    Book getById(Long l);

    Book getByIdCondition(Long id,boolean isTrue);

    Book getByIdUnless(Long l,boolean isTrue);

    Book add(Long id, String content);

    void delete(Long id);

}
```

## EhcacheServiceImpl.java	
```java

package top.javahouse.ehcache.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.javahouse.ehcache.entity.Book;
import top.javahouse.ehcache.service.EhcacheService;

import java.util.*;

@Service
public class EhcacheServiceImpl implements EhcacheService {

    //public static Map<Long, String> articleMap = new HashMap<>();
    public static Map<Long, Book> articleMap = new HashMap<>();

    static {
        /*articleMap.put(1L, "文章1");
        articleMap.put(2L, "文章2");
        articleMap.put(3L, "文章3");*/
        for(int i=1;i<=3;i++){
            Book book=new Book();
            book.setId((long) i);
            book.setName("文章"+i);
            articleMap.put((long)i,book);
        }
    }

    //@Cacheable 作用: 在方法执行前，spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中
    @Override
    @Cacheable(value = "dataCache", key = "'getById'+#id")
    public Book getById(Long id) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    //condition属性默认为空，表示将缓存所有的调用情形，其值是通过spel表达式来指定的，
            //当为true时表示先尝试从缓存中获取；若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
            //当为false的时候，不走缓存、直接执行方法、并且返回结果也不会丢到缓存中。
    //unless : 表示满足条件则不缓存 ; 与上述的condition是反向的 ;
    @Override
    @Cacheable(value = "dataCache", key = "'getById'+#id", condition = "#isTrue")
    public Book getByIdCondition(Long id,boolean isTrue) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    @Cacheable(value = "dataCache", key = "'getById'+#id", unless = "#isTrue")
    public Book getByIdUnless(Long id,boolean isTrue) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    //将结果放入缓存
    @CachePut(value = "dataCache", key = "'getById'+#id")
    public Book add(Long id, String content) {
        System.out.println("新增文章:" + id);
        //this.articleMap.put(id, content);
        return null;
    }

    // * value：缓存的名称，每个缓存名称下面可以有多个key
    //* key：缓存的key
    //删除
    @CacheEvict(value = "dataCache", key = "'getById'+#id")
    public void delete(Long id) {
        System.out.println("删除文章：" + id);
        this.articleMap.remove(id);
    }
    /*https://blog.csdn.net/doubleedged/article/details/125922281
    https://blog.csdn.net/qq_37493556/article/details/120934545
    https://blog.csdn.net/i402097836/article/details/114977864
    https://blog.csdn.net/zhaoyinjun7897/article/details/119862220
    */


}
```

## SpringBootDemoCacheRedisApplication.java	
```java

package top.javahouse.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/9/7 10:46
 */
@SpringBootApplication
public class SpringBootDemoCacheRedisApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoCacheRedisApplication.class);
        application.run(args);
    }
}
```




##参考
- Ehcache 官网：http://www.ehcache.org/documentation/
- Spring  官网 https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#io.caching