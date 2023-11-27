package top.javahouse.properties.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Data
@Configuration
@ConfigurationProperties(prefix = "developer")
public class DeveloperProperty {
	private String name;
	private String website;
	private String qq;
	private String phoneNumber;
}



