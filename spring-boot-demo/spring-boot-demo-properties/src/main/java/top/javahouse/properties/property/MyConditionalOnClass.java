package top.javahouse.properties.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*有一行注解为@ConditionalOnClass({ RabbitTemplate.class, Channel.class })，
 则表示当类路径classpath中存在 RabbitTemplate 和 Channel这两个类时，该条件注解才会通过*/
@Data
@Configuration
@ConditionalOnClass({MyConditionalOnClass.class})
public class MyConditionalOnClass {
    private String name;
}
