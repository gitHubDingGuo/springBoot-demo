# spring-boot-demo-mybatis

> 此 demo 主要演示了 Spring Boot 如何使用mybatis

## BookController.java
```java
package top.javahouse.mybatis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.mybatis.entity.Book;
import top.javahouse.mybatis.service.BookService;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/bookOps")
    public void bookOps() {
        Book b1 = new Book();
        b1.setName("西厢记");
        b1.setAuthor("王实甫");
        int i = bookService.addBook(b1);
        System.out.println("addBook>>>" + i);
        Book b2 = new Book();
        b2.setId(1);
        b2.setName("朝花夕拾");
        b2.setAuthor("鲁迅");
        int updateBook = bookService.updateBook(b2);
        System.out.println("updateBook>>" + updateBook);
        Book b3 = bookService.getBookById(1);
        System.out.println("getBookById>>>" + b3);
        int delete = bookService.deleteBookById(2);
        System.out.println("deleteBookById>>" + delete);
        List<Book> allBooks = bookService.getAllBooks();
        System.out.println("getAllBooks>>>" + allBooks);
    }
}
```
## Book.java
```java
package top.javahouse.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private Integer id;
    private String name;
    private String author;
}
```
## BookMapper.java
```java
package top.javahouse.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.javahouse.mybatis.entity.Book;

import java.util.List;

@Mapper
public interface BookMapper {
    int addBook(Book book);

    int deleteBookById(Integer id);

    int updateBookById(Book book);

    Book getBookById(Integer id);

    List<Book> getAllBooks();
}
```
## BookService.java
```java
package top.javahouse.mybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.mybatis.entity.Book;
import top.javahouse.mybatis.mapper.BookMapper;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookMapper bookMapper;

    public int addBook(Book book) {
        return bookMapper.addBook(book);
    }

    public int updateBook(Book book) {
        return bookMapper.updateBookById(book);
    }

    public int deleteBookById(Integer id) {
        return bookMapper.deleteBookById(id);
    }

    public Book getBookById(Integer id) {
        return bookMapper.getBookById(id);
    }

    public List<Book> getAllBooks() {
        return bookMapper.getAllBooks();

    }
}
```
## SpringBootDemoMybatisApplication.java
```java
package top.javahouse.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description: SpringBootDemoMybatisApplication
 * @Date: 2023/9/27 10:44
 */
@SpringBootApplication
public class SpringBootDemoMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoMybatisApplication.class, args);
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
```



## data.db
```
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `author` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `book` VALUES (1, '三国演义', '罗贯中');
INSERT INTO `book` VALUES (2, '水浒传', '施耐庵');
```
## BookMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="top.javahouse.mybatis.mapper.BookMapper">

    <insert id="addBook" parameterType="top.javahouse.mybatis.entity.Book">
    INSERT INTO book (name, author) VALUES (# {name}, # {author})
</insert>

    <delete id="deleteBookById" parameterType="int">
    DELETE FROM book WHERE id=#{id}
</delete>

    <update id="updateBookById" parameterType="top.javahouse.mybatis.entity.Book">
    UPDATE book set name=# { name }, author=# {author} WHERE id=#{id}
</update>


    <select id="getAllBooks" resultType="top.javahouse.mybatis.entity.Book">
    SELECT * FROM book
</select>

    <select id="getBookById" resultType="top.javahouse.mybatis.entity.Book">
    SELECT * FROM book WHERE id=#{id}
    </select>

</mapper>
```
## mybatis-config.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--
    创建一个全局配置文件，这里面是对MyBatis 的核心行为的控制，比如mybatis-config.xml。 然後從yml那里引入
    https://blog.csdn.net/weixin_30718391/article/details/95726928?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-4-95726928-blog-109553684.pc_relevant_multi_platform_featuressortv2removedup&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-4-95726928-blog-109553684.pc_relevant_multi_platform_featuressortv2removedup&utm_relevant_index=8
    -->
    <settings>
        <setting name="jdbcTypeForNull" value="NULL"/>
        <setting name="safeRowBoundsEnabled" value="false"/>
        <setting name="safeResultHandlerEnabled" value="false"/>
        <!-- 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。
        SLF4J | LOG4J | LOG4J2 | JDK_LOGGING | COMMONS_LOGGING | STDOUT_LOGGING | NO_LOGGING -->
        <setting name="logImpl" value="SLF4J"/>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <databaseIdProvider type="DB_VENDOR">
        <property name="SQL Server" value="SQLServer" />
        <property name="MySQL" value="MySQL" />
        <property name="DB2" value="DB2" />
        <property name="Oracle" value="Oracle" />
    </databaseIdProvider>
</configuration>
```

##参考

MyBatis中文文档：[https://mybatis.net.cn/][https_mybatis.net.cn]

GitHub：[https://github.com/mybatis/mybatis-3][https_github.com_mybatis_mybatis-3]
