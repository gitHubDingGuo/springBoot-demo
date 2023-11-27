# spring-boot-demo-properties

> 此 demo 主要演示了 Spring Boot 如何使用 properties

## PropertyController.java
```java
package top.javahouse.properties.controller;

import cn.hutool.core.lang.Dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.properties.property.ApplicationProperty;
import top.javahouse.properties.property.DeveloperProperty;

/**
 * <p>
 * 测试Controller
 * </p>
 *
 * @package: com.xkcoding.properties.controller
 * @description: 测试Controller
 * @author: yangkai.shen
 * @date: Created in 2018/9/29 10:49 AM
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@RestController
public class PropertyController {
	private final ApplicationProperty applicationProperty;
	private final DeveloperProperty developerProperty;

	@Autowired
	public PropertyController(ApplicationProperty applicationProperty, DeveloperProperty developerProperty) {
		this.applicationProperty = applicationProperty;
		this.developerProperty = developerProperty;
	}

	@GetMapping("/property")
	public Dict index() {
		return Dict.create().set("applicationProperty", applicationProperty).set("developerProperty", developerProperty);
	}
}
```
## ApplicationProperty.java
```java
package top.javahouse.properties.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApplicationProperty {
	@Value("${application.name}")
	private String name;
	@Value("${application.version}")
	private String version;
}
```
## DeveloperProperty.java
```java
package top.javahouse.properties.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "developer")
@Component
public class DeveloperProperty {
	private String name;
	private String website;
	private String qq;
	private String phoneNumber;
}
```
## SpringBootDemoPropertiesApplication.java
```java
package top.javahouse.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 18:00
 */
@SpringBootApplication
public class SpringBootDemoPropertiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoPropertiesApplication.class, args);
	}
}
```
## application-dev.yml
```yml
application:
  name: dev环境
  version: dev1.0环境

developer:
  name: dev环境
  website: www.dev.com
  qq: dev.qq.com
  phone-number: 123456789
spring:
  rabbitmq:
    listener:
      direct:
        idle-event-interval:


```
## application.yml
```yml
server:
  port: 8081

spring:
  profiles:
    active: dev

```
## additional-spring-configuration-metadata.json
```
{
  "properties": [
    {
      "name": "application.name",
      "description": "Default value is artifactId in pom.xml.",
      "type": "java.lang.String"
    },
    {
      "name": "application.version",
      "description": "Default value is version in pom.xml.",
      "type": "java.lang.String"
    },
    {
      "name": "developer.name",
      "description": "The Developer Name.",
      "type": "java.lang.String"
    },
    {
      "name": "developer.website",
      "description": "The Developer Website.",
      "type": "java.lang.String"
    },
    {
      "name": "developer.qq",
      "description": "The Developer QQ Number.",
      "type": "java.lang.String"
    },
    {
      "name": "developer.phone-number",
      "description": "电话号码.",
      "type": "java.lang.String"
    }
  ]
}
```

## 参考 
*  https://blog.csdn.net/weixin_42159327/article/details/117159864