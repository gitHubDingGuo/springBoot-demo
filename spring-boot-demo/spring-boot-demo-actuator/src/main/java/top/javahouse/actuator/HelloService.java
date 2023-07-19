package top.javahouse.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Service;

@Endpoint(id = "hello")
@Service
public class HelloService{
    @ReadOperation
    public String hello(){
        return "Hello Endpoint";
    }

}
