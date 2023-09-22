package top.javahouse.docker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerController {

    @GetMapping("data")
    public String hello() {
        return "Hello,From Docker!";
    }
}
