package top.javahouse.ehcache.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class EhcacheControllerTest {

    @Autowired
    private EhcacheController ehcacheController;

    @Test
    void getById() {
        ehcacheController.getById();
    }

    @Test
    void getByIdCondition() {
        ehcacheController.getByIdCondition();
    }

    @Test
    void getByIdUnless() {
        ehcacheController.getByIdUnless();
    }

    @Test
    void add() {
        ehcacheController.add();
    }

    @Test
    void delete() {
        ehcacheController.delete();
    }
}