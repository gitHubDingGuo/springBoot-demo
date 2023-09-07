# spring-boot-demo-async

> 此 demo 主要演示了 Spring Boot 如何使用cache缓存。

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

    <artifactId>spring-boot-demo-cache-ehcache</artifactId>


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

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

    </dependencies>

    <build>
        <finalName>spring-boot-demo-cache-ehcache</finalName>
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
    name: ehcache

```

## SpringBootDemoEhcacheApplication.java
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
public class SpringBootDemoEhcacheApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoEhcacheApplication.class);
        application.run(args);
    }
}
```

## EhcacheService.java
```java
package top.javahouse.ehcache.service;

import java.util.List;

public interface EhcacheService {

    public List<String> getList();

    public String getPage(int page, int pageSize);

    String getById(Long l, boolean b);

    String findById(Long l);

    public String add(Long id, String content);

    public void delete(Long id);

}
```

## EhcacheServiceImpl.java
```java
package top.javahouse.ehcache.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.javahouse.ehcache.service.EhcacheService;

import java.util.*;

@Service
//@CacheConfig(cacheNames = "cache1") 公共抽取
public class EhcacheServiceImpl implements EhcacheService {

    Map<Long, String> articleMap = new HashMap<>();



    @Cacheable(cacheNames = {"cache1"})
    public List<String> getList() {
        System.out.println("---第一次进来获取列表数据---");
        return Arrays.asList("1", "2", "3", "4");
    }


    @Cacheable(cacheNames = {"cache1"}, key = "#root.target.class.name+'-'+#page+'-'+#pageSize")
    public String getPage(int page, int pageSize) {
        String msg = String.format("page-%s-pageSize-%s", page, pageSize);
        System.out.println("第一次从db中获取数据：" + msg);
        return msg;
    }

    @Override
    @Cacheable(cacheNames = "cache1", key = "'getById'+#id", condition = "#cache")
    public String getById(Long id, boolean cache) {
        System.out.println("获取数据!");
        return "spring缓存:" + UUID.randomUUID().toString();
    }

    /**
     * 获取文章，先从缓存中获取，如果获取的结果为空，不要将结果放在缓存中
     */
    @Cacheable(cacheNames = "cache1", key = "'findById'+#id", unless = "#result==null")
    public String findById(Long id) {
        this.articleMap.put(1L, "spring系列");
        System.out.println("----获取文章:" + id);
        return articleMap.get(id);
    }


    //将结果放入缓存
    @CachePut(cacheNames = "cache1", key = "'findById'+#id")
    public String add(Long id, String content) {
        System.out.println("新增文章:" + id);
        this.articleMap.put(id, content);
        return content;
    }

    //删除
    @CacheEvict(cacheNames = "cache1", key = "'findById'+#id")
    public void delete(Long id) {
        System.out.println("删除文章：" + id);
        this.articleMap.remove(id);
    }
}

```

## EhcacheController.java
```java
package top.javahouse.ehcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.ehcache.service.EhcacheService;


@RestController
public class EhcacheController {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("getList")
    public ResponseEntity<?>  getList(){
        System.out.println(ehcacheService.getList());
        System.out.println(ehcacheService.getList());
        return ResponseEntity.ok(null);
    }

    @GetMapping("getPage")
    public ResponseEntity<?> getPage(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        //page=1,pageSize=10调用2次
        System.out.println(ehcacheService.getPage(1, 10));
        System.out.println(ehcacheService.getPage(1, 10));

        //page=2,pageSize=10调用2次
        System.out.println(ehcacheService.getPage(2, 10));
        System.out.println(ehcacheService.getPage(2, 10));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("getById")
    public ResponseEntity<?> getById(){
        System.out.println(ehcacheService.getById(1L, true));
        System.out.println(ehcacheService.getById(1L, true));
        System.out.println(ehcacheService.getById(1L, false));
        System.out.println(ehcacheService.getById(1L, true));
        return ResponseEntity.ok(null);
    }

    @GetMapping("findById")
    public ResponseEntity<?> findById(){
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(3L));
        System.out.println(ehcacheService.findById(3L));
        {
            System.out.println("下面打印出缓cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        return ResponseEntity.ok(null);
    }


    @GetMapping("add")
    public ResponseEntity<?> add(){
        //新增3个文章，由于add方法上面有@CachePut注解，所以新增之后会自动丢到缓存中
        ehcacheService.add(1L, "java高并发系列");
        ehcacheService.add(2L, "Maven高手系列");
        ehcacheService.add(3L, "MySQL高手系列");

        //然后调用findById获取，看看是否会走缓存
        System.out.println("调用findById方法，会尝试从缓存中获取");
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(2L));
        System.out.println(ehcacheService.findById(3L));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        return ResponseEntity.ok(null);
    }



    @GetMapping("delete")
    public ResponseEntity<?> delete() {
        //第1次调用findById，缓存中没有，则调用方法，将结果丢到缓存中
        System.out.println(ehcacheService.findById(1L));
        //第2次调用findById，缓存中存在，直接从缓存中获取
        System.out.println(ehcacheService.findById(1L));

        //执行删除操作，delete方法上面有@CacheEvict方法，会清除缓存
        ehcacheService.delete(1L);

        //再次调用findById方法，发现缓存中没有了，则会调用目标方法
        System.out.println(ehcacheService.findById(1L));
        return ResponseEntity.ok(null);

    }

}

```

## CacheConfig.java
```java
package top.javahouse.ehcache.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/*
 * @author:javahouse.top
 * @Description: 缓存配置
 * @Date: 2023/9/7 10:46
 */
@EnableCaching
@ComponentScan
@Configuration
public class CacheConfig {
    //缓存管理器
    @Bean
    public CacheManager cacheManager() {
        //创建缓存管理器(ConcurrentMapCacheManager：其内部使用ConcurrentMap实现的)，构造器用来指定缓存的名称，可以指定多个
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("cache1");
        return cacheManager;
    }

}
```
##参考
- Ehcache 官网：http://www.ehcache.org/documentation/
- Spring  官网 https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#io.caching