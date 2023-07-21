package top.javahouse.swagger.config;
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
