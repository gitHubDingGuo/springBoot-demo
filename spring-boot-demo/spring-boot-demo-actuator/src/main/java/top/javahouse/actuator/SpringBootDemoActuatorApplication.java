package top.javahouse.actuator;


import io.micrometer.core.instrument.Clock;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author:javahouse.top
 * @Date: 2023/7/19 11:39
 */
@SpringBootApplication
@RestController
public class SpringBootDemoActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoActuatorApplication.class, args);
    }


    @Bean
    public InfluxMeterRegistry getMeterRegistry(@Autowired InfluxConfig config){
        return new InfluxMeterRegistry(config, Clock.SYSTEM);
    }



}