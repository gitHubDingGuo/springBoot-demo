package top.javahouse.properties.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@ConditionalOnProperty注解是Spring Boot的条件注解，
// 主要用法是根据配置文件中的属性来控制某个配置类是否生效，或者控制某个Bean是否被创建。
/*
属性名称 介绍
prefix 指定属性前缀
name 指定属性名称
havingValue 用来指定期望的属性值，如果配置的值等于期望值则执行
matchIfMissing 当配置文件中不存在指定属性时的处理方式，处理方式有两个，分别是 true 和 false，
true 是不存在指定属性时执行，false 是不执行，默认是 false
*/

@Data
@Configuration
@ConfigurationProperties(prefix = "myproperty")
@ConditionalOnProperty(prefix ="myproperty",name = "openFlag",havingValue ="true")
public class MyConditionalOnProperty {
       private String data;
       private String openFlag;
}
