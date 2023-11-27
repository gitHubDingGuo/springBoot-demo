package top.javahouse.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 18:00
 */
@ServletComponentScan(basePackages = {"top.javahouse.session.filter"})
@SpringBootApplication
public class SpringBootDemoSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoSessionApplication.class, args);
	}
}
