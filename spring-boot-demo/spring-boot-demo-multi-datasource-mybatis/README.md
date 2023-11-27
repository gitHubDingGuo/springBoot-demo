# spring-boot-demo-multi-datasource-mybatis

> 此 demo 主要演示了 Spring Boot 如何使用 动态数据源

## UserController.java
```java
package top.javahouse.multiDatasource.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.service.AllSalveService;
import top.javahouse.multiDatasource.service.UserService;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AllSalveService allSalveService;

    @GetMapping("addMasterUser")
    public void addMasterUser() {
        User user =new User();
        user.setName("master-"+ UUID.randomUUID());
        user.setAge(18);
        userService.addMasterUser(user);
    }

    @GetMapping("addSlaveUser")
    public void addSlaveUser() {
        User user =new User();
        user.setName("slave-"+ UUID.randomUUID());
        user.setAge(18);
        userService.addSlaveUser(user);
    }

    @GetMapping("addAllSlaveUser")
    public void addAllSlaveUser() {
        User user =new User();
        user.setName("all-slave-"+ UUID.randomUUID());
        user.setAge(18);
        allSalveService.addSlaveUser(user);
    }



}
```
## User.java
```java
package top.javahouse.multiDatasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@TableName("multi_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -1923859222295750467L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;
}
```
## UserMapper.java
```java
package top.javahouse.multiDatasource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javahouse.multiDatasource.entity.User;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```
## AllSalveService.java
```java
package top.javahouse.multiDatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javahouse.multiDatasource.entity.User;

public interface AllSalveService extends IService<User> {

    void addSlaveUser(User user);
}
```
## AllSalveServiceImpl.java
```java
package top.javahouse.multiDatasource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.service.AllSalveService;
import top.javahouse.multiDatasource.service.UserService;

/*
 * @author:javahouse.top
 * @Description: 类级别，全部插入从库
 * @Date: 2023/11/7 17:20
 */
@Service
@DS("slave")
public class AllSalveServiceImpl extends ServiceImpl<UserMapper, User> implements AllSalveService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public void addSlaveUser(User user) {
        userMapper.insert(user);
    }
}
```
## UserServiceImpl.java
```java
package top.javahouse.multiDatasource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.service.UserService;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 17:01
 */
@Service
//@DS("master")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
/*
    @DS 可以注解在方法上或类上，同时存在就近原则 方法上注解 优先于 类上注解。
    注解	结果
    没有@DS	默认数据源
    @DS("dsName")	dsName可以为组名也可以为具体某个库的名称*/

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addMasterUser(User user) {
        userMapper.insert(user);
    }

    @DS("slave")
    @Override
    public void addSlaveUser(User user) {
        userMapper.insert(user);
    }
}
```
## UserService.java
```java
package top.javahouse.multiDatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javahouse.multiDatasource.entity.User;


public interface UserService extends IService<User> {

    void addMasterUser(User user);

    void addSlaveUser(User user);
}
```
## SpringBootDemoMultiDatasourceMybatisApplication.java
```java
package top.javahouse.multiDatasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
/*
@MapperScan(basePackages = "top.javahouse.multiDatasource.mapper")
*/
public class SpringBootDemoMultiDatasourceMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoMultiDatasourceMybatisApplication.class, args);
    }

}

```
## application.yml
```yml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          username: root
          password: 12580
          url: jdbc:mysql://127.0.0.1:3306/my_project?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
          driver-class-name: com.mysql.cj.jdbc.Driver
        slave:
          username: root
          password: 12580
          url: jdbc:mysql://127.0.0.1:3306/my_project_two?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
          driver-class-name: com.mysql.cj.jdbc.Driver
      mp-enabled: true

logging:
  level:
     top.javahouse.multiDatasource: debug

server:
  port: 8081
```
## db.sql
```
DROP TABLE IF EXISTS `multi_user`;
CREATE TABLE `multi_user` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(30) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```


## 参考
* https://github.com/baomidou/dynamic-datasource