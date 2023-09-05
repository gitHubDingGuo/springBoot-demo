# spring-boot-demo-admin

> 本 demo 主要演示了 Spring Boot 如何集成 Admin 管控台，监控管理 Spring Boot 应用，分别为 admin 服务端和 admin 客户端，两个模块。

父类pom.xml
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

    <artifactId>spring-boot-demo-admin</artifactId>
    <packaging>pom</packaging>
    <modules>
        <module>spring-boot-demo-admin-server</module>
        <module>spring-boot-demo-admin-clients</module>
    </modules>
</project>
```

## 服务端

### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo-admin</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-demo-admin-server</artifactId>


    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
            <version>2.7.1</version>
        </dependency>

        <!-- security-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

    </dependencies>

    <build>
        <finalName>spring-boot-demo-admin-server</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### application.yml

```yml
server:
  port: 8000

spring:
  application:
    name: adminServer
    #配置登录的账号密码
  security:
    user:
      name: "admin"
      password: "admin"
```

### SecuritySecureConfig
```java
package top.javahouse.adminServer;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter( "redirectTo" );

        http.authorizeRequests()
                .antMatchers( adminContextPath + "/assets/**" ).permitAll()
                .antMatchers( adminContextPath + "/login" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage( adminContextPath + "/login" ).successHandler( successHandler ).and()
                .logout().logoutUrl( adminContextPath + "/logout" ).and()
                .httpBasic().and()
                .csrf().disable();
    }
}
``` 

###
```java
package top.javahouse.adminServer;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
//启动admin服务端服务
@EnableAdminServer
@SpringBootApplication
public class SpringBootDemoAdminServerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAdminServerApplication.class);
        application.run(args);
    }

}
```

##客户端

### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo-admin</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-demo-admin-clients</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
            <version>2.7.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>


    </dependencies>

    <build>
        <finalName>spring-boot-demo-admin-clients</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```



### application.yml

```yml
server:
  port: 8100

spring:
  application:
    name: adminClents
  ##向admin服务端注册
  boot:
    admin:
      client:
        url: http://127.0.0.1:8000
        username: admin
        password: admin

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: '*'
    enabled-by-default: true
```

### SpringBootDemoAdminClientsApplication

```java
package top.javahouse.adminClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Proxy;

/*
 * @author:javahouse.top
 * @Date: 2023/8/31 17:49
 */
@SpringBootApplication
public class SpringBootDemoAdminClientsApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoAdminClientsApplication.class);
        application.run(args);
    }
}

```