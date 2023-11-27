# spring-boot-demo-redis

> 此 demo 主要演示了 Spring Boot 如何使用 Redis

## MyWebMvcConfigurer.java
```java
package top.javahouse.redis.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/*
报错内容
        Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
报错原因
        Springfox使用的路径匹配是基于AntPathMatcher的，而Spring Boot 2.6.X使用的是PathPatternMatcher
解决方案
        在config目录下，新建MyWebMvcConfigurer，修改资源映射路径，完成mvc与swagger对接。
        注意，这一步是最核心的，由于springboot使用的是PathPatternMatcher，所以无法识别swagger的静态资源路径。网上很多的方法指导是将springboot2.6.x版本的路径匹配规则修改回AntPathMatcher，因此要改一堆配置。而直接添加addResourceHandlers的方式，能够避免复杂的配置工作。
        原文链接：https://blog.csdn.net/m0_70651612/article/details/126426084
 */
/*
 * @author:javahouse.top
 * @Date: 2023/7/21 10:56
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger配置
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);

        //解决knife4j的时候出现No mapping for GET /doc.html
        //详情：https://blog.csdn.net/niushijia/article/details/128839264
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
    }
}
```
## RedisConfig.java
```java
package top.javahouse.redis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/9 11:53
 */
@Configuration
public class RedisConfig {

    //覆盖原来的redisTemplate,@Bean的方式配置RedisTemplate，主要是设置RedisConnectionFactory以及各种类型数据的Serializer。
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}

```
## SwaggerThreeConfig.java
```java
package top.javahouse.redis.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/*
 * @author:javahouse.top
 * @Date: 2023/7/21 10:37
 */
@Configuration
@EnableOpenApi
//开启增强文档knife4j
@EnableKnife4j
public class SwaggerThreeConfig {
    /**
     * swagger3的配置文件
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()) //应用文档基本信息
                .select()
                //swagger扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("top.javahouse.redis"))
                //或者扫描对应有注解的方法
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //匹配所有路径
                .paths(PathSelectors.any())
                .build()
                //生成全局通用参数和下面方法对应
                //.globalRequestParameters(getGlobalRequestParameters())
                //生成通用响应信息和下面方法对应
                /*.globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResponseMessage())
                .globalResponses(HttpMethod.DELETE, getGlobalResponseMessage())
                .globalResponses(HttpMethod.PUT, getGlobalResponseMessage())*/
                ;
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        //获取工程名称
        String projectName = System.getProperty("user.dir");
        return new ApiInfoBuilder()
                .title(projectName.substring(projectName.lastIndexOf("\\") + 1) + " API接口文档")
                .contact(new Contact("JavaHouse", "https://javahouse.top", "1@qq.com"))
                .version("1.0")
                .description("API文档")
                .build();
    }

    /**
     * 生成全局通用参数
     * @return
     */
    /*private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("x-access-token")
                .description("令牌")
                .required(false)
                .in(ParameterType.HEADER)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name("Equipment-Type")
                .description("产品类型")
                .required(false)
                .in(ParameterType.HEADER)
                .build());
        return parameters;
    }*/

    /**
     * 生成通用响应信息
     * @return
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }

}
```
## RedisController.java
```java
package top.javahouse.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.redis.entity.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags = "Redis")
@RestController
public class RedisController {


    // 注意：这里@Autowired是报错的，因为@Autowired按照类名注入的
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @ApiOperation("Add")
    @PostMapping("add")
    public ResponseEntity<?> add() {
        List<User> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            User one=new User();
            one.setId(i+"");
            one.setName(i+"name");
            list.add(one);
        }
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("AutomaticConfirmation_autoAckTask", "AutomaticConfirmation_autoAckTask", 5 * 60, TimeUnit.SECONDS);
        redisTemplate.opsForList().rightPushAll("userList",list);
        return ResponseEntity.ok((List<User>)redisTemplate.opsForList().leftPop("userList"));
        //return ResponseEntity.ok(redisTemplate.opsForList().range("userList",0,999999L));
    }


    @ApiOperation("Find")
    @GetMapping("find/{userId}")
    public ResponseEntity<User> edit(@PathVariable("userId") String userId) {
        return ResponseEntity.ok((User)redisTemplate.opsForValue().get(userId));
    }

}
```
## User.java
```java
package top.javahouse.redis.entity;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
}
```
## SpringBootDemoRedisApplication.java
```java
package top.javahouse.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 18:00
 */
@SpringBootApplication
public class SpringBootDemoRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoRedisApplication.class, args);
	}
}
```
## application.yml
```yml
server:
  port: 8081

spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    # password: 123456
    lettuce:
      pool:
        min-idle: 0
        max-active: 8
        max-idle: 8
        max-wait: -1ms
    connect-timeout: 30000ms
```

## 参考 

* https://blog.csdn.net/qq_40036754/article/details/101220473