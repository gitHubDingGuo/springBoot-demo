package top.javahouse.helloWorld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author:javahouse.top
 * @Date: 2023/7/7 15:19
 */
@SpringBootApplication
@RestController
public class SpringBootDemoHelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoHelloWorldApplication.class, args);
    }

    @GetMapping("/helloWorld")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("helloWorld", HttpStatus.OK);
    }

}
