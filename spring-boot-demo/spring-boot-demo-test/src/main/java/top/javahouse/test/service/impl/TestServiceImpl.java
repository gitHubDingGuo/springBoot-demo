package top.javahouse.test.service.impl;

import org.springframework.stereotype.Service;
import top.javahouse.test.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void doSomething() {
        System.out.println("处理业务逻辑");
    }
}
