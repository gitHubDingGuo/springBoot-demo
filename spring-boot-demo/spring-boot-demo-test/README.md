# spring-boot-demo-async

> 此 demo 主要演示了 Spring Boot 简单的单元测试。

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

    <artifactId>spring-boot-demo-test</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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
        <finalName>spring-boot-demo-test</finalName>
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
    name: test


```

## SpringBootDemoTestApplication.java
```java
package top.javahouse.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/9/7 16:01
 */
@SpringBootApplication
public class SpringBootDemoTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoTestApplication.class, args);
    }

}

```

## TestService.java
```java
package top.javahouse.test.service;

public interface TestService {

    public void doSomething();

}

```

## TestServiceImpl.java
```java
package top.javahouse.test.service.impl;

import org.springframework.stereotype.Service;
import top.javahouse.test.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void doSomething() {
        System.out.println("处理业务逻辑");
    }
}

```

## TestServiceTest.java
```java
package top.javahouse.test.service;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestServiceTest {

     @Autowired
     private  TestService testService;


    @Test
    void doSomething() {
        System.out.println("---执行业务逻辑---");
        testService.doSomething();
        System.out.println("debug-断点");
        //断言二者相等
        Assertions.assertEquals(1,1);
        //断言二者不相等
        Assertions.assertNotEquals(1,2);

        Assertions.assertNull(null);

        Assertions.assertNotNull(null);
        //...... 使用Assertions的方法代替assert可以提高代码可读性
    }

    //all 都是作用于静态方法之上的
    @BeforeAll
    public static void beforeClass() {
        System.out.println("在所有方法执行之前执行 before all");
    }

    @BeforeEach
    public void setup() {
        System.out.println("在每个方法之前 before each ");
    }

    @Test
    public void test_1() {
        System.out.println("test1 执行");
    }

    @Test
    public void test_2() {
        System.out.println("test_2 执行");
    }

    @AfterEach
    public void teardown() {
        System.out.println("在每个测试之后 after each");
    }

    @AfterAll  //只能作用于静态方法
    public static void afterAll() {
        System.out.println("所有测试之后  after all");
    }
}
```


##参考

https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#howto.testing