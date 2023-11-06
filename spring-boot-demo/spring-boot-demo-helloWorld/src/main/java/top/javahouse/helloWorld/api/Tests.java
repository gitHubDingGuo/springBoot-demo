package top.javahouse.helloWorld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties(Datas.class)
public class Tests {

    @Autowired
    private Datas datas;

    @Bean
    public void say(){
        System.out.println("---------------------");
        System.out.println(datas.getServerUrl());
    }
}
