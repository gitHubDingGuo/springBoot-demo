# spring-boot-demo-logback

> 此 demo 主要演示了 Spring Boot 如何使用logback

## Log.java
```java
package top.javahouse.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * @author:javahouse.top
 * @Description: 打印日志
 * @Date: 2023/9/22 13:43
 */
@Component
public class Log {

    Logger logger= LoggerFactory.getLogger(Log.class);

    public Log(){
        logger.info("日志={}","logback");
    }
}
```
## SpringBootDemoLogbackApplication.java
```java
package top.javahouse.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:启动入口
 * @Date: 2023/9/22 13:43
 */
@SpringBootApplication
public class SpringBootDemoLogbackApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoLogbackApplication.class);
        application.run(args);

    }
}
```
## application-local.yml
```yml
logging:
  level:
    org.springframework: INFO

```
## application.yml
```yml
server:
  port: 8000

spring:
  application:
    name: logback
  profiles:
    active: local

```
## logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--
scan：当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
scanPeriod：设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒当scan为true时，此属性生效。默认的时间间隔为1分钟。
debug：当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。
-->
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <!--导入默认的Spring-boot logback配置
        该文件给出了logback的一些默认配置。
    -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <!-- Property 用来定义变量值的标签，
         有两个属性，name和value；其中name的值是变量的名称，value的值时变量定义的值。
         通过定义的值会被插入到logger上下文中。定义变量后，可以使“${}”来使用变量。
    -->
    <!-- 定义日志格式 -->
    <property name="FILE_ERROR_PATTERN"
              value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} %file:%line: %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <!-- appender用来格式化日志输出节点，有俩个属性name和class，class用来指定哪种输出策略，常用就是控制台输出策略和文件输出策略。 -->
    <!-- 控制台打印配置,用于开发环境 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- LevelFilter 根据精确的级别匹配过滤事件。
                 如果事件的级别等于配置的级别，则筛选器接受或拒绝该事件，具体取决于onMatch和onMismatch属性的配置。
                 如下配置，只打印INFO级别日志
            -->
            <level>INFO</level>
            <!-- 匹配到，就打印 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 没有匹配到，就禁止 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- <encoder> 表示对日志进行格式化 -->
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <appender name="FILE_INFO" class="ch.qos.logback.classic.sift.SiftingAppender">
        <!--discriminator鉴别器，根据File_Name这个key对应的value鉴别日志事件，然后委托给具体appender写日志-->
        <discriminator>
            <key>File_Name</key>
            <defaultValue>AepConsumerServe</defaultValue>
        </discriminator>
        <sift>
            <!--
                RollingFileAppender 的作用是滚动记录文件，先将日志记录到指定文件，当符合某个条件时再将日志记录到其他文件
             -->
            <appender name="FILE_INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
                <!--如果只是想要 Info 级别的日志，只是过滤 info 还是会输出 Error 日志，因为 Error 的级别高， 所以我们使用下面的策略，可以避免输出 Error 的日志-->
                <filter class="ch.qos.logback.classic.filter.LevelFilter"> <!-- LevelFilter 根据精确的级别匹配过滤事件 -->
                    <!--过滤 Error-->
                    <level>ERROR</level>
                    <!--匹配到就禁止-->
                    <onMatch>DENY</onMatch>
                    <!--没有匹配到就允许-->
                    <onMismatch>ACCEPT</onMismatch>
                </filter>
                <!--
                    日志名称，
                    如果没有File 属性，那么只会使用FileNamePattern的文件路径规则如果同时有<File>和<FileNamePattern>，那么当天日志是<File>，
                    明天会自动把今天的日志改名为今天的日期。即，<File> 的日志都是当天的。-->
                <!--<File>logs/info.demo-logback.log</File>-->

                <!--滚动策略，按照时间滚动 TimeBasedRollingPolicy-->
                <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                    <!--文件路径,定义了日志的切分方式——把每一天的日志归档到一个文件中,以防止日志填满整个磁盘空间-->
                    <FileNamePattern>D:/logs/GuoXin/${File_Name}/info-%d{yyyy-MM-dd}.part_%i.log</FileNamePattern>
                    <!--只保留最近90天的日志-->
                    <maxHistory>90</maxHistory>
                    <!--用来指定日志文件的上限大小，那么到了这个值，就会删除旧的日志-->
                    <!--<totalSizeCap>1GB</totalSizeCap>-->
                    <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                        <!-- maxFileSize:这是活动文件的大小，默认值是10MB,本篇设置为1KB，只是为了演示 -->
                        <maxFileSize>100MB</maxFileSize>
                    </timeBasedFileNamingAndTriggeringPolicy>
                </rollingPolicy>

                <!--<triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">-->
                <!--<maxFileSize>1KB</maxFileSize>-->
                <!--</triggeringPolicy>-->
                <!-- 日志输出编码格式化 -->
                <encoder>
                    <pattern>${FILE_LOG_PATTERN}</pattern>
                    <charset>UTF-8</charset> <!-- 此处设置字符集 -->
                </encoder>
            </appender>
        </sift>
    </appender>

    <appender name="FILE_ERROR" class="ch.qos.logback.classic.sift.SiftingAppender">
        <discriminator>
            <key>File_Name</key>
            <defaultValue>AepConsumerServe</defaultValue>
        </discriminator>
        <sift>
            <appender name="FILE_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
                <!--如果只是想要 Error 级别的日志，那么需要过滤一下，默认是 info 级别的，ThresholdFilter-->
                <!-- ThresholdFilter 过滤低于指定阈值的事件,如下拒绝所有低于error级别的日志，只输出error以及以上级别的日志：
                -->
                <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
                    <level>Error</level>
                </filter>
                <!--日志名称，如果没有File 属性，那么只会使用FileNamePattern的文件路径规则如果同时有<File>和<FileNamePattern>，那么当天日志是<File>，明天会自动把今天的日志改名为今天的日期。即，<File> 的日志都是当天的。-->
                <!--<File>logs/error.demo-logback.log</File>-->
                <!--滚动策略，按照时间滚动 TimeBasedRollingPolicy-->
                <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                    <!--文件路径,定义了日志的切分方式——把每一天的日志归档到一个文件中,以防止日志填满整个磁盘空间-->
                    <FileNamePattern>D:/logs/GuoXin/${File_Name}/error-%d{yyyy-MM-dd}.part_%i.log</FileNamePattern>
                    <!--只保留最近90天的日志-->
                    <maxHistory>90</maxHistory>
                    <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                        <!-- maxFileSize:这是活动文件的大小，默认值是10MB,本篇设置为1KB，只是为了演示 -->
                        <maxFileSize>100MB</maxFileSize>
                    </timeBasedFileNamingAndTriggeringPolicy>
                </rollingPolicy>
                <encoder>
                    <pattern>${FILE_ERROR_PATTERN}</pattern>
                    <charset>UTF-8</charset> <!-- 此处设置字符集 -->
                </encoder>
            </appender>
        </sift>
    </appender>

    <!--
        root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性。
        level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，不能设置为INHERITED或者同义词NULL。
        默认是DEBUG。
        可以包含零个或多个元素，标识这个appender将会使用root设置的日志级别。
     -->
    <!-- 测试环境，本地，生产环境，输出形式-->
    <springProfile name="local">
        <root level="info">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <springProfile name="prd">
        <root level="info">
            <appender-ref ref="FILE_INFO"/>
            <appender-ref ref="FILE_ERROR"/>
        </root>
    </springProfile>

</configuration>
```

## 参考

https://logback.qos.ch/manual/index.html
